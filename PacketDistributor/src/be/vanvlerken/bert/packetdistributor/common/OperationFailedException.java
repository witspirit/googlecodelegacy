/**
 * @author wItspirit
 * 27-apr-2003
 * OperationFailedException.java
 */
package be.vanvlerken.bert.packetdistributor.common;

/**
 * Thrown when an operation could not be performed
 */
public class OperationFailedException extends Exception
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3906366013262999607L;

    public OperationFailedException(String message)
    {
        super(message);
    }
}
