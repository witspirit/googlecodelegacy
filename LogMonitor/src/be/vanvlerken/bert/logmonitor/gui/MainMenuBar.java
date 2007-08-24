/**
 * @author vlerkenb
 * 4-feb-2003
 * MainMenuBar.java
 */
package be.vanvlerken.bert.logmonitor.gui;

import javax.swing.Box;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

import be.vanvlerken.bert.logmonitor.LogMonitor;
import be.vanvlerken.bert.logmonitor.actions.*;
import be.vanvlerken.bert.logmonitor.configuration.Configuration;
import be.vanvlerken.bert.logmonitor.logging.IDatabasePersister;

/**
 * Constructs and manages the menu bar
 */
public class MainMenuBar extends JMenuBar implements ChangeListener
{
    private static Logger     logger = Logger.getLogger(MainMenuBar.class);

    private JCheckBoxMenuItem newestAtTheTopCheckbox;

    private JCheckBoxMenuItem respectNewlinesCheckbox;

    public MainMenuBar(JFrame mainWindow, IDatabasePersister dbPersister)
    {
        logger.debug("Obtaining the configuration of the system...");

        logger.debug("Building the File Menu");
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem(new ExitAction(mainWindow));
        JMenuItem clearDbItem = new JMenuItem(new ClearDatabaseAction());
        JMenuItem loadDbItem = new JMenuItem(new LoadDatabaseAction(mainWindow,
                dbPersister));
        JMenuItem saveDbItem = new JMenuItem(new SaveDatabaseAction(mainWindow,
                dbPersister));
        JMenuItem saveAsDbItem = new JMenuItem(new SaveDatabaseAsAction(
                mainWindow, dbPersister));

        fileMenu.add(clearDbItem);
        fileMenu.add(loadDbItem);
        fileMenu.add(saveDbItem);
        fileMenu.add(saveAsDbItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        this.add(fileMenu);

        logger.debug("Building the Config Menu");
        JMenu guiConfigMenu = new JMenu("Config");
        JMenuItem loggingItem = new JMenuItem(new LogServerAction(mainWindow));
        newestAtTheTopCheckbox = new JCheckBoxMenuItem("Newest At The Top");
        newestAtTheTopCheckbox.setSelected(Configuration.getInstance()
                .getLoggingConfig().isNewestAtTheTop());
        newestAtTheTopCheckbox.addChangeListener(this);
        respectNewlinesCheckbox = new JCheckBoxMenuItem("Respect Newlines");
        respectNewlinesCheckbox.setSelected(Configuration.getInstance()
                .getLoggingConfig().respectsNewlines());
        respectNewlinesCheckbox.addChangeListener(this);

        JMenuItem showConfigItem = new JMenuItem(new ShowConfigAction(
                mainWindow));
        JMenuItem loadConfigItem = new JMenuItem(new LoadConfigAction(
                mainWindow));
        JMenuItem saveConfigItem = new JMenuItem(new SaveConfigAction(
                mainWindow));
        JMenuItem saveConfigAsItem = new JMenuItem(new SaveConfigAsAction(
                mainWindow));

        guiConfigMenu.add(loggingItem);
        guiConfigMenu.addSeparator();
        guiConfigMenu.add(newestAtTheTopCheckbox);
        guiConfigMenu.add(respectNewlinesCheckbox);
        guiConfigMenu.addSeparator();
        guiConfigMenu.add(showConfigItem);
        guiConfigMenu.add(loadConfigItem);
        guiConfigMenu.add(saveConfigItem);
        guiConfigMenu.add(saveConfigAsItem);

        this.add(guiConfigMenu);

        this.add(Box.createHorizontalGlue());

        logger.debug("Building the Help Menu");
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem(new AboutAction(mainWindow));

        helpMenu.add(aboutItem);

        this.add(helpMenu);
    }

    /**
     * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    public void stateChanged(ChangeEvent ce)
    {
        if (ce.getSource() == newestAtTheTopCheckbox)
        {
            Configuration.getInstance().getLoggingConfig().setNewestAtTheTop(
                    newestAtTheTopCheckbox.isSelected());
        }
        else if (ce.getSource() == respectNewlinesCheckbox)
        {
            Configuration.getInstance().getLoggingConfig().setRespectNewlines(
                    respectNewlinesCheckbox.isSelected());
            // Dirty hack to immediately trigger the logDatabase
            LogMonitor.getInstance().getLogDatabase().setRespectNewlines(
                    respectNewlinesCheckbox.isSelected());
        }
    }
}