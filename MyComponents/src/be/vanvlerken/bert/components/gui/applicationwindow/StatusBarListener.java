/**
 * @author wItspirit
 * 11-apr-2003
 * StatusBarListener.java
 */
package be.vanvlerken.bert.components.gui.applicationwindow;

import java.util.EventListener;

/**
 * Interface to retrieve notifications of updates of the statusBar
 */
public interface StatusBarListener extends EventListener
{
    public void statusBarUpdated(StatusBarEvent statusBarEvent);
}
