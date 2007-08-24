/**
 * @author wItspirit
 * 24-apr-2005
 * ThresholdConfigDialog.java
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
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Shows a Dialog to configure the highlighting of old prices
 */
public class ThresholdConfigDialog extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private static final ResourceBundle msgs = ResourceBundle.getBundle("be.vanvlerken.bert.zfpricemgt.gui.localization.ThresholdConfigDialog");
        
    public static final int UNKNOWN = -1;
    public static final int OK = 0;
    public static final int CANCEL = 1;
    
    private DateFormat df;
    
    private int status;
    
    private JButton okButton;
    private JButton cancelButton;
    private JCheckBox displayCheck;
    private JTextField thresholdDateField;
    
    private Date originalDate;
    private boolean originalDisplay;
    private Date selectedDate;
    private boolean selectedDisplay;

    public ThresholdConfigDialog(JFrame parent)
    {
        super(parent, msgs.getString("title"), true);
        
        df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRANCE);
        
        status = UNKNOWN;
        originalDate = new Date();
        originalDisplay = false;
        
        selectedDate = originalDate;
        selectedDisplay = originalDisplay;
        
        add(buildLayout());
        
        setSize(300,140);
    }
    
    private JComponent buildLayout()
    {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        JPanel optionPanel = createOptionPanel();
        JPanel buttonPanel = createButtonPanel();
        
        mainPanel.add(optionPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        return mainPanel;
    }

    private JPanel createOptionPanel()
    {
        JPanel optionPanel = new JPanel(new GridLayout(2,1,0,0));
        optionPanel.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
        displayCheck = new JCheckBox(msgs.getString("highlightOldPrices"), true);
        displayCheck.addActionListener(this);
        thresholdDateField = new JTextField();
        JLabel thresholdDateLabel = new JLabel(msgs.getString("thresholdDate"));
        
        JPanel datePanel = new JPanel(new BorderLayout());
        datePanel.add(thresholdDateLabel, BorderLayout.WEST);
        datePanel.add(thresholdDateField, BorderLayout.CENTER);
        
        optionPanel.add(displayCheck);
        optionPanel.add(datePanel);
        return optionPanel;
    }

    /**
     * @return
     */
    private JPanel createButtonPanel()
    {
        JPanel buttonPanel = new JPanel(new GridLayout(1,2,10,10));
        okButton = new JButton(msgs.getString("okButton"));
        okButton.setMnemonic(msgs.getString("okMnemonic").charAt(0));
        okButton.addActionListener(this);
        cancelButton = new JButton(msgs.getString("cancelButton"));
        cancelButton.setMnemonic(msgs.getString("cancelMnemonic").charAt(0));
        cancelButton.addActionListener(this);
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        return buttonPanel;
    }
    
    public int showThresholdConfigDialog()
    {
        displayCheck.setSelected(originalDisplay);
        thresholdDateField.setEnabled(originalDisplay);
        thresholdDateField.setText(df.format(originalDate));
        this.setVisible(true);
        return status;
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

    public void actionPerformed(ActionEvent ae)
    {
        if ( ae.getSource() == displayCheck )
        {
            thresholdDateField.setEnabled(displayCheck.isSelected());
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
    }

    private boolean validateFields()
    {
        boolean allOk = true;
        selectedDisplay = displayCheck.isSelected();
        try
        {
            selectedDate = df.parse(thresholdDateField.getText());
        }
        catch (ParseException e)
        {
            JOptionPane.showMessageDialog(this, msgs.getString("couldNotParseDate"), msgs.getString("invalidDateTitle"), JOptionPane.ERROR_MESSAGE);
            allOk = false;
        }
        return allOk;
    }

    public Date getSelectedDate()
    {
        return selectedDate;
    }
    

    public boolean isSelectedDisplay()
    {
        return selectedDisplay;
    }
    

    public void setOriginalDate(Date originalDate)
    {
        this.originalDate = originalDate;
    }
    

    public void setOriginalDisplay(boolean originalDisplay)
    {
        this.originalDisplay = originalDisplay;
    }
    
}
