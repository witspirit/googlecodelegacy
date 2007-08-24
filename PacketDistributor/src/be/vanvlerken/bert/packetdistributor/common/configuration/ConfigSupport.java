/*
 * @author vlerkenb
 * 12-nov-2004
 * ConfigSupport.java
 */
package be.vanvlerken.bert.packetdistributor.common.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This file offers support with regards to configuration It aides in fetching
 * properties files and parsing command line arguments.
 */
public class ConfigSupport
{
    /**
     * Searches for a property file called 'name' This implementation will look
     * in several places, in sequence. The first found will be used.
     * <ul>
     * <li>name in working directory</li>
     * <li>name on classpath</li>
     * <li>etc/name in working directory</li>
     * <li>etc/name on classpath</li>
     * </ul>
     * 
     * @param name
     *            The name of the property file to look for
     * @return A Properties object. When the file has been found, populated,
     *         otherwise empty.
     * @throws IOException
     */
    public static Properties getProperties(String name) throws IOException
    {
        Properties props = new Properties();
        InputStream propertyStream = null;

        propertyStream = getResource(name);
        if (propertyStream == null) { return props; }

        props.load(propertyStream);
        return props;
    }

    /**
     * Searches for a resource called 'name' This implementation will look in
     * several places, in sequence. The first found will be used.
     * <ul>
     * <li>name in working directory</li>
     * <li>name on classpath</li>
     * <li>etc/name in working directory</li>
     * <li>etc/name on classpath</li>
     * </ul>
     * 
     * @param name
     *            The name of the resource to look for
     * @return An InputStream linked to the resource or null when the resource
     *         was not found
     * @throws IOException
     */
    public static InputStream getResource(String name) throws IOException
    {
        InputStream resourceStream = null;

        // Go over several possibilities... First match wins.
        resourceStream = getResourceStream(name);
        if (resourceStream == null)
        {
            String etcName = "etc/" + name;
            resourceStream = getResourceStream(etcName);
        }

        return resourceStream;
    }

    private static InputStream getResourceStream(String name)
    {
        InputStream resourceStream = null;
        File resourceFile = new File(name);
        if (resourceFile.canRead())
        {
            try
            {
                resourceStream = new FileInputStream(resourceFile);
                return resourceStream;
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }

        resourceStream = ConfigSupport.class.getClassLoader()
                .getResourceAsStream(name);
        return resourceStream;
    }
}
