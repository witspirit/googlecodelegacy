/**
 * @author wItspirit
 * 16-okt-2005
 * FlickrCommunicationException.java
 */

package be.vanvlerken.bert.flickrstore;

/**
 * This exception is thrown whenever there was an underlying exception.
 * These could be either IOException, SAXException or FlickrException
 */
public class FlickrCommunicationException extends Exception
{

    public FlickrCommunicationException()
    {
        super();
    }

    public FlickrCommunicationException(String msg)
    {
        super(msg);
    }

    public FlickrCommunicationException(String msg, Throwable t)
    {
        super(msg, t);
    }

    public FlickrCommunicationException(Throwable t)
    {
        super(t);
    }

}
