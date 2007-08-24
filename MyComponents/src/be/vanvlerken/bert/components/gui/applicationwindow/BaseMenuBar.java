/**
 * @author wItspirit
 * 10-apr-2003
 * BaseMenuBar.java
 */package be.vanvlerken.bert.components.gui.applicationwindow;

import javax.swing.Box;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * Basic menubar implementing at least a File>Exit and Help>About element
 */
public class BaseMenuBar extends JMenuBar
{
    private ApplicationWindow   appWindow;
    
    public BaseMenuBar(ApplicationWindow appWindow)
    {
        this.appWindow = appWindow;
        
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem(new ExitAction(appWindow));
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem(new AboutAction(appWindow));
        helpMenu.add(aboutItem);
        
        
        this.add(fileMenu);
        this.add(Box.createHorizontalGlue());
        this.add(helpMenu);
    }
}
