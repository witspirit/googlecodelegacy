/**
 * @author wItspirit
 * 4-jan-2004
 * JdbcPriceDatabase.java
 */

package be.vanvlerken.bert.zfpricemgt.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.vanvlerken.bert.zfpricemgt.AlreadyExistsException;
import be.vanvlerken.bert.zfpricemgt.IProduct;
import be.vanvlerken.bert.zfpricemgt.database.JdbcConfigurator.JdbcConfigListener;
import be.vanvlerken.bert.zfpricemgt.database.exporters.ExportStatusReport;
import be.vanvlerken.bert.zfpricemgt.database.exporters.Exporter;
import be.vanvlerken.bert.zfpricemgt.database.importers.Importer;

/**
 * Performs database interaction using JDBC
 */
public class JdbcPriceDatabase implements IZFPriceDatabase, JdbcConfigListener
{
    private static final ResourceBundle dbErrors   = ResourceBundle
                                                           .getBundle("be.vanvlerken.bert.zfpricemgt.database.localization.DatabaseErrorConditions");
    private static final Log            log        = LogFactory.getLog(JdbcPriceDatabase.class);

    private static final int            BATCH_SIZE = 250;

    private PreparedStatement           getProductsQuery;
    private PreparedStatement           getAllProductsQuery;
    private PreparedStatement           getAllProductsSinceQuery;
    private PreparedStatement           insertProductUpdate;
    private PreparedStatement           deleteProductUpdate;
    private Connection                  dbConnection;

    /**
     * During a batch import, there could be a problem with one of the records.
     * This will cause a rollback of the complete batch. In order to automate
     * the failure recovery and limit the impact to only those records that have
     * actually failed, we keep a cache of all the products in a batch. When the
     * batch is succesfully committed, the cache is cleared. When a batch
     * rollsback we go in 'failsafe' mode and we revert to adding a record one
     * at a time, using the elements in the cache. We then only log those
     * records that have actually failed.
     */
    private List<IProduct>              batchCache;
    
    private JdbcConfigurator            configurator;
    private boolean                     configReady;

    public JdbcPriceDatabase()
    {
        configReady = false;
        configurator = new JdbcConfigurator();
        configurator.addConfigListener(this);
    }
    
    private void init()
    {
        if ( configReady )
        {
            return;
        }
        setDbConnection(configurator.getDbConnection(), configurator.getPricesTable());
        configReady = true;
    }

    private void setDbConnection(Connection newConnection, String newPricesTable)
    {
        PreparedStatement newGetProductsQuery;
        PreparedStatement newGetAllProductsQuery;
        PreparedStatement newGetAllProductsSinceQuery;
        PreparedStatement newInsertProductUpdate;
        PreparedStatement newDeleteProductUpdate;
        try
        {
            newGetProductsQuery = newConnection.prepareStatement("SELECT * FROM " + newPricesTable + " AS T1 WHERE T1.product_number = ?");
            newGetAllProductsQuery = newConnection.prepareStatement("SELECT * FROM " + newPricesTable, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            newGetAllProductsSinceQuery = newConnection.prepareStatement("SELECT * FROM " + newPricesTable + " WHERE valid_since >= ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            newInsertProductUpdate = newConnection.prepareStatement("INSERT INTO " + newPricesTable + " VALUES (?,?,?,?)");
            newDeleteProductUpdate = newConnection
                    .prepareStatement("DELETE FROM " + newPricesTable + " WHERE product_number = ? AND valid_since = ?");
        }
        catch (SQLException e)
        {
            log.error("A problem occured during the update of the database connection.", e);
            log.error("No changes were made.");
            return;
        }

        // Everything went fine... Time to commit the new queries
        try
        {
            if (dbConnection != null)
            {
                if (!dbConnection.isClosed())
                {
                    dbConnection.close();
                }
            }
        }
        catch (SQLException e)
        {
            log.warn("No clean stop of the old db connection.", e);
        }

        dbConnection = newConnection;
        getProductsQuery = newGetProductsQuery;
        getAllProductsQuery = newGetAllProductsQuery;
        getAllProductsSinceQuery = newGetAllProductsSinceQuery;
        insertProductUpdate = newInsertProductUpdate;
        deleteProductUpdate = newDeleteProductUpdate;

        // Initialize the batchCache
        batchCache = new ArrayList<IProduct>(BATCH_SIZE);
    }

    /**
     * @see be.vanvlerken.bert.zfpricemgt.database.IZFPriceDatabase#getProduct(java.lang.String)
     */
    public IProduct getProduct(String productNumber)
    {
        init();
        // We choose this sub-optimal implementation, because MySQL doesn't
        // support nested SELECTs
        IProduct[] allProducts = getProducts(productNumber);
        if (allProducts.length == 0) { return null; }
        IProduct mostRecent = allProducts[0];
        for (IProduct product : allProducts)
        {
            if (product.getValidSince().after(mostRecent.getValidSince()))
            {
                mostRecent = product;
            }
        }
        return mostRecent;
    }

    /**
     * @see be.vanvlerken.bert.zfpricemgt.database.IZFPriceDatabase#getProducts(java.lang.String)
     */
    public IProduct[] getProducts(String productNumber)
    {
        init();
        List<IProduct> products = new LinkedList<IProduct>();

        try
        {
            getProductsQuery.setString(1, productNumber);
            ResultSet results = getProductsQuery.executeQuery();

            while (results.next())
            {
                String number;
                String description;
                double price;
                Date validSince;

                number = results.getString("product_number");
                description = results.getString("description");
                price = results.getDouble("price");
                validSince = results.getDate("valid_since");

                products.add(new Product(number, description, price, validSince));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return products.toArray(new IProduct[0]);
    }

    /**
     * @see be.vanvlerken.bert.zfpricemgt.database.IZFPriceDatabase#addProduct(be.vanvlerken.bert.zfpricemgt.IProduct)
     */
    public void addProduct(IProduct newProduct) throws AlreadyExistsException
    {
        init();
        try
        {
            insertProductUpdate.setString(1, newProduct.getNumber());
            insertProductUpdate.setString(2, newProduct.getDescription());
            insertProductUpdate.setDouble(3, newProduct.getPrice());
            java.sql.Date sqlDate = new java.sql.Date(newProduct.getValidSince().getTime());
            insertProductUpdate.setDate(4, sqlDate);
            insertProductUpdate.executeUpdate();
        }
        catch (SQLException e)
        {
            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRANCE);
            String key = "key (" + newProduct.getNumber() + ", " + df.format(newProduct.getValidSince()) + ")";
            throw new AlreadyExistsException(MessageFormat.format(dbErrors.getString("product.already.exists"), key), e);
        }
    }

    /**
     * @see be.vanvlerken.bert.zfpricemgt.database.IZFPriceDatabase#deleteProduct(be.vanvlerken.bert.zfpricemgt.IProduct)
     */
    public void deleteProduct(IProduct product)
    {
        init();
        try
        {
            deleteProductUpdate.setString(1, product.getNumber());
            java.sql.Date sqlDate = new java.sql.Date(product.getValidSince().getTime());
            deleteProductUpdate.setDate(2, sqlDate);
            deleteProductUpdate.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    protected void finalize() throws Throwable
    {
        if (dbConnection != null)
        {
            if (!dbConnection.isClosed())
            {
                dbConnection.close();
            }
        }
    }

    public void exportProducts(ExportStatusReport report, Exporter exporter, Date validSince)
    {
        init();
        try
        {
            double progress = 0.00;
            report.setProgress(progress);
            int rows = 0;
            ResultSet results;
            if (validSince == null)
            {
                // Export ALL products
                results = getAllProductsQuery.executeQuery();
            }
            else
            {
                // Export only products with a price valid after validSince
                java.sql.Date sqlDate = new java.sql.Date(validSince.getTime());
                getAllProductsSinceQuery.setDate(1, sqlDate);
                results = getAllProductsSinceQuery.executeQuery();
            }
            // Fetch the number of rows ... A little bit annoying !
            // Requires ResultSet.TYPE_SCROLL_INSENSITIVE !
            results.last(); // Move to the last row
            rows = results.getRow(); // Obtain the number of this last row
            results.beforeFirst(); // Put ourselves back in the beginning

            log.debug("Start export of " + rows + " records");

            while (!report.isCancelled() && results.next())
            {
                String number;
                String description;
                double price;
                Date pValidSince;

                number = results.getString("product_number");
                description = results.getString("description");
                price = results.getDouble("price");
                pValidSince = results.getDate("valid_since");

                exporter.export(new Product(number, description, price, pValidSince));
                progress = ((double) results.getRow() / (double) rows) * 100;
                // log.debug("Setting progress to "+progress);
                report.setProgress(progress);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void importProducts(Importer importer)
    {
        init();
        IProduct importProduct = importer.nextProduct();
        int currentBatchSize = 0;
        int recordNr = 1;
        batchCache.clear(); // Just to make sure we start with a clean slate
        while (importProduct != null)
        {
            batchCache.add(importProduct); // Add the product to the failsafe
            // cache
            try
            {
                insertProductUpdate.setString(1, importProduct.getNumber());
                insertProductUpdate.setString(2, importProduct.getDescription());
                insertProductUpdate.setDouble(3, importProduct.getPrice());
                java.sql.Date sqlDate = new java.sql.Date(importProduct.getValidSince().getTime());
                insertProductUpdate.setDate(4, sqlDate);

                insertProductUpdate.addBatch(); // Queue up multiple updates
                currentBatchSize++;
            }
            catch (SQLException e)
            {
                log.warn("Received an SQLException during import of record " + recordNr + " (" + importProduct + ")", e);
            }

            if (currentBatchSize >= BATCH_SIZE)
            {
                try
                {
                    insertProductUpdate.executeBatch();
                }
                catch (SQLException e)
                {
                    log.warn("Could not perform batch update. (records " + (recordNr + 1 - currentBatchSize) + " - " + recordNr + ")", e);
                    // Let's go safemode
                    safeImport(recordNr + 1 - currentBatchSize);
                }
                currentBatchSize = 0;
                batchCache.clear();
            }

            importProduct = importer.nextProduct();
            recordNr++;
        }
        try
        {
            insertProductUpdate.executeBatch();
            batchCache.clear();
        }
        catch (SQLException e)
        {
            log.warn("Could not perform last batch update. (records " + (recordNr - currentBatchSize) + " - " + (recordNr - 1) + ")", e);
            // Let's go safemode
            safeImport(recordNr - currentBatchSize);
        }
    }

    /**
     * Performs a record by record import of the records stored in the
     * batchCache
     */
    private void safeImport(int baseRecordNr)
    {
        log.warn("Use safe mode for failed batch...");
        int recordNr = baseRecordNr;
        for (IProduct product : batchCache)
        {
            try
            {
                addProduct(product);
            }
            catch (AlreadyExistsException e)
            {
                log.error("Import failed for record " + recordNr + " (" + product + ") : " + e.getLocalizedMessage());
            }
            recordNr++;
        }
        batchCache.clear();
        log.warn("Batch fully processed in safe mode.");
    }

    public DBConfigurator getConfigurator()
    {
        return configurator;
    }

    public void jdbcConfigUpdate(Connection connection, String pricesTable)
    {
        configReady = false;
    }
}
