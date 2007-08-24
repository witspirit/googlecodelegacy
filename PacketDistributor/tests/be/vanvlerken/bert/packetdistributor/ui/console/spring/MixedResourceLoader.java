/**
 * @author wItspirit
 * 26-dec-2004
 * MixedResourceLoader.java
 */

package be.vanvlerken.bert.packetdistributor.ui.console.spring;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;


/**
 * This resource loader combines the features of the DefaultResourceLoader and the FileSystemResourceLoader
 * It will first look in the filesystem, if it fails to find it there, a lookup will be done on the classpath.
 */
public class MixedResourceLoader implements ResourceLoader
{
    private ResourceLoader classPathLoader;
    private ResourceLoader fileSystemLoader;

    public MixedResourceLoader()
    {
        classPathLoader = new DefaultResourceLoader();
        fileSystemLoader = new FileSystemResourceLoader();
    }

    /**
     * @see org.springframework.core.io.ResourceLoader#getResource(java.lang.String)
     */
    public Resource getResource(String resourceName)
    {
        Resource resource = fileSystemLoader.getResource(resourceName);
        if ( !resource.exists() )
        {
            resource = classPathLoader.getResource(resourceName);
        }
        return resource;
    }

}
