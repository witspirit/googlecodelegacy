/**
 * @author wItspirit
 * 3-feb-2005
 * ImportDialog.java
 */

package be.vanvlerken.bert.zfpricemgt.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 * This is a simple dialog offering the user the opportunity to set
 * the import properties
 */
public class ImportDialog extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private static final ResourceBundle msgs = ResourceBundle.getBundle("be.vanvlerken.bert.zfpricemgt.gui.localization.ImportDialog");
    
    public static final int UNKNOWN = -1;
    public static final int IMPORT = 0;
    public static final int CANCEL = 1;
    
    private JFileChooser fileChooser;
    private JButton importButton;
    private JButton cancelButton;
    
    
    private int status;
    private File selectedFile;
    private boolean overruleDate;
    private Date validSince;
    private JTextField validSinceField;
    private DateFormat df;
    private JCheckBox overruleCheck;
    private JTextField fileField;
    private JButton browseButton;
    
    
    public ImportDialog(JFrame parent)
    {
        super(parent, msgs.getString("title"), true);
        
        df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRANCE);
        
        status = UNKNOWN;
        selectedFile = null;
        overruleDate = false;
        validSince = new Date();
        
        add(buildLayout());
        
        setSize(340, 170);
    } 
    
    
    /**
     * 
     */
    private JComponent buildLayout()
    {
        JPanel rootPanel = new JPanel(new GridLayout(4,1,10,10));
        rootPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        // 1st row: ValidSince field
        JPanel validSincePanel = getValidSincePanel();
        
        // 2nd row: Checkbox overrule
        JPanel overrulePanel = getOverrulePanel();
        
        // 3rd row: File selection
        JPanel fileSelectPanel = getFileSelectPanel();
        
        // 4th row: Action Buttons
        JPanel buttonPanel = getButtonPanel();
        
        rootPanel.add(validSincePanel);
        rootPanel.add(overrulePanel);
        rootPanel.add(fileSelectPanel);
        rootPanel.add(buttonPanel);
        return rootPanel;
    }

    private JPanel getValidSincePanel()
    {
        JPanel validSincePanel = new JPanel(new BorderLayout());
        
        JLabel validSinceLabel = new JLabel(msgs.getString("valid.since.label"));
        validSinceField = new JTextField();
        validSinceField.setText(df.format(validSince));
        
        validSincePanel.add(validSinceLabel, BorderLayout.WEST);
        validSincePanel.add(validSinceField, BorderLayout.CENTER);
                
        return validSincePanel; 
    }

    private JPanel getOverrulePanel()
    {
        JPanel overrulePanel = new JPanel(new BorderLayout());
        
        overruleCheck = new JCheckBox(msgs.getString("ignore.valid.since.in.source"));
        
        overrulePanel.add(overruleCheck);
        
        return overrulePanel; 
    }

    private JPanel getFileSelectPanel()
    {
        JPanel fileSelectPanel = new JPanel(new BorderLayout());
        
        JLabel fileLabel = new JLabel(msgs.getString("file.label"));
        fileField = new JTextField();
        browseButton = new JButton(msgs.getString("select.button"));
        browseButton.setMnemonic(msgs.getString("select.button.mnemonic").charAt(0));
        browseButton.addActionListener(this);
        
        fileSelectPanel.add(fileLabel, BorderLayout.WEST);
        fileSelectPanel.add(fileField, BorderLayout.CENTER);
        fileSelectPanel.add(browseButton, BorderLayout.EAST);
        
        return fileSelectPanel;
    }
    
    /**
     * @return
     */
    private JPanel getButtonPanel()
    {        
        JPanel buttonPanel = new JPanel(new GridLayout(1,2,10,10));
        
        importButton = new JButton(msgs.getString("import.button"));
        importButton.setMnemonic(msgs.getString("import.button.mnemonic").charAt(0));
        importButton.addActionListener(this);
        
        cancelButton = new JButton(msgs.getString("cancel.button"));
        cancelButton.setMnemonic(msgs.getString("cancel.button.mnemonic").charAt(0));
        cancelButton.addActionListener(this);
        
        buttonPanel.add(importButton);
        buttonPanel.add(cancelButton);
        
        return buttonPanel;
    }


    /**
     * @return
     */
    private File getFile()
    {
        File file = null;
        if (fileChooser == null)
        {
            fileChooser = new JFileChooser();
        }
        int action = fileChooser.showDialog(this, msgs.getString("file.chooser.title"));
        if (action == JFileChooser.APPROVE_OPTION)
        {
            file = fileChooser.getSelectedFile();
        }
        return file;
    }


    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae)
    {
        if ( ae.getSource() == importButton )
        {
            status = validateFields();
            if ( status == IMPORT )
            {
                this.dispose();
            }
        }
        else if ( ae.getSource()  == cancelButton )
        {
            status = CANCEL;
            this.dispose();
        }
        else if ( ae.getSource() == browseButton )
        {
            File file = getFile();
            if ( file != null )
            {
                fileField.setText(file.toString());
            }
        }
    }
    
    public int showImportDialog()
    {
        this.setVisible(true);
        return status;
    }
    
    /**
     * 
     */
    private int validateFields()
    {
        try
        {
            validSince = df.parse(validSinceField.getText());
        }
        catch (ParseException e)
        {
            JOptionPane.showMessageDialog(this, msgs.getString("date.format.invalid"), null, JOptionPane.ERROR_MESSAGE);
            return UNKNOWN;
        }
        overruleDate = overruleCheck.isSelected();
        String filePath = fileField.getText();
        if ( filePath.equals("") )
        {
            JOptionPane.showMessageDialog(this, msgs.getString("please.select.file"), null, JOptionPane.ERROR_MESSAGE);
            return UNKNOWN;
        }
        File file = new File(filePath);
        if ( !file.canRead() )
        {
            JOptionPane.showMessageDialog(this,MessageFormat.format(msgs.getString("cannot.read.file"), file), null, JOptionPane.ERROR_MESSAGE);
            return UNKNOWN;
        }
        selectedFile = file;
        return IMPORT;
    }


    public boolean overruleDate()
    {
        return overruleDate;
    }
    public File getSelectedFile()
    {
        return selectedFile;
    }
    public int getStatus()
    {
        return status;
    }
    public Date getValidSince()
    {
        return validSince;
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

    public void setSize(Dimension newSize)
    {
        setSize(newSize.width, newSize.height);
    }
}
