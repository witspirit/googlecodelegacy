/**
 * @author wItspirit
 * 19-apr-2003
 * ConfigAction.java
 */
package be.vanvlerken.bert.packetdistributor.ui.swing.configuration;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

/**
 * Action that provides a dialog for setting the necessary variables for the
 * PacketDistributorGui
 */
public class ConfigAction extends AbstractAction
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3258130258506036534L;
    private JFrame            mainWindow;

    public ConfigAction(JFrame mainWindow)
    {
        super("Configuration...");
        this.mainWindow = mainWindow;
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
     */
    public void actionPerformed(ActionEvent ae)
    {
        Configuration config = Configuration.getInstance();
        GuiConfig guiConfig = config.getGuiConfig();
        ConfigDialog configDialog = new ConfigDialog(mainWindow);
        configDialog.setInitialInterface(guiConfig.getPacketDistributorInterface());
        configDialog.setInitialConfigFile(guiConfig.getConfigFile());
        configDialog.setInitialRmiUrl(guiConfig.getRmiUrl());

        int operation = configDialog.showDialog();
        if (operation == ConfigDialog.VALUES_SET)
        {
            // System.out.println(configDialog.getEnteredIpStr());
            // System.out.println(configDialog.getEnteredPort());

            guiConfig.setPacketDistributorInterface(configDialog.getEnteredInterface());
            guiConfig.setConfigFile(configDialog.getEnteredConfigFile());
            guiConfig.setRmiUrl(configDialog.getEnteredRmiUrl());
            config.configUpdated();
        }
    }
}
