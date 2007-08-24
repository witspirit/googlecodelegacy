/**
 * @author wItspirit
 * 30-okt-2005
 * UrlUtils.java
 */

package be.vanvlerken.bert.flickrstore.gui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UrlUtils
{
    public static String extractPhotosetId(String url)
    {
        String workStr = url.trim();
        System.out.println("URL: "+workStr);
        
        Pattern regex = Pattern.compile("http://www.flickr.com/photos/.*/sets/([^/]*)/?");
        Matcher matcher = regex.matcher(workStr);
        if ( matcher.matches() )
        {
            workStr = matcher.group(1);
            return workStr;
        }
        return null;
    }
}
