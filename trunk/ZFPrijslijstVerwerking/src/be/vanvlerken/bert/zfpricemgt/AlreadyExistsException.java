/**
 * @author wItspirit
 * 30-jan-2005
 * AlreadyExistsException.java
 */

package be.vanvlerken.bert.zfpricemgt;


/**
 * Thrown when a record is being added that already exists
 */
public class AlreadyExistsException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	/**
     * 
     */
    public AlreadyExistsException()
    {
        super();
    }
    /**
     * @param arg0
     */
    public AlreadyExistsException(String arg0)
    {
        super(arg0);
    }
    /**
     * @param arg0
     * @param arg1
     */
    public AlreadyExistsException(String arg0, Throwable arg1)
    {
        super(arg0, arg1);
    }
    /**
     * @param arg0
     */
    public AlreadyExistsException(Throwable arg0)
    {
        super(arg0);
    }
}
