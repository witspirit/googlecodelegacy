/**
 * @author wItspirit
 * 27-jun-2003
 * TimeoutException.java
 */

package be.vanvlerken.bert.packetdistributor.common;


/**
 * An exception that flags any kind of timeout
 */
public class TimeoutException extends Exception
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3905524891045146934L;

    public TimeoutException(String message)
    {
        super(message);
    }
}
