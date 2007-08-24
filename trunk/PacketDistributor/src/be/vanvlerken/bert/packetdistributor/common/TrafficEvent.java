/**
 * @author wItspirit
 * 4-apr-2003
 * TrafficEvent.java
 */package be.vanvlerken.bert.packetdistributor.common;

import java.util.EventObject;


/**
 * Event to transport a received DataChunk to it's listeners
 */
public class TrafficEvent extends EventObject
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3256445823838532145L;
    private DataChunk dataChunk;
    
    public TrafficEvent(Object source, DataChunk data)
    {
        super(source);
        this.dataChunk = data;
    }
    
    /**
     * Returns the dataChunk.
     * @return DataChunk
     */
    public DataChunk getDataChunk()
    {
        return dataChunk;
    }

}
