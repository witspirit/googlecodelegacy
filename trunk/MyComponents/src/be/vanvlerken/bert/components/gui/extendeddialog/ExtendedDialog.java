/**
 * @author wItspirit
 * 20-apr-2003
 * ExtendedDialog.java
 */

package be.vanvlerken.bert.components.gui.extendeddialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
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

/**
 * Dialog that extends the JDialog with some additional build in behavior
 * Use this dialog when you want a dialog box that provides a column with 'names' and a second column with
 * field to fill in the 'values'.
 * This class is a template. Just a few methods have to be overridden to generate a 'default' looking dialog
 * window.
 */
public abstract class ExtendedDialog extends JDialog implements ActionListener
{
    public static final int VALUES_SET = 0;
    public static final int CANCEL = 1;
    public static final int CLOSED = 2;
    
    private JButton setButton;
    private JButton resetButton;
    private JButton cancelButton;
    
    private int action;
    private boolean init;
    
    protected ExtendedDialog(JFrame parent, String title)
    {
        super(parent, title, true);
        init = false;
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
        int dialogWidth = getDialogWidth();
        int dialogHeight = getDialogHeight();
        
        // int dialogWidth = this.getPreferredSize().width;
        // int dialogHeight = this.getPreferredSize().height;
        
        int xLoc = 0;
        int yLoc = 0;
        
        GraphicsEnvironment gE = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle screenRect =  gE.getMaximumWindowBounds();
        
        xLoc = (screenRect.width-dialogWidth)/2;
        yLoc = (screenRect.height-dialogHeight)/2;
        
        setLocation(new Point(xLoc, yLoc));
        
        setSize(new Dimension(dialogWidth,dialogHeight));        
    }

    private void generateLayout()
    {
        /* Graphical layouting */
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel parametersPanel = new JPanel(new GridLayout(1,2));
        JPanel buttonPanel = new JPanel(new GridLayout(1,3,10,10));        
        // JPanel introPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        parametersPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        /* Intro message */
        String[] introMessage = getIntroMessage();
        JPanel introPanel = new JPanel(new GridLayout(introMessage.length,1));
        for (int i=0; i < introMessage.length; i++)
        {
            JLabel messageLine = new JLabel(introMessage[i]);
            introPanel.add(messageLine);
        }
        
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
        JPanel namesPanel = new JPanel(new GridLayout(getNrOfEntries(),1));
        JPanel valuesPanel = new JPanel(new GridLayout(getNrOfEntries(),1));
        
        parametersPanel.add(namesPanel);
        parametersPanel.add(valuesPanel);
        
        addEntries(namesPanel, valuesPanel);
        
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
        
        if ( button == setButton )
        {
            setValues();
            action = VALUES_SET;
            dispose(); 
        }
        else if ( button == resetButton )
        {
            resetValues();
        }
        else if ( button == cancelButton )
        {
            action = CANCEL;
            dispose();
        }
    }
    
    /**
     * Opens up the dialog. It will only return when the dialog has been exited by the user
     */
    public int showDialog()
    {
        if ( !init )
        {
            init();
        }
        resetValues();
        show();
        return action;
    }
    
    
    /********************* Template methods to be overridden ******************************/   

    /**
     * Method getDialogHeight.
     * @return int
     */
    protected abstract int getDialogHeight();

    /**
     * Method getDialogWidth.
     * @return int
     */
    protected abstract int getDialogWidth();

    /**
     * Method getIntroMessage.
     * Returns the Intro message that will be on top of the names/values table
     */
    protected abstract String[] getIntroMessage();

    /**
     * Method getNrOfEntries.
     * Indicates how many entries will be added by the addEntries method
     */
    protected abstract int getNrOfEntries();

    /**
     * This method should add the necessary names and values to the columns
     */
    protected abstract void addEntries(JPanel namesPanel, JPanel valuesPanel);
    
    /**
     * When the user presses the SET button. All values from the fields should be made 'available' to the 
     * outside world.
     */
    protected abstract void setValues();
    
    /**
     * When the user presses the RESET button. All values from the fields should be reset to their initial 
     * values.
     */
    protected abstract void resetValues();

}
