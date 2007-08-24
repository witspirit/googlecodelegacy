/**
 * @author wItspirit
 * 10-apr-2003
 * StatusBarContainer.java
 */

package be.vanvlerken.bert.components.gui.applicationwindow;

import javax.swing.JComponent;

/**
 * Interface defining the minimum interface for a StatusBarContainer
 */
public interface StatusBarContainer
{
    public void setStatusBar(StatusBar statusBar);
    public JComponent getJComponent();
}
