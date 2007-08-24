/**
 * @author wItspirit
 * 9-apr-2003
 * ShutdownListener.java
 */package be.vanvlerken.bert.components.gui.applicationwindow;

/**
 * Should be implemented by any object that wants to perform the shutdown of the application
 * Typically this is only one object within an application. It is typically the 
 * startup object that has all the references to the rest of the system.
 */
public interface ShutdownListener
{
    public void performShutdown();
}
