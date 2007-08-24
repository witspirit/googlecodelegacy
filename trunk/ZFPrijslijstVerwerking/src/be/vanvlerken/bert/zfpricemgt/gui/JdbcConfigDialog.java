/**
 * @author wItspirit
 * 5-mei-2005
 * JdbcConfigDialog.java
 */

package be.vanvlerken.bert.zfpricemgt.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import be.vanvlerken.bert.zfpricemgt.database.JdbcDriverConfig;

/**
 * Dialog for configuration of JDBC settings
 * 
 */
public class JdbcConfigDialog extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private static final ResourceBundle msgs = ResourceBundle.getBundle("be.vanvlerken.bert.zfpricemgt.gui.localization.JdbcConfigDialog");
    
    public static final int     UNKNOWN             = -1;
    public static final int     CANCEL              = 0;
    public static final int     OK                  = 1;
    
    private int                 status;
    private List<JdbcDriverConfig> standardDriverList;
    
    private String              initialDbDriver;
    private String              initialDbUrl;
    private String              initialDbUser;
    private String              initialDbPassword;
    private String              initialPricesTable;

    private String              dbDriver;
    private String              dbUrl;
    private String              dbUser;
    private String              dbPassword;
    private String              pricesTable;
    
    private JComboBox driverSelector;
    private JButton resetButton;
    private JButton okButton;
    private AbstractButton cancelButton;
    private JTextField driverField;
    private JTextField urlField;
    private JTextField userField;
    private JTextField passwordField;
    private JTextField pricesTableField;

    public JdbcConfigDialog(JFrame parent, List<JdbcDriverConfig> standardDriverList)
    {
        super(parent, msgs.getString("title"), true);
        
        this.standardDriverList = standardDriverList;
        status = UNKNOWN;
        
        add(buildLayout());
        
        setSize(400,230);
    }
    
    private JComponent buildLayout()
    {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));        
                
        JPanel settingsPanel = createSettingsPanel();
        JPanel buttonPanel = createButtonPanel();
        
        mainPanel.add(createDriverSelector(), BorderLayout.NORTH);
        mainPanel.add(settingsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        return mainPanel;        
    }

    private Component createDriverSelector() {
    	Object[] descriptions = new Object[standardDriverList.size()];
    	for (int itemNr=0; itemNr < descriptions.length; itemNr++) {
    		descriptions[itemNr] = standardDriverList.get(itemNr).getDescription();
    	}
    	
		driverSelector = new JComboBox(descriptions);
		driverSelector.setEditable(false);
		driverSelector.setSelectedIndex(0);
		driverSelector.addActionListener(this);
		
		setInitialConfig(standardDriverList.get(0));
		
		return driverSelector;
	}
    
    private void setInitialConfig(JdbcDriverConfig initialConfig) {
    	initialDbDriver = dbDriver = initialConfig.getDbDriver();
        initialDbUrl = dbUrl = initialConfig.getDbUrl();
        initialDbUser = dbUser = initialConfig.getDbUser();
        initialDbPassword = dbPassword = initialConfig.getDbPassword();
        initialPricesTable = pricesTable = initialConfig.getPricesTable();
    }

	private JPanel createButtonPanel()
    {
        JPanel buttonPanel = new JPanel(new GridLayout(1,3,10,10));
        
        resetButton = new JButton(msgs.getString("resetButton"));
        resetButton.setMnemonic(msgs.getString("resetMnemonic").charAt(0));
        resetButton.addActionListener(this);
        okButton = new JButton(msgs.getString("okButton"));
        okButton.setMnemonic(msgs.getString("okMnemonic").charAt(0));
        okButton.addActionListener(this);
        cancelButton = new JButton(msgs.getString("cancelButton"));
        cancelButton.setMnemonic(msgs.getString("cancelMnemonic").charAt(0));
        cancelButton.addActionListener(this);
        
        buttonPanel.add(resetButton);
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
               
        return buttonPanel;
    }

    private JPanel createSettingsPanel()
    {
        JPanel settingsPanel = new JPanel(new BorderLayout());
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
        
        JPanel labelPanel = new JPanel(new GridLayout(5,1,0,0));
        JPanel fieldPanel = new JPanel(new GridLayout(5,1,0,0));
        
        JLabel driverLabel = new JLabel(msgs.getString("driverLabel"));
        JLabel urlLabel = new JLabel(msgs.getString("urlLabel"));
        JLabel userLabel = new JLabel(msgs.getString("userLabel"));
        JLabel passwordLabel = new JLabel(msgs.getString("passwordLabel"));
        JLabel pricesTableLabel = new JLabel(msgs.getString("pricesTableLabel"));
        
        labelPanel.add(driverLabel);
        labelPanel.add(urlLabel);
        labelPanel.add(userLabel);
        labelPanel.add(passwordLabel);
        labelPanel.add(pricesTableLabel);
        
        driverField = new JTextField();
        urlField = new JTextField();
        userField = new JTextField();
        passwordField = new JTextField();
        pricesTableField = new JTextField();
        
        fieldPanel.add(driverField);
        fieldPanel.add(urlField);
        fieldPanel.add(userField);
        fieldPanel.add(passwordField);
        fieldPanel.add(pricesTableField);
        
        settingsPanel.add(labelPanel, BorderLayout.WEST);
        settingsPanel.add(fieldPanel, BorderLayout.CENTER);
        
        return settingsPanel;
    }

    public void setInitialDbDriver(String dbDriver)
    {
        initialDbDriver = dbDriver;
    }

    public void setInitialDbUrl(String dbUrl)
    {
        initialDbUrl = dbUrl;
    }

    public void setInitialDbUser(String dbUser)
    {
        initialDbUser = dbUser;
    }

    public void setInitialDbPassword(String dbPassword)
    {
        initialDbPassword = dbPassword;
    }

    public void setInitialPricesTable(String pricesTable)
    {
        initialPricesTable = pricesTable;
    }

    public int showJdbcConfigDialog()
    {
        resetFields();
        setVisible(true);
        return status;
    }

    public String getDbDriver()
    {
        return dbDriver;
    }

    public String getDbUrl()
    {
        return dbUrl;
    }

    public String getDbUser()
    {
        return dbUser;
    }

    public String getDbPassword()
    {
        return dbPassword;
    }

    public String getPricesTable()
    {
        return pricesTable;
    }
    
    public void setSize(int width, int height)
    {
        GraphicsEnvironment gE = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle screenRect = gE.getMaximumWindowBounds();
        int xLoc = (screenRect.width - width) / 2;
        int yLoc = (screenRect.height - height) / 2;
        this.setLocation(new Point(xLoc, yLoc));
        super.setSize(width, height);
    }

    public void actionPerformed(ActionEvent ae)
    {
    	if ( ae.getSource() == driverSelector ) {
    		setInitialConfig(standardDriverList.get(driverSelector.getSelectedIndex()));
    		resetFields();
    	}
    	else if ( ae.getSource() == okButton )
        {
            if ( validateFields() )
            {
                status = OK;
                dispose();
            }
        }
        else if ( ae.getSource() == cancelButton )
        {
            status = CANCEL;
            dispose();
        }
        else if ( ae.getSource() == resetButton )
        {
            resetFields();
        }
    }

    private void resetFields()
    {
        driverField.setText(initialDbDriver);
        urlField.setText(initialDbUrl);
        userField.setText(initialDbUser);
        passwordField.setText(initialDbPassword);
        pricesTableField.setText(initialPricesTable);
    }

    private boolean validateFields()
    {
        dbDriver = driverField.getText();
        dbUrl = urlField.getText();
        dbUser = userField.getText();
        dbPassword = passwordField.getText();
        pricesTable = pricesTableField.getText();
        
        return true;
    }
}
