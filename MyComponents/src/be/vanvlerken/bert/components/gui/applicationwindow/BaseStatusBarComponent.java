/**
 * @author wItspirit
 * 10-apr-2003
 * BaseStatusBarComponent.java
 */
package be.vanvlerken.bert.components.gui.applicationwindow;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.Border;

/**
 * Basic Swing component that displays a StatusBar
 */
public class BaseStatusBarComponent implements StatusBarContainer, StatusBarListener
{
    private JLabel      label;
    private StatusBar   statusBar;
    
    public BaseStatusBarComponent(StatusBar statusBar)
    {
        label = new JLabel();
        this.statusBar = statusBar;
        
        Border marginBorder = BorderFactory.createEmptyBorder(1,3,1,3);
        Border surroundBorder = BorderFactory.createEtchedBorder();
        Border compoundBorder = BorderFactory.createCompoundBorder(surroundBorder, marginBorder);
        label.setBorder(compoundBorder);
        
        label.setText(statusBar.getMessage());
        
        statusBar.setStatusBarListener(this);
    }
    
    /**
     * @see be.vanvlerken.bert.components.gui.applicationwindow.StatusBarContainer#setStatusBar(StatusBar)
     */
    public void setStatusBar(StatusBar statusBar)
    {
        this.statusBar = statusBar;
    }

    /**
     * @see be.vanvlerken.bert.components.gui.applicationwindow.StatusBarComponentSupport#getJComponent()
     */
    public JComponent getJComponent()
    {
        return label;
    }
    
    /**
     * @see be.vanvlerken.bert.components.gui.applicationwindow.StatusBarListener#statusBarUpdated(StatusBarEvent)
     */
    public void statusBarUpdated(StatusBarEvent statusBarEvent)
    {
        label.setText(statusBar.getMessage());
    }

}
