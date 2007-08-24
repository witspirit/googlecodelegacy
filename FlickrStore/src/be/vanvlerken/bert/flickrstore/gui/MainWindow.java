/**
 * @author wItspirit
 * 23-okt-2005
 * MainWindow.java
 */

package be.vanvlerken.bert.flickrstore.gui;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import be.vanvlerken.bert.components.gui.applicationwindow.AboutMessage;
import be.vanvlerken.bert.components.gui.applicationwindow.ApplicationWindow;
import be.vanvlerken.bert.flickrstore.gui.grouppoolselector.GroupPoolSelector;
import be.vanvlerken.bert.flickrstore.gui.photosetselector.PhotosetSelector;
import be.vanvlerken.bert.flickrstore.login.FlickrUser;

public class MainWindow extends ApplicationWindow implements Observer
{
    private ActionFactory actionFactory;
    private JLabel userLabel;
    private int loginMessageLevel;
    public MainWindow(ActionFactory actionFactory)
    {
        super();
        this.actionFactory = actionFactory;

        generateLayout();
    }

    private void generateLayout()
    {
        setTitle("FlickrStore");
        setSize(400, 400);

        setAboutMessage();
        buildMenubar();

        JPanel mainPanel = new JPanel(new BorderLayout(0, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel userDisplay = getUserLabel();
        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Photoset Selector", new PhotosetSelector(getStatusBar(), getProgressBar(), actionFactory));
        tabs.addTab("GroupPool Selector", new GroupPoolSelector(getStatusBar(), getProgressBar(), actionFactory));

        mainPanel.add(userDisplay, BorderLayout.NORTH);
        mainPanel.add(tabs, BorderLayout.CENTER);        
                       
        // getContentPane().add(mainPanel);
        getContentPane().add(tabs);
    }

    private void buildMenubar()
    {
        JMenuBar menuBar = this.getJMenuBar();
        JMenu fileMenu = menuBar.getMenu(0);
        fileMenu.insert(new JMenuItem(actionFactory.getReloginAction()), 0);
    }

    private JPanel getUserLabel()
    {
        JLabel loginLabel = new JLabel("Logged in as ");
        userLabel = new JLabel(actionFactory.getCurrentUser().getFlickrUser().getUsername());
        loginMessageLevel = getStatusBar().addMessage("Logged in as "+actionFactory.getCurrentUser().getFlickrUser().getUsername());
        actionFactory.getCurrentUser().addObserver(this);
        // JButton reloginButton = new JButton(actionFactory.getReloginAction());
        JPanel userDisplayPanel = new JPanel();
        userDisplayPanel.add(loginLabel);
        userDisplayPanel.add(userLabel);
        // userDisplayPanel.add(reloginButton);
        return userDisplayPanel;
    }    

    /**
     * 
     */
    private void setAboutMessage()
    {
        AboutMessage about = AboutMessage.getInstance();
        about.setAuthor("Bert - wItspirit - Van Vlerken");
        about.setCopyrightMessage("Copyright 2005");
        about.setProgramName("FlickrStore");
        about.setVersion("2.0");
        // String fullMessage = "FlickrStore allows you to download an entire
        // photo collection from Flickr and store it in a local folder.
        // FlickrStore also downloads metadata like title, description and tags
        // in an extra XML file, allowing you to make a backup of your Flickr
        // collections.";
        about.setAboutMessage("FlickrStore allows you to download an entire photo collection from Flickr.");
    }

    public void update(Observable observable, Object extra)
    {
        if (observable instanceof CurrentUser)
        {
            FlickrUser flickrUser = ((CurrentUser) observable).getFlickrUser();
            // System.out.println("User changed to " + flickrUser.getUsername());
            userLabel.setText(flickrUser.getUsername());
            getStatusBar().setMessage(loginMessageLevel, "Logged in as "+flickrUser.getUsername());
        }
    }

}
