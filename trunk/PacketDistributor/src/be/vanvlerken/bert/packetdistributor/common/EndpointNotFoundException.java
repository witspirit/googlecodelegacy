/**
 * @author wItspirit
 * 4-apr-2003
 * EndpointNotFoundException.java
 */
package be.vanvlerken.bert.packetdistributor.common;

/**
 * Thrown when a certain entity you are looking for cannot be found on the queried object
 */
public class EndpointNotFoundException extends Exception
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3257002172461101110L;

    public EndpointNotFoundException(String message)
    {
        super(message);
    }
}
