/**
 * @author wItspirit
 * 4-apr-2003
 * ITrafficDestination.java
 */
package be.vanvlerken.bert.packetdistributor.common;

/**
 * The API definition for sending traffic to a certain destination, using a certain protocol
 */
public interface ITrafficDestination extends ITrafficEndpoint
{
    public abstract void send(DataChunk data) throws DeliveryException;
}
