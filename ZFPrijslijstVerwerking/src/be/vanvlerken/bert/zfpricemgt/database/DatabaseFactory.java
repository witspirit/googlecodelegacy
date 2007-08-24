/**
 * @author wItspirit
 * 4-jan-2004
 * DatabaseFactory.java
 */

package be.vanvlerken.bert.zfpricemgt.database;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * Creates instances of a certain IZFPriceDatabase implementation
 */
public class DatabaseFactory
{
    private IZFPriceDatabase            database;

    public DatabaseFactory() throws Exception
    {
        // Read in some configuration parameters from a properties file
        // Use that to create a certain implementation
        ClassPathResource dbFactoryConfig = new ClassPathResource("be/vanvlerken/bert/zfpricemgt/database/databaseFactory.xml");
        BeanFactory dbFactory = new XmlBeanFactory(dbFactoryConfig);
        database = (IZFPriceDatabase) dbFactory.getBean("databaseInterface");
    }

    public IZFPriceDatabase getDatabase()
    {
        return database;
    }
}
