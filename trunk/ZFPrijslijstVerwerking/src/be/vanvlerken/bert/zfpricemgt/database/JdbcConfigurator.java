/**
 * @author wItspirit
 * 5-mei-2005
 * JdbcConfigurator.java
 */

package be.vanvlerken.bert.zfpricemgt.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.JFrame;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import be.vanvlerken.bert.zfpricemgt.gui.JdbcConfigDialog;

/**
 * This provides the configuration for the JDBC based Price Database
 *
 */
public class JdbcConfigurator implements DBConfigurator
{
    private static final Log            log        = LogFactory.getLog(JdbcConfigurator.class);
    
    private static final String KEY_DBDRIVER = "dbDriver";
    private static final String KEY_DBURL = "dbUrl";
    private static final String KEY_DBUSER = "dbUser";
    private static final String KEY_DBPASSWORD = "dbPassword";
    private static final String KEY_PRICESTABLE = "pricesTable";

    private Preferences prefs;
    
    private List<JdbcDriverConfig> driverList;
    private JdbcDriverConfig activeConfig;
    
    private Connection dbConnection;
    private List<JdbcConfigListener>    configListeners;
    
    public interface JdbcConfigListener
    {
        /**
         * This method is invoked whenever there is a change in the configuration
         * @param connection
         *          The new database connection
         * @param pricesTable
         *          The new name of the prices table
         */
        public void jdbcConfigUpdate(Connection connection, String pricesTable);
    }
    
    public JdbcConfigurator()
    {        
        configListeners = new LinkedList<JdbcConfigListener>();
        
        // Read the standard list of configurations
        List<JdbcDriverConfig> standardDriverList = getStandardDriverList();
        
        // Select a defaultConfig if there is no active configuration
        JdbcDriverConfig defaultConfig;
        if (standardDriverList.isEmpty()) {
        	defaultConfig = new JdbcDriverConfig();
        } else {
        	defaultConfig = standardDriverList.get(0);
        }
        
        // Look up an active configuration if it exists, otherwise load the default into the active
        activeConfig = new JdbcDriverConfig();
        activeConfig.setDescription("Active configuration");
        
        prefs = Preferences.userNodeForPackage(JdbcConfigurator.class);
        activeConfig.setDbDriver(prefs.get(KEY_DBDRIVER, defaultConfig.getDbDriver()));
        activeConfig.setDbUrl(prefs.get(KEY_DBURL, defaultConfig.getDbUrl()));
        activeConfig.setDbUser(prefs.get(KEY_DBUSER, defaultConfig.getDbUser()));
        activeConfig.setDbPassword(prefs.get(KEY_DBPASSWORD, defaultConfig.getDbPassword()));
        activeConfig.setPricesTable(prefs.get(KEY_PRICESTABLE, defaultConfig.getPricesTable()));

        // Create the complete driverList, with the active configuration as the first entry
        driverList = new ArrayList<JdbcDriverConfig>();
        driverList.add(activeConfig);
        driverList.addAll(standardDriverList);
        
        // Load the active configuration
        updateDbConnection();
    }
    
    @SuppressWarnings("unchecked")
	private List<JdbcDriverConfig> getStandardDriverList() {
    	ClassPathResource standardJdbcConfigsLocation = new ClassPathResource("be/vanvlerken/bert/zfpricemgt/database/standardJdbcConfigs.xml");
        BeanFactory beanFactory = new XmlBeanFactory(standardJdbcConfigsLocation);
        
        return (List<JdbcDriverConfig>) beanFactory.getBean("driverList");
    }
    
    private void updateDbConnection()
    {
        try
        {
            Class.forName(activeConfig.getDbDriver());
            dbConnection = DriverManager.getConnection(activeConfig.getDbUrl(), activeConfig.getDbUser(), activeConfig.getDbPassword());
            fireConfigUpdate();
        }
        catch (ClassNotFoundException e)
        {
            log.error("Could not load database driver: "+activeConfig.getDbDriver(), e);
        }
        catch (SQLException e)
        {
            log.error("A problem occured during the creation of the database connection",e);
        }
    }

    public void addConfigListener(JdbcConfigListener configListener)
    {
        synchronized (configListeners)
        {
            configListeners.add(configListener);
        }
    }
    
    public void removeConfigListener(JdbcConfigListener configListener)
    {
        synchronized (configListeners)
        {
            configListeners.remove(configListener);
        }
    }
    
    private void fireConfigUpdate()
    {
        List<JdbcConfigListener> safeListeners;
        synchronized (configListeners)
        {
            safeListeners = new LinkedList<JdbcConfigListener>(configListeners);
        }
        for ( JdbcConfigListener listener : safeListeners)
        {
            listener.jdbcConfigUpdate(dbConnection, activeConfig.getPricesTable());
        }
    }

    public void showConfigurator(JFrame parent)
    {
        JdbcConfigDialog configDialog = new JdbcConfigDialog(parent, driverList);

        int status = configDialog.showJdbcConfigDialog();
        if ( status == JdbcConfigDialog.OK )
        {
            boolean configChanged = false;
            if ( !configDialog.getDbDriver().equals(activeConfig.getDbDriver()) )
            {
                activeConfig.setDbDriver(configDialog.getDbDriver());
                prefs.put(KEY_DBDRIVER, activeConfig.getDbDriver());
                configChanged = true;
            }
            if ( !configDialog.getDbUrl().equals(activeConfig.getDbUrl()) )
            {
                activeConfig.setDbUrl(configDialog.getDbUrl());
                prefs.put(KEY_DBURL, activeConfig.getDbUrl());
                configChanged = true;
            }
            if ( !configDialog.getDbUser().equals(activeConfig.getDbUser()) )
            {
                activeConfig.setDbUser(configDialog.getDbUser());
                prefs.put(KEY_DBUSER, activeConfig.getDbUser());
                configChanged = true;
            }
            if ( !configDialog.getDbPassword().equals(activeConfig.getDbPassword()) )
            {
                activeConfig.setDbPassword(configDialog.getDbPassword());
                prefs.put(KEY_DBPASSWORD, activeConfig.getDbPassword());
                configChanged = true;
            }
            if ( !configDialog.getPricesTable().equals(activeConfig.getPricesTable()) )
            {
                activeConfig.setPricesTable(configDialog.getPricesTable());
                prefs.put(KEY_PRICESTABLE, activeConfig.getPricesTable());
                configChanged = true;
            }
            if ( configChanged )
            {
                updateDbConnection();
            }
        }  
    }

    public Connection getDbConnection()
    {
        return dbConnection;
    }
    

    public String getPricesTable()
    {
        return activeConfig.getPricesTable();
    }
    

}

