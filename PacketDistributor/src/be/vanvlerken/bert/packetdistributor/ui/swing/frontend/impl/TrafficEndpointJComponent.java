/**
 * @author wItspirit
 * 28-apr-2003
 * TrafficEndpointJComponent.java
 */
package be.vanvlerken.bert.packetdistributor.ui.swing.frontend.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Provides an input component for a traffic endpoint
 * A traffic endpoint is characterized by 3 elements:
 * - IP address
 * - Port number
 * - Protocol identification
 */
public class TrafficEndpointJComponent extends JPanel
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3616450090013831225L;
    public static final int     UDP = 0;
    public static final int     TCP = 1;
    public static final int     UNKNOWN = 0xFF;
    
    private JTextField  ipField;
    private JComboBox   protocolField;
    private JTextField  portField;
    
    public TrafficEndpointJComponent()
    {
        setupComponent();
    }

    /**
     * Method setupComponent.
     */
    private void setupComponent()
    {
        String[] options = { "UDP", "TCP" };
        ipField = new JTextField(15);
        protocolField = new JComboBox(options);
        portField = new JTextField(5);
        
        add(ipField);
        add(protocolField);
        add(portField);
    }
    
    public InetAddress getIpAddress() throws UnknownHostException
    {
        return InetAddress.getByName(ipField.getText());		    		
    }
    
    public void setIpAddress(InetAddress ipAddress)
    {
        ipField.setText(ipAddress.getHostAddress());
    }
    
    public int getProtocol()
    {
        return protocolField.getSelectedIndex();
    }
    
    public void setProtocol(int protocol)
    {
        protocolField.setSelectedIndex(protocol);
    }
    
    public int getPort() throws NumberFormatException
    {        
        int port = Integer.parseInt(portField.getText());
        if ( port < 0 || port > 65535)
        {
            throw new NumberFormatException("Port number not within 0-65535 range");
        }
        return port;
    }
    
    public void setPort(int port)
    {
        portField.setText(Integer.toString(port));
    }
    
    public void clearFields()
    {
        ipField.setText("");
        portField.setText("");
        protocolField.setSelectedIndex(UDP);        
    }
}
