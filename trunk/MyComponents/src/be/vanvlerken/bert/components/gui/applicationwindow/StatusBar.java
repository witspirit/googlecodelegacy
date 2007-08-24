/**
 * @author wItspirit
 * 10-apr-2003
 * StatusBar.java
 */
package be.vanvlerken.bert.components.gui.applicationwindow;

/**
 * Specifies the interface a StatusBar has to offer for full functionality
 */
public interface StatusBar 
{
    public int addMessage(String message);
    public int setMessage(int level, String message);
    public void clearMesssage(int level);
    public int getLevel();
    public String getMessage();
    public String getMessage(int level);
    
    public void setStatusBarListener(StatusBarListener statusBarListener);
}
