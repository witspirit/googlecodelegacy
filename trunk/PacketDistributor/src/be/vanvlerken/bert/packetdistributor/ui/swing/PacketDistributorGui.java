/**
 * @author wItspirit
 * 9-apr-2003
 * PacketDistributorGui.java
 */
package be.vanvlerken.bert.packetdistributor.ui.swing;

import java.awt.Container;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.MenuElement;
import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.vanvlerken.bert.components.gui.applicationwindow.AboutMessage;
import be.vanvlerken.bert.components.gui.applicationwindow.ApplicationWindow;
import be.vanvlerken.bert.components.gui.applicationwindow.ShutdownListener;
import be.vanvlerken.bert.components.gui.fileselector.ExtensionFileFilter;
import be.vanvlerken.bert.components.gui.fileselector.FileOperationFactory;
import be.vanvlerken.bert.components.gui.fileselector.FileSelector;
import be.vanvlerken.bert.components.gui.fileselector.SaveAction;
import be.vanvlerken.bert.packetdistributor.ui.swing.backend.IPacketDistributorFacade;
import be.vanvlerken.bert.packetdistributor.ui.swing.configuration.ConfigAction;
import be.vanvlerken.bert.packetdistributor.ui.swing.configuration.Configuration;
import be.vanvlerken.bert.packetdistributor.ui.swing.frontend.AddRemovePanel;
import be.vanvlerken.bert.packetdistributor.ui.swing.frontend.impl.DestinationModel;
import be.vanvlerken.bert.packetdistributor.ui.swing.frontend.impl.RelayModel;
import be.vanvlerken.bert.packetdistributor.ui.swing.frontend.impl.SourceModel;

/**
 * Startup class for the GUI for the PacketDistributorServer application
 */
public class PacketDistributorGui implements Observer, Runnable, ShutdownListener
{
    private static final Log     log = LogFactory.getLog(PacketDistributorGui.class);
    private ApplicationWindow    mainWindow;
    private Configuration        config;
    private FileOperationFactory configFileOperations;
    private RelayModel           relayModel;

    public static void main(String[] args)
    {
        PacketDistributorGui guiApp = new PacketDistributorGui(args);
        SwingUtilities.invokeLater(guiApp);
    }

    public PacketDistributorGui(String[] args)
    {
        mainWindow = null;
        config = null;
        configFileOperations = null;
        relayModel = null;
    }

    /**
     * Runnable
     */
    public void run()
    {
        prepareApplicationWindow();
        loadConfiguration();
        setupGui();

        mainWindow.setShutdownListener(this);

        mainWindow.setVisible(true);
    }

    private void prepareApplicationWindow()
    {
        log.info("Preparing application window...");
        mainWindow = new ApplicationWindow("PacketDistributorServer GUI");
        mainWindow.setMaximized();

        AboutMessage aboutMessage = AboutMessage.getInstance();
        aboutMessage.setProgramName("PacketDistributorGui");
        aboutMessage.setAuthor("Bert -wItspirit- Van Vlerken");
        aboutMessage.setAboutMessage("This program provides a management GUI for the"
                + " PacketDistributorServer application. It allows to configure it" + " and monitor it's behavior.");
        aboutMessage.setVersion("0.9");
    }

    private void loadConfiguration()
    {
        config = Configuration.getInstance();
        File defaultConfigFile = new File(System.getProperty("user.dir") + "/pdconfig.xml");
        try
        {
            config.open(defaultConfigFile);
        }
        catch (FileNotFoundException e)
        {
            /* Silently ignore... Might be first start-up for this user */
            log.info("Default config file not found.");
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(mainWindow, e.getMessage(), "IO Exception", JOptionPane.ERROR_MESSAGE);
        }
        config.addObserver(this);
    }

    private void setupGui()
    {
        addMenuItems(mainWindow);
        addPanels(mainWindow);
    }

    /**
     * @param mainWindow
     */
    private void addPanels(ApplicationWindow mainWindow)
    {
        Container contentPane = mainWindow.getContentPane();
        contentPane.removeAll();
        contentPane.setLayout(new GridLayout(1, 3));

        IPacketDistributorFacade pdFacade = config.getPacketDistributorAPI();
        
        relayModel = new RelayModel(pdFacade);
        SourceModel sourceModel = new SourceModel();
        DestinationModel destinationModel = new DestinationModel();

        relayModel.addSelectionListener(sourceModel);
        relayModel.addSelectionListener(destinationModel);

        AddRemovePanel relayPanel = new AddRemovePanel(relayModel);
        AddRemovePanel sourcePanel = new AddRemovePanel(sourceModel);
        AddRemovePanel destinationPanel = new AddRemovePanel(destinationModel);

        contentPane.add(relayPanel);
        contentPane.add(sourcePanel);
        contentPane.add(destinationPanel);

        contentPane.validate();
    }

    /**
     * Method addConfigItem. Adds a configuration menu item to the application
     * window
     * 
     * @param mainWindow
     */
    private void addMenuItems(ApplicationWindow mainWindow)
    {
        /* Retrieve the FileMenu */
        JMenuBar menuBar = mainWindow.getJMenuBar();
        MenuElement[] menus = menuBar.getSubElements();
        JMenu fileMenu = (JMenu) menus[0];

        /* Add the File operations */
        FileSelector configSelector = FileSelector.getFileSelector("config");
        configSelector.setFileFilter(new ExtensionFileFilter("Configuration files (*.xml)", "xml"));
        configSelector.setSelectedFile(new File("pdconfig.xml"));
        configFileOperations = new FileOperationFactory("", configSelector, config, mainWindow);
        int menuOffset = configFileOperations.insertIntoMenu(fileMenu, 0);

        /* Add the configuration ability */
        Action configAction = new ConfigAction(mainWindow);
        JMenuItem configMenuItem = new JMenuItem(configAction);
        fileMenu.add(configMenuItem, menuOffset++);
    }

    /**
     * @see be.vanvlerken.bert.components.gui.ShutdownListener#performShutdown()
     */
    public void performShutdown()
    {
        log.info("Shutting down...");

        relayModel.cleanUp();

        if (config.needsSave())
        {
            /*
             * Should show dialog asking for saving or not If they want to save,
             * try saveConfigAction
             */
            log.info("Should check wether the user wants to save his configuration or not");

            int yesno = JOptionPane.showConfirmDialog(mainWindow,
                    "Configuration has changed since last save.\nDo you want to save your configuration ?", "Do you want to save configuration ?",
                    JOptionPane.YES_NO_OPTION);
            if (yesno == JOptionPane.YES_OPTION)
            {
                /* Create saveConfigAction */
                SaveAction saveAction = configFileOperations.getSaveAction();
                saveAction.actionPerformed(null);
            }
        }

        log.info("Shutdown complete. Bye bye...");

        /*
         * Because some stuff happens with AWT when changing the components in
         * the content pane, I have to resort to System.exit(). There might be a
         * cleaner way, but it certainly is much harder. This system.exit()
         * however may cause some annoying behavior in combination with other
         * Java-based tools.
         */
        System.exit(0);
    }

    /**
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update(Observable observable, Object obj)
    {
        relayModel.cleanUp();
        
        addPanels(mainWindow);
    }
}
