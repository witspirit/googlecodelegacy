/**
 * @author vlerkenb
 * 10-feb-2003
 * MixedModePacket.java
 */
package be.vanvlerken.bert.logmonitor.logging;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * Encapsulates a DatagramPacket and provides an extra interface to
 * get and set Logging specific properties
 */
public class MixedModePacket
{
    private static final int SEQNR_OFFSET   = 0;
    private static final int TYPE_OFFSET    = 4;
    private static final int MESSAGE_OFFSET = 5;
    
    private static final int MAX_MESSAGE_LENGTH = 1500; 
    
    private DatagramPacket udpPacket;
    private byte[] buffer;
    
    
    
    public MixedModePacket(int seqNr, int type, String message)
    {
        buffer = new byte[MESSAGE_OFFSET+message.length()+1];
        
        setSeqNr(seqNr);
        setType(type);
        setMessage(message);
        
        udpPacket = new DatagramPacket(buffer, buffer.length);
    }
    
    public MixedModePacket(int seqNr, int type, String message, InetSocketAddress destinationSocket) throws SocketException
    {
        buffer = new byte[MESSAGE_OFFSET+message.length()+1];
        
        setSeqNr(seqNr);
        setType(type);
        setMessage(message);
        
        udpPacket = new DatagramPacket(buffer, buffer.length, destinationSocket);
    }
    

    public MixedModePacket()
    {
        buffer = new byte[MAX_MESSAGE_LENGTH];
        this.udpPacket = new DatagramPacket(buffer, MAX_MESSAGE_LENGTH);
    }
    
    public void setMessage(String message)
    {
        byte[] stringBytes = message.getBytes();
        
        int stringLength = message.length() + 1;
        int messageBufferLength = buffer.length-MESSAGE_OFFSET;
        
        int minLength;
        
        if ( stringLength > messageBufferLength )
        {
            minLength = messageBufferLength;
        }
        else
        {
            minLength = stringLength;
        }
        
        for (int i=0; i < minLength-1; i++)
        {
            buffer[MESSAGE_OFFSET+i] = stringBytes[i];
        }
        buffer[MESSAGE_OFFSET+minLength-1] = 0;
    }
    
    public String getMessage()
    {
        int stringLength = 0;
        
        /* Count the stringLength */
        for (int i=MESSAGE_OFFSET; i < buffer.length; i++)
        {
            if ( buffer[i] == 0 )
            {
                stringLength = i-MESSAGE_OFFSET;
                break;
            }
        }
        
        
        byte[] stringBytes = new byte[stringLength];
        
        for (int i=0; i < stringLength; i++)
        {
            stringBytes[i] = buffer[MESSAGE_OFFSET+i];
        }
        
        return new String(stringBytes);
    }
    
    public void setType(int type)
    {
        buffer[TYPE_OFFSET] = (byte) type;
    }
    
    public int getType()
    {
        return (int) buffer[TYPE_OFFSET];
    }
    
    public void setSeqNr(long seqNr)
    {
        byte[] seqNrBytes = new byte[4];
        
        seqNrBytes[0] = (byte) (seqNr % (256*256*256)); 
        seqNrBytes[1] = (byte) ((seqNr % (256*256)) / 256);
        seqNrBytes[2] = (byte) ((seqNr % 256) / (256*256));
        seqNrBytes[3] = (byte) (seqNr / ( 256*256*256));
        
        buffer[SEQNR_OFFSET+0] = seqNrBytes[3];
        buffer[SEQNR_OFFSET+1] = seqNrBytes[2];
        buffer[SEQNR_OFFSET+2] = seqNrBytes[1];
        buffer[SEQNR_OFFSET+3] = seqNrBytes[0];
    }
    
    public long getSeqNr()
    {
        long seqNr = 0;
        
        seqNr += (buffer[SEQNR_OFFSET+0] & 0xFF) * (256*256*256);
        seqNr += (buffer[SEQNR_OFFSET+1] & 0xFF) * (256*256);
        seqNr += (buffer[SEQNR_OFFSET+2] & 0xFF) * (256);
        seqNr += (buffer[SEQNR_OFFSET+3] & 0xFF);
        
        return seqNr;
    }


    public InetAddress getAddress()
    {
        return udpPacket.getAddress();
    }
    
    public byte[] getData()
    {
        return udpPacket.getData();
    }
    
    public int getLength()
    {
        return udpPacket.getLength();
    }
    
    public int getPort()
    {
        return udpPacket.getPort();
    }
    
    public InetSocketAddress getSocketAddress()
    {
        return (InetSocketAddress) udpPacket.getSocketAddress();
    }
    
    public void setAddress(InetAddress destinationIp)
    {
        udpPacket.setAddress(destinationIp);
    }
    
    public void setPort(int destinationPort)
    {
        udpPacket.setPort(destinationPort);
    }
    
    public void setSocketAddress(InetSocketAddress destinationSocket)
    {
        udpPacket.setSocketAddress(destinationSocket);
    }
	
    /**
	 * Returns the udpPacket.
	 * @return DatagramPacket
	 */
	public DatagramPacket getUdpPacket()
	{
		return udpPacket;
	}

	/**
	 * Sets the udpPacket.
	 * @param udpPacket The udpPacket to set
	 */
	public void setUdpPacket(DatagramPacket udpPacket)
	{
		this.udpPacket = udpPacket;
	}

}
