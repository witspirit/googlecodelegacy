/**
 * @author wItspirit
 * 4-apr-2003
 * MaximumExceededException.java
 */
package be.vanvlerken.bert.packetdistributor.common;

/**
 * Thrown when adding another entity to something is impossible due to some limitation of
 * the implementation
 */
public class MaximumExceededException extends Exception
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3257850982619559985L;

    public MaximumExceededException(String message)
    {
        super(message);
    }
}
