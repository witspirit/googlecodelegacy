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
import java.io.IOException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import be.vanvlerken.bert.zfpricemgt.database.exporters.Exporter;


/**
 * This is a simple dialog offering the user the opportunity to set
 * the import properties
 */
public class ExportDialog extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private static final ResourceBundle msgs = ResourceBundle.getBundle("be.vanvlerken.bert.zfpricemgt.gui.localization.ExportDialog");
    
    public static final int UNKNOWN = -1;
    public static final int EXPORT = 0;
    public static final int CANCEL = 1;
    
    private JFileChooser fileChooser;
    private JButton exportButton;
    private JButton cancelButton;
    
    
    private int status;
    private File outputFile;
    private boolean completeDatabase;
    private Date validSince;
    private JTextField validSinceField;
    private DateFormat df;
    private JCheckBox completeDatabaseCheck;
    private JTextField fileField;
    private JButton browseButton;
    private Exporter[] exporters;
    private JComboBox exporterList;
    private Exporter exporter;
    
    
    public ExportDialog(JFrame parent, Exporter[] exporters)
    {
        super(parent, msgs.getString("title"), true);
        this.exporters = exporters;
        
        df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRANCE);
        
        status = UNKNOWN;
        outputFile = null;
        completeDatabase = true;
        validSince = new Date();
        exporter = null;
        
        add(buildLayout());
        
        setSize(340, 200);
    } 
    
    
    /**
     * 
     */
    private JComponent buildLayout()
    {
        JPanel rootPanel = new JPanel(new GridLayout(5,1,10,10));
        rootPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        // 1st row: Checkbox Full Database
        JPanel completeDatabasePanel = getCompleteDatabasePanel();
        
        // 2nd row: ValidSince field
        JPanel validSincePanel = getValidSincePanel();
        
        // 3rd row: File selection
        JPanel fileSelectPanel = getFileSelectPanel();
        
        // 4th row: Exporter type selection
        JPanel exporterSelectPanel = getExporterSelectPanel();
        
        // 5th row: Action Buttons
        JPanel buttonPanel = getButtonPanel();
        
        rootPanel.add(completeDatabasePanel);
        rootPanel.add(validSincePanel);        
        rootPanel.add(fileSelectPanel);
        rootPanel.add(exporterSelectPanel);
        rootPanel.add(buttonPanel);
        return rootPanel;
    }

    private JPanel getExporterSelectPanel()
    {
        JPanel exporterSelectPanel = new JPanel(new BorderLayout());
        
        exporterList = new JComboBox(exporters);
        
        exporterSelectPanel.add(exporterList, BorderLayout.CENTER);
        return exporterSelectPanel;
    }


    private JPanel getValidSincePanel()
    {
        JPanel validSincePanel = new JPanel(new BorderLayout());
        
        JLabel validSinceLabel = new JLabel(msgs.getString("valid.since.label"));
        validSinceField = new JTextField();
        validSinceField.setText(df.format(validSince));
        validSinceField.setEnabled(!completeDatabase);
        
        validSincePanel.add(validSinceLabel, BorderLayout.WEST);
        validSincePanel.add(validSinceField, BorderLayout.CENTER);
                
        return validSincePanel; 
    }

    private JPanel getCompleteDatabasePanel()
    {
        JPanel overrulePanel = new JPanel(new BorderLayout());
        
        completeDatabaseCheck = new JCheckBox(msgs.getString("export.complete.database"));
        completeDatabaseCheck.setSelected(completeDatabase);
        completeDatabaseCheck.addActionListener(this);
        
        overrulePanel.add(completeDatabaseCheck);
        
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
        
        exportButton = new JButton(msgs.getString("export.button"));
        exportButton.setMnemonic(msgs.getString("export.button.mnemonic").charAt(0));
        exportButton.addActionListener(this);
        
        cancelButton = new JButton(msgs.getString("cancel.button"));
        cancelButton.setMnemonic(msgs.getString("cancel.button.mnemonic").charAt(0));
        cancelButton.addActionListener(this);
        
        buttonPanel.add(exportButton);
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
        if ( ae.getSource() == exportButton )
        {
            status = validateFields();
            if ( status == EXPORT )
            {
                this.dispose();
            }
        }
        else if ( ae.getSource() == browseButton )
        {
            File file = getFile();
            if ( file != null )
            {
                fileField.setText(file.toString());
            }
        }
        else if ( ae.getSource() == completeDatabaseCheck )
        {
            validSinceField.setEnabled(!completeDatabaseCheck.isSelected());
        }
        else if ( ae.getSource()  == cancelButton )
        {
            status = CANCEL;
            this.dispose();
        }

    }
    
    public int showExportDialog()
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
        completeDatabase = completeDatabaseCheck.isSelected();
        String filePath = fileField.getText();
        if ( filePath.equals("") )
        {
            JOptionPane.showMessageDialog(this, msgs.getString("please.select.file"), null, JOptionPane.ERROR_MESSAGE);
            return UNKNOWN;
        }
        File file = new File(filePath);
        if ( file.exists() )
        {
            // Ask if they want to overwrite
            int confirm = JOptionPane.showConfirmDialog(this, MessageFormat.format(msgs.getString("perform.overwrite"), file), msgs.getString("overwrite"), JOptionPane.YES_NO_OPTION);
            if ( confirm != JOptionPane.YES_OPTION )
                return UNKNOWN;
        }
        else 
        {
            try
            {
                if ( !file.createNewFile() )
                {
                    throw new IOException(MessageFormat.format(msgs.getString("cannot.create.file"), file));
                }
            }
            catch (IOException e)
            {
                JOptionPane.showMessageDialog(this, msgs.getString("io.problem")+" : "+e.getLocalizedMessage(), null, JOptionPane.ERROR_MESSAGE);
                return UNKNOWN;
            }
        }
        if ( !file.canWrite() )
        {
            JOptionPane.showMessageDialog(this,MessageFormat.format(msgs.getString("cannot.write.file"), file), null, JOptionPane.ERROR_MESSAGE);
            return UNKNOWN;
        }
        outputFile = file;
        exporter = (Exporter) exporterList.getSelectedItem();
        return EXPORT;
    }


    public boolean exportCompleteDatabase()
    {
        return completeDatabase;
    }
    public File getOutputFile()
    {
        return outputFile;
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


    public Exporter getExporter()
    {
        return exporter;
    }
    
}
