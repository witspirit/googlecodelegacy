/**
 * @author wItspirit
 * 5-apr-2003
 * ApplicationWindow.java
 */
package be.vanvlerken.bert.components.gui.applicationwindow;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;


/**
 * Provides basic Application Window functionality
 * It can start with a certain size or 'maximized'
 * It provides a default menubar consisting of a File menu and a help menu
 * with at least File>Exit and Help>About.
 * It generates a statusbar with a powerful interface for layered access
 * Thus it abstracts some basic construction that can help in rapidly developing
 * a gui application
 * It can be used as a Template class when it is subclassed to add behaviour or it can
 * be used as a support class when operations are delegated to it.
 */
public class ApplicationWindow extends JFrame implements WindowListener, ShutdownListener
{
    private ShutdownListener    shutdownListener;
    private StatusBar           statusBar;
    private StatusBarContainer  statusBarContainer;
    private JPanel              centerPanel;
    private boolean            centered;
    
    public ApplicationWindow(String title)
    {
        super(title);
        commonInit();
    }
    
    public ApplicationWindow()
    {
        super("Application Window");
        commonInit();
    }

    /**
     * Method commonInit.
     */
    private void commonInit()
    {        
        shutdownListener = this;
        centered = true;
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.addWindowListener(this);
        
        this.setSize(300,300);
        
        this.setJMenuBar(new BaseMenuBar(this));
        statusBar = new BaseStatusBar();
        statusBarContainer = new BaseStatusBarComponent(statusBar);
        
        centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(statusBarContainer.getJComponent(), BorderLayout.SOUTH);
        super.getContentPane().add(mainPanel);
    }
    
    public void setSize(int width, int height)
    {
        if ( centered )
        {
            GraphicsEnvironment gE = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Rectangle screenRect = gE.getMaximumWindowBounds();
            int xLoc = (screenRect.width - width) / 2;
            int yLoc = (screenRect.height - height) / 2;
            this.setLocation(new Point(xLoc, yLoc));
        }
        super.setSize(width, height);
    }
    
    public void setSize(Dimension newSize)
    {
        setSize(newSize.width, newSize.height);
    }

    public Container getContentPane()
    {
        return (Container) centerPanel;
    }

    public void setMaximized()
    {
        GraphicsEnvironment gE = GraphicsEnvironment.getLocalGraphicsEnvironment();
        this.setBounds(gE.getMaximumWindowBounds());
    }
    
    /**
     * Method shutdown.
     */
    public void shutdown()
    {
        shutdownListener.performShutdown();
    }
    
    /**
     * Register your own shutdown listener
     */
    public void setShutdownListener(ShutdownListener shutdownListener)
    {
        this.shutdownListener = shutdownListener;
    }
    
    /**
     * A default shutdown implementation
     */
    public void performShutdown()
    {
        System.exit(0);
    }


    /**************** WindowListener Interface ****************************/

    /**
     * @see java.awt.event.WindowListener#windowActivated(WindowEvent)
     */
    public void windowActivated(WindowEvent arg0)
    {
    }

    /**
     * @see java.awt.event.WindowListener#windowClosed(WindowEvent)
     */
    public void windowClosed(WindowEvent arg0)
    {
        shutdown();
    }    

    /**
     * @see java.awt.event.WindowListener#windowClosing(WindowEvent)
     */
    public void windowClosing(WindowEvent arg0)
    {
    }

    /**
     * @see java.awt.event.WindowListener#windowDeactivated(WindowEvent)
     */
    public void windowDeactivated(WindowEvent arg0)
    {
    }

    /**
     * @see java.awt.event.WindowListener#windowDeiconified(WindowEvent)
     */
    public void windowDeiconified(WindowEvent arg0)
    {
    }

    /**
     * @see java.awt.event.WindowListener#windowIconified(WindowEvent)
     */
    public void windowIconified(WindowEvent arg0)
    {
    }

    /**
     * @see java.awt.event.WindowListener#windowOpened(WindowEvent)
     */
    public void windowOpened(WindowEvent arg0)
    {
    }
    
    /**
     * Returns the statusBar.
     * @return StatusBar
     */
    public StatusBar getStatusBar()
    {
        return statusBar;
    }

    /**
     * Sets the statusBar.
     * @param statusBar The statusBar to set
     */
    public void setStatusBar(StatusBar statusBar)
    {
        this.statusBar = statusBar;
        this.statusBarContainer.setStatusBar(statusBar);
    }

    /**
     * Returns the statusBarContainer
     * @return StatusBarContainer
     */
    public StatusBarContainer getStatusBarContainer()
    {
        return statusBarContainer;
    }

    /**
     * Sets the statusBarContainer
     * It will NOT reset the container with the current statusBar
     * -> This should be done by the application that replaces the statusBarContainer
     * This system allows for a special kind of statusBar, combined with it's own StatusBar
     * @param statusBarContainer The statusBarContainer to set
     */
    public void setStatusBarContainer(StatusBarContainer statusBarContainer)
    {
        this.statusBarContainer = statusBarContainer;
    }


}
