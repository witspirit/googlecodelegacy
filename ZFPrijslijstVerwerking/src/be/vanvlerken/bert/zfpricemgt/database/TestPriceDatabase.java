/**
 * @author wItspirit
 * 4-jan-2004
 * TestPriceDatabase.java
 */

package be.vanvlerken.bert.zfpricemgt.database;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import be.vanvlerken.bert.zfpricemgt.AlreadyExistsException;
import be.vanvlerken.bert.zfpricemgt.IProduct;
import be.vanvlerken.bert.zfpricemgt.database.exporters.ExportStatusReport;
import be.vanvlerken.bert.zfpricemgt.database.exporters.Exporter;
import be.vanvlerken.bert.zfpricemgt.database.importers.Importer;

/**
 * This is a dummy implementation of the IZFPriceDatabase interface It just
 * contains a few dummy values.
 */
public class TestPriceDatabase implements IZFPriceDatabase
{
    private static final ResourceBundle dbErrors = ResourceBundle.getBundle("be.vanvlerken.bert.zfpricemgt.database.localization.DatabaseErrorConditions");

    private Map<ProductKey, IProduct> products = null;
    private DateFormat df;
    
    private class ProductKey
    {
        private String productNumber;
        private Date   validSince;

        public ProductKey(IProduct product)
        {
            productNumber = product.getNumber();
            validSince = product.getValidSince();
        }

        public boolean equals(Object e)
        {
            if (e instanceof ProductKey)
            {
                ProductKey otherKey = (ProductKey) e;
                return productNumber.equals(otherKey.productNumber) && validSince.equals(otherKey.validSince);
            }
            return false;
        }

        public int hashCode()
        {
            return productNumber.hashCode() & validSince.hashCode();
        }

        public String toString()
        {
            return "ProductKey(" + productNumber + ", " + df.format(validSince) + ")";
        }
    }

    public TestPriceDatabase()
    {        
        products = new HashMap<ProductKey, IProduct>();
        try
        {
            df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRANCE);
            Date strangeDate = new Date();
            Date normalDate = df.parse("16/01/2005");
            Date oldDate = df.parse("01/01/2004");
            Date olderDate = df.parse("01/01/2003");

            addProduct(new Product("0301 301 009", "DICHTUNG", 1.98, normalDate));
            addProduct(new Product("0301 301 012", "DICHTUNG", 8.64, normalDate));
            addProduct(new Product("0301 301 017", "UEBERWURFMUTTE", 3.46, normalDate));
            addProduct(new Product("0301 301 018", "VERSCHLUSSKAPPE", 2.11, normalDate));
            addProduct(new Product("0301 302 001", "AUSSENLAMELLE", 2.5, normalDate));
            addProduct(new Product("0301 302 004", "ENDLAMELLE", 17.66, normalDate));
            addProduct(new Product("0301 302 020", "ANLAUFSCHEIBE", 0.38, normalDate));
            addProduct(new Product("0301 302 031", "TELLERFEDER", 9.28, normalDate));
            addProduct(new Product("0301 302 037", "AUSSENLAMELLE", 12.86, normalDate));

            addProduct(new Product("0301 301 009", "DICHTUNG", 1.98, strangeDate));

            addProduct(new Product("0301 301 012", "DICHTUNG", 8.64, oldDate));
            addProduct(new Product("0301 301 012", "DICHTUNG", 8.64, olderDate));

            addProduct(new Product("0301 301 010", "HALTEWINKEL", 1.34, oldDate));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        catch (AlreadyExistsException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @see be.vanvlerken.bert.zfpricemgt.database.IZFPriceDatabase#getProduct(java.lang.String)
     */
    public IProduct getProduct(String productNumber)
    {
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
        List<IProduct> allProducts = new LinkedList<IProduct>();
        for (IProduct product : products.values())
        {
            if (product.getNumber().equals(productNumber))
            {
                allProducts.add(product);
            }
        }
        return allProducts.toArray(new IProduct[0]);
    }

    /**
     * @throws AlreadyExistsException 
     * @see be.vanvlerken.bert.zfpricemgt.database.IZFPriceDatabase#addProduct(be.vanvlerken.bert.zfpricemgt.IProduct)
     */
    public void addProduct(IProduct newProduct) throws AlreadyExistsException
    {
        ProductKey key = new ProductKey(newProduct);
        if (products.containsKey(key))
        {
            // Fail
            throw new AlreadyExistsException(MessageFormat.format(dbErrors.getString("product.already.exists"), key));
        }
        else
        {
            products.put(key, newProduct);            
        }
    }

    /**
     * @see be.vanvlerken.bert.zfpricemgt.database.IZFPriceDatabase#deleteProduct(be.vanvlerken.bert.zfpricemgt.IProduct)
     */
    public void deleteProduct(IProduct product)
    {
        ProductKey key = new ProductKey(product);
        products.remove(key);     
    }

    public void exportProducts(ExportStatusReport report, Exporter exporter, Date validSince)
    {       
        int size = products.values().size();
        int current = 0;
        double progress;
        for (IProduct product : products.values() )
        {
            if ( report.isCancelled() ) break;
            progress = ((double) current / (double) size)*100;
            report.setProgress(progress);
            current++;
            if ( validSince == null || product.getValidSince().after(validSince) )
            {
                exporter.export(product);
            }
        }        
    }
    public void importProducts(Importer importer)
    {
        IProduct importProduct = importer.nextProduct();
        while ( importProduct != null )
        {
            try
            {
                addProduct(importProduct);
            }
            catch (AlreadyExistsException e)
            {
                // Silent ignore
            }
            importProduct = importer.nextProduct();
        }
    }

    public DBConfigurator getConfigurator()
    {
        return null;
    }
}
