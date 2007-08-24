/**
 * @author wItspirit
 * 9-feb-2003
 * LogServerDialog.java
 */
package be.vanvlerken.bert.logmonitor.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * A dialog for entering logging information
 */
public class LoggingDialog extends JDialog implements ActionListener
{
    public static final int VALUES_SET = 0;
    public static final int CANCEL = 1;
    public static final int CLOSED = 2;
    
    private JButton setButton;
    private JButton resetButton;
    private JButton cancelButton;
    
    private JTextField ipField;
    private JTextField portField;
    
    private String initialIp;
    private int initialPort;
    
    private String enteredIp;
    private int enteredPort;
    
    private int action;
    
    public LoggingDialog(JFrame parent)
    {
        super(parent, "Logging parameters", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        
        int dialogWidth = 300;
        int dialogHeight = 180;
        
        int xLoc = 0;
        int yLoc = 0;
        
        GraphicsEnvironment gE = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle screenRect =  gE.getMaximumWindowBounds();
        
        xLoc = (screenRect.width-dialogWidth)/2;
        yLoc = (screenRect.height-dialogHeight)/2;
        
        setLocation(new Point(xLoc, yLoc));
        
        setSize(new Dimension(dialogWidth,dialogHeight));
        
        initialIp = null;
        initialPort = 0;
        
        enteredIp = null;
        enteredPort = 0;
        
        action = CLOSED;
        
        /* Graphical layouting */
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel parametersPanel = new JPanel(new GridLayout(1,2));
        JPanel buttonPanel = new JPanel(new GridLayout(1,3,10,10));
        JPanel introPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        parametersPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        /* Intro message */
        JLabel introMessage = new JLabel("Please enter the parameters for the MixedModeServer");
        
        introPanel.add(introMessage);
        
        /* Buttons */
        setButton = new JButton("Set");
        resetButton = new JButton("Reset");
        cancelButton = new JButton("Cancel");
        
        setButton.addActionListener(this);
        resetButton.addActionListener(this);
        cancelButton.addActionListener(this);
        
        buttonPanel.add(setButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(cancelButton);
        
        /* Parameters */
        JPanel namesPanel = new JPanel(new GridLayout(2,1));
        JPanel valuesPanel = new JPanel(new GridLayout(2,1));
        
        parametersPanel.add(namesPanel);
        parametersPanel.add(valuesPanel);
        
        JLabel ip = new JLabel("IP address");
        JLabel port = new JLabel("Port");
        
        ipField = new JTextField();     
        portField = new JTextField();
        
        namesPanel.add(ip);
        namesPanel.add(port);
        
        valuesPanel.add(ipField);
        valuesPanel.add(portField);

        /* Adding all the panels */        
        mainPanel.add(introPanel, BorderLayout.NORTH);
        mainPanel.add(parametersPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
                
        getContentPane().add(mainPanel, BorderLayout.CENTER);                                  
    }
	
    /**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae)
	{
        Object button = ae.getSource();
        
        if ( button == setButton )
        {
            enteredIp = ipField.getText();
            enteredPort = Integer.parseInt(portField.getText());
            action = VALUES_SET;
            dispose(); 
        }
        else if ( button == resetButton )
        {
            setInitialIp(initialIp);
            setInitialPort(initialPort);   
        }
        else if ( button == cancelButton )
        {
            action = CANCEL;
            dispose();
        }
	}
	
    public int showDialog()
    {
        show();
        return action;
    }
    
    
    /**
	 * Sets the initialIp.
	 * @param initialIp The initialIp to set
	 */
	public void setInitialIp(String initialIp)
	{
		this.initialIp = initialIp;
        ipField.setText(initialIp);
	}

	/**
	 * Sets the initialPort.
	 * @param initialPort The initialPort to set
	 */
	public void setInitialPort(int initialPort)
	{
		this.initialPort = initialPort;
        portField.setText(new Integer(initialPort).toString());
	}

	/**
	 * Returns the enteredIp.
	 * @return String
	 */
	public String getEnteredIp()
	{
		return enteredIp;
	}

	/**
	 * Returns the enteredPort.
	 * @return int
	 */
	public int getEnteredPort()
	{
		return enteredPort;
	}

}
