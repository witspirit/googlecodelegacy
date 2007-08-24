/**
 * @author wItspirit
 * 9-feb-2003
 * LogServerDialog.java
 */
package be.vanvlerken.bert.logmonitor.actions;

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
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * A dialog for entering logging information
 */
public class LogServerDialog extends JDialog implements ActionListener
{
    public static final int VALUES_SET = 0;
    public static final int CANCEL = 1;
    public static final int CLOSED = 2;

    private JButton setButton;
    private JButton resetButton;
    private JButton cancelButton;

    private JTextField ipField;
    private JTextField portField;
    private JComboBox coderSelector;
    private JComboBox typeSelector;

    private String initialIp;
    private int initialPort;
    private int initialCoder;
    private int initialType;

    private String enteredIp;
    private int enteredPort;
    private int enteredCoder;
    private int enteredType;

    private int action;

    public LogServerDialog(JFrame parent, int initialType, String[] serverTypes, String initialIp, int initialPort, int initialCoder, String[] coders)
    {
        super(parent, "LogServer settings", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        int dialogWidth = 450;
        int dialogHeight = 300;

        int xLoc = 0;
        int yLoc = 0;

        GraphicsEnvironment gE = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle screenRect = gE.getMaximumWindowBounds();

        xLoc = (screenRect.width - dialogWidth) / 2;
        yLoc = (screenRect.height - dialogHeight) / 2;

        setLocation(new Point(xLoc, yLoc));

        setSize(new Dimension(dialogWidth, dialogHeight));

        this.initialIp = initialIp;
        this.initialPort = initialPort;
        this.initialCoder = initialCoder;
        this.initialType = initialType;

        enteredIp = initialIp;
        enteredPort = initialPort;
        enteredCoder = initialCoder;
        enteredType = initialType;

        action = CLOSED;

        /* Graphical layouting */
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel settingsPanel = new JPanel(new BorderLayout());
        JPanel socketParamsPanel = new JPanel(new GridLayout(1, 2));
        JPanel coderPanel = new JPanel(new BorderLayout());
        JPanel typePanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        JPanel introPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        /* Intro message */
        JLabel introMessage = new JLabel("Please enter the parameters for the LogServer");

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
        JPanel namesPanel = new JPanel(new GridLayout(2, 1));
        JPanel valuesPanel = new JPanel(new GridLayout(2, 1));

        socketParamsPanel.add(namesPanel);
        socketParamsPanel.add(valuesPanel);
        socketParamsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel ip = new JLabel("IP address");
        JLabel port = new JLabel("Port");

        ipField = new JTextField();
        ipField.setText(initialIp);
        portField = new JTextField();
        portField.setText(new Integer(initialPort).toString());

        namesPanel.add(ip);
        namesPanel.add(port);

        valuesPanel.add(ipField);
        valuesPanel.add(portField);

        /* The type selector */
        JLabel typeSelect = new JLabel("Select LogServer type:");
        
        typeSelector = new JComboBox(serverTypes);
        typeSelector.setSelectedIndex(initialType);
        
        typePanel.add(typeSelect, BorderLayout.NORTH);
        typePanel.add(typeSelector, BorderLayout.CENTER);
        typePanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        
        /* The coder selector */
        JLabel coderSelect = new JLabel("Select Decoder:");
        
        coderSelector = new JComboBox(coders);
        coderSelector.setSelectedIndex(initialCoder);
        
        coderPanel.add(coderSelect, BorderLayout.NORTH);
        coderPanel.add(coderSelector, BorderLayout.CENTER);
        coderPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        
        /* The settingspanel */
        settingsPanel.add(socketParamsPanel, BorderLayout.NORTH);
        
        JPanel comboPanel = new JPanel(new GridLayout(2,1));
        comboPanel.add(typePanel);
        comboPanel.add(coderPanel);
        
        settingsPanel.add(comboPanel, BorderLayout.CENTER);

        /* Adding all the panels */
        mainPanel.add(introPanel, BorderLayout.NORTH);
        mainPanel.add(settingsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae)
    {
        Object button = ae.getSource();

        if (button == setButton)
        {
            enteredIp = ipField.getText();
            enteredPort = Integer.parseInt(portField.getText());
            enteredType = typeSelector.getSelectedIndex();
            enteredCoder = coderSelector.getSelectedIndex();
            action = VALUES_SET;
            dispose();
        }
        else if (button == resetButton)
        {
            ipField.setText(initialIp);
            portField.setText(new Integer(initialPort).toString());
            typeSelector.setSelectedIndex(initialType);
            coderSelector.setSelectedIndex(initialCoder);            
        }
        else if (button == cancelButton)
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

    /**
     * Returns the enteredCoder
     * @return int
     */
    public int getEnteredCoder()
    {
        return enteredCoder;
    }
    
    /**
     * @return Returns the enteredType.
     */
    public int getEnteredType()
    {
        return enteredType;
    }
}
