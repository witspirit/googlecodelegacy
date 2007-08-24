/**
 * @author wItspirit
 * 20-apr-2003
 * ConfigDialog.java
 */

package be.vanvlerken.bert.packetdistributor.ui.swing.configuration;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Displays a dialog for configuring the PacketDistributorGui
 */
public class ConfigDialog extends JDialog implements ActionListener, ChangeListener
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3978143249032427058L;
    public static final int   VALUES_SET       = 0;
    public static final int   CANCEL           = 1;
    public static final int   CLOSED           = 2;

    private static final int  height           = 300;
    private static final int  width            = 300;

    private JButton           setButton;
    private JButton           resetButton;
    private JButton           cancelButton;

    private int               action;
    private boolean           init;

    private String            initialConfigFile;
    private JTextField        configFileField;
    private String            enteredConfigFile;

    private String            initialRmiUrl;
    private JTextField        rmiUrlField;
    private String            enteredRmiUrl;

    private int               initialInterface;
    private int               enteredInterface;
    private JRadioButton      localInterfaceButton;
    private JRadioButton      remoteInterfaceButton;

    public ConfigDialog(JFrame parent)
    {
        super(parent, "Configuration", true);
        init = false;

        initialInterface = GuiConfig.LOCAL_INTERFACE;
        enteredInterface = GuiConfig.LOCAL_INTERFACE;

        initialConfigFile = "";
        enteredConfigFile = null;

        initialRmiUrl = "";
        enteredRmiUrl = null;
    }

    private void init()
    {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        action = CLOSED;

        generateLayout();
    }

    private void calculateSize()
    {
        int dialogWidth = width;
        int dialogHeight = height;

        // int dialogWidth = this.getPreferredSize().width;
        // int dialogHeight = this.getPreferredSize().height;

        int xLoc = 0;
        int yLoc = 0;

        GraphicsEnvironment gE = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle screenRect = gE.getMaximumWindowBounds();

        xLoc = (screenRect.width - dialogWidth) / 2;
        yLoc = (screenRect.height - dialogHeight) / 2;

        setLocation(new Point(xLoc, yLoc));

        setSize(new Dimension(dialogWidth, dialogHeight));
    }

    private void generateLayout()
    {
        /* Graphical layouting */
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        /* Intro message */
        String[] introMessage = getIntroMessage();
        JPanel introPanel = new JPanel(new GridLayout(introMessage.length, 1));
        for (String introLine : introMessage)
        {
            JLabel messageLine = new JLabel(introLine);
            introPanel.add(messageLine);
        }

        /* Buttons */
        setButton = new JButton("Set");
        resetButton = new JButton("Reset");
        cancelButton = new JButton("Cancel");

        setButton.addActionListener(this);
        resetButton.addActionListener(this);
        cancelButton.addActionListener(this);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        buttonPanel.add(setButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(cancelButton);

        /* Parameters */
        JPanel parametersPanel = new JPanel(new BorderLayout());
        parametersPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        /* The interface panel */
        JPanel interfacePanel = new JPanel(new GridLayout(2, 1));
        Border etchedBorder = BorderFactory.createEtchedBorder();
        Border interfaceBorder = BorderFactory.createTitledBorder(etchedBorder, "Interface selector");
        interfacePanel.setBorder(interfaceBorder);

        localInterfaceButton = new JRadioButton("Local interface");
        remoteInterfaceButton = new JRadioButton("Remote interface");

        ButtonGroup interfaceSelector = new ButtonGroup();
        interfaceSelector.add(localInterfaceButton);
        interfaceSelector.add(remoteInterfaceButton);

        interfacePanel.add(localInterfaceButton);
        interfacePanel.add(remoteInterfaceButton);

        /* The nameValue panel */
        JPanel nameValuePanel = new JPanel(new GridLayout(1, 2));
        Border nameValueBorder = BorderFactory.createTitledBorder(etchedBorder, "Interface config");
        nameValuePanel.setBorder(nameValueBorder);

        int nrOfEntries = 2;
        JPanel namesPanel = new JPanel(new GridLayout(nrOfEntries, 1));
        JPanel valuesPanel = new JPanel(new GridLayout(nrOfEntries, 1));

        nameValuePanel.add(namesPanel);
        nameValuePanel.add(valuesPanel);

        // Adding the entries to the parameters panels
        namesPanel.add(new JLabel("Config File"));
        namesPanel.add(new JLabel("RMI Url"));

        configFileField = new JTextField();
        rmiUrlField = new JTextField();

        configFileField.setEnabled(true);
        rmiUrlField.setEnabled(false);

        valuesPanel.add(configFileField);
        valuesPanel.add(rmiUrlField);

        // Some extra inits
        localInterfaceButton.addChangeListener(this);
        remoteInterfaceButton.addChangeListener(this);

        switch (initialInterface)
        {
        case GuiConfig.LOCAL_INTERFACE:
            localInterfaceButton.setSelected(true);
            break;
        case GuiConfig.RMI_INTERFACE:
            remoteInterfaceButton.setSelected(true);
            break;
        default:
            localInterfaceButton.setSelected(true);
        }

        // Adding the sub-panels to the parameters panel
        parametersPanel.add(interfacePanel, BorderLayout.CENTER);
        parametersPanel.add(nameValuePanel, BorderLayout.SOUTH);

        /* Adding all the panels */
        mainPanel.add(introPanel, BorderLayout.NORTH);
        mainPanel.add(parametersPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel, BorderLayout.CENTER);

        calculateSize();
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
     */
    public void actionPerformed(ActionEvent ae)
    {
        Object button = ae.getSource();

        if (button == setButton)
        {
            setValues();
            action = VALUES_SET;
            dispose();
        }
        else if (button == resetButton)
        {
            resetValues();
        }
        else if (button == cancelButton)
        {
            action = CANCEL;
            dispose();
        }
    }

    /**
     * Opens up the dialog. It will only return when the dialog has been exited
     * by the user
     */
    public int showDialog()
    {
        if (!init)
        {
            init();
        }
        resetValues();
        setVisible(true);
        return action;
    }

    /**
     * @see be.vanvlerken.bert.components.gui.extendeddialog.ExtendedDialog#getIntroMessage()
     */
    protected String[] getIntroMessage()
    {
        String[] introMessage = { "Provide the necessary parameters to be able ", "to contact a specific instance of the ",
                "PacketDistributorServer application."};
        return introMessage;
    }

    /**
     * @see be.vanvlerken.bert.components.gui.extendeddialog.ExtendedDialog#setValues()
     */
    protected void setValues()
    {
        enteredConfigFile = configFileField.getText();
        enteredRmiUrl = rmiUrlField.getText();
    }

    /**
     * @see be.vanvlerken.bert.components.gui.extendeddialog.ExtendedDialog#resetValues()
     */
    protected void resetValues()
    {
        configFileField.setText(initialConfigFile);
        rmiUrlField.setText(initialRmiUrl);
    }

    /**
     * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    public void stateChanged(ChangeEvent ce)
    {
        if (remoteInterfaceButton.isSelected())
        {
            enteredInterface = GuiConfig.RMI_INTERFACE;
            configFileField.setEnabled(false);
            rmiUrlField.setEnabled(true);
        }
        else
        {
            configFileField.setEnabled(true);
            rmiUrlField.setEnabled(false);
            enteredInterface = GuiConfig.LOCAL_INTERFACE;
        }
    }

    /**
     * @return
     */
    public int getEnteredInterface()
    {
        return enteredInterface;
    }

    /**
     * @param i
     */
    public void setInitialInterface(int i)
    {
        initialInterface = i;
    }

    /**
     * @return Returns the enteredConfigFile.
     */
    public String getEnteredConfigFile()
    {
        return enteredConfigFile;
    }

    /**
     * @return Returns the enteredRmiUrl.
     */
    public String getEnteredRmiUrl()
    {
        return enteredRmiUrl;
    }

    /**
     * @param initialConfigFile
     *            The initialConfigFile to set.
     */
    public void setInitialConfigFile(String initialConfigFile)
    {
        this.initialConfigFile = initialConfigFile;
    }

    /**
     * @param initialRmiUrl
     *            The initialRmiUrl to set.
     */
    public void setInitialRmiUrl(String initialRmiUrl)
    {
        this.initialRmiUrl = initialRmiUrl;
    }
}
