/**
 * @author vlerkenb
 * 4-feb-2003
 * GeneralLoggingPanel.java
 */
package be.vanvlerken.bert.logmonitor.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.apache.log4j.Logger;

import be.vanvlerken.bert.logmonitor.configuration.Configuration;
import be.vanvlerken.bert.logmonitor.configuration.IConfigurationListener;
import be.vanvlerken.bert.logmonitor.logging.LogLevelFilter;
import be.vanvlerken.bert.logmonitor.logging.LogView;
import be.vanvlerken.bert.logmonitor.logging.ILogView;

/**
 * A panel that allows to monitor the loggings coming in
 */
public class GeneralLoggingPanel extends JPanel implements ItemListener, ActionListener, IConfigurationListener
{
    private static Logger logger = Logger.getLogger(GeneralLoggingPanel.class);

    private IStatusBar statusBar;
    private Configuration config;

    private JButton toggleRealtimeLogging;

    private LogView logView;
    private LogDisplay logDisplay;
    private LogLevelSelector selector;
    

    public GeneralLoggingPanel(ILogView logDb, IStatusBar statusBar)
    {
        super(new BorderLayout());
        logger.info("Creating GeneralLoggingPanel");

        config = Configuration.getInstance();
        config.addConfigurationListener(this);
        this.statusBar = statusBar;
                
        logView =
            new LogView(
                logDb,
                new LogLevelFilter(
                    config.getLoggingConfig().logErrors(),
                    config.getLoggingConfig().logWarnings(),
                    config.getLoggingConfig().logInfo(),
                    config.getLoggingConfig().logVerbose()));

        logDisplay =
            new LogDisplay(
                logView,
                config.getLoggingConfig().getErrorColor(),
                config.getLoggingConfig().getWarningColor(),
                config.getLoggingConfig().getInfoColor(),
                config.getLoggingConfig().getVerboseColor(),
                config.getLoggingConfig().isNewestAtTheTop());

        add(createLoggingFilterPanel(), BorderLayout.NORTH);
        add(logDisplay.getDisplay(), BorderLayout.CENTER);
    }

    public void activate()
    {
        logView.activate();
        toggleRealtimeLogging.setText("Freeze");
        logView.setRealTime(true);
        statusBar.setStatus("Displaying Real-Time logging information...");
    }

    public void deactivate()
    {
        logView.deactivate();
        toggleRealtimeLogging.setText("Track ");
        logView.setRealTime(false);
        statusBar.setStatus("Real-Time information update is paused.");
    }

    /**
     * Method createLoggingFilterPanel.
     * @return JComponent
     */
    private JComponent createLoggingFilterPanel()
    {
        JPanel horizontalPanel = new JPanel(new BorderLayout());

        selector =
            new LogLevelSelector(
                config.getLoggingConfig().logErrors(),
                config.getLoggingConfig().logWarnings(),
                config.getLoggingConfig().logInfo(),
                config.getLoggingConfig().logVerbose(),
                logDisplay.getErrorColor(),
                logDisplay.getWarningColor(),
                logDisplay.getInfoColor(),
                logDisplay.getVerboseColor());
        JPanel selectionPanel = selector.getPanel();
        selector.addActionListener(this);
        selector.addItemListener(this);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 1));

        Border surroundBorder = BorderFactory.createEtchedBorder();
        Border buttonBorder = BorderFactory.createTitledBorder(surroundBorder, "Real-Time control");
        buttonPanel.setBorder(buttonBorder);

        toggleRealtimeLogging = new JButton("Track ");
        Border lineBorder = BorderFactory.createEtchedBorder();
        Border spacerBorder = BorderFactory.createEmptyBorder(5, 40, 5, 40);
        Border compoundBorder = BorderFactory.createCompoundBorder(lineBorder, spacerBorder);
        toggleRealtimeLogging.setBorder(compoundBorder);

        toggleRealtimeLogging.addActionListener(this);

        buttonPanel.add(toggleRealtimeLogging);

        horizontalPanel.add(selectionPanel, BorderLayout.CENTER);
        horizontalPanel.add(buttonPanel, BorderLayout.EAST);

        return horizontalPanel;
    }

    /**
     * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
     */
    public synchronized void itemStateChanged(ItemEvent ie)
    {
        /* We don't care about which checkbox has changed, that it changed is enough */
        logView.setFilter(new LogLevelFilter(selector.logErrors(), selector.logWarnings(), selector.logInfo(), selector.logVerbose()));

        /* Notify configuration of this change */
        switch (selector.getItemSource(ie))
        {
            case LogLevelSelector.LOG_ERRORS_CHANGED :
                config.getLoggingConfig().logErrors(selector.logErrors());
                break;
            case LogLevelSelector.LOG_WARNINGS_CHANGED :
                config.getLoggingConfig().logWarnings(selector.logWarnings());
                break;
            case LogLevelSelector.LOG_INFO_CHANGED :
                config.getLoggingConfig().logInfo(selector.logInfo());
                break;
            case LogLevelSelector.LOG_VERBOSE_CHANGED :
                config.getLoggingConfig().logVerbose(selector.logVerbose());
                break;
        }
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae)
    {
        AbstractButton button = (AbstractButton) ae.getSource();

        if (button == toggleRealtimeLogging & !logView.getRealtime())
        {
            logView.setRealTime(true);
            toggleRealtimeLogging.setText("Freeze");
            statusBar.setStatus("Displaying Real-Time information...");
        }
        else if (button == toggleRealtimeLogging & logView.getRealtime())
        {
            logView.setRealTime(false);
            toggleRealtimeLogging.setText("Track ");
            statusBar.setStatus("Real-Time information update is paused.");
        }
        else
        {
            /* It's one of the coloured buttons... Launch a ColorChooser */
            logger.info("Update the display colors...");
            Color selectedColor;

            switch (selector.getActionSource(ae))
            {
                case LogLevelSelector.ERROR_COLOR_CHANGED :
                    selectedColor = selector.getErrorColor();
                    logDisplay.setErrorColor(selectedColor);
                    config.getLoggingConfig().setErrorColor(selectedColor);
                    break;
                case LogLevelSelector.WARNING_COLOR_CHANGED :
                    selectedColor = selector.getWarningColor();
                    logDisplay.setWarningColor(selectedColor);
                    config.getLoggingConfig().setWarningColor(selectedColor);
                    break;
                case LogLevelSelector.INFO_COLOR_CHANGED :
                    selectedColor = selector.getInfoColor();
                    logDisplay.setInfoColor(selectedColor);
                    config.getLoggingConfig().setInfoColor(selectedColor);
                    break;
                case LogLevelSelector.VERBOSE_COLOR_CHANGED :
                    selectedColor = selector.getVerboseColor();
                    logDisplay.setVerboseColor(selectedColor);
                    config.getLoggingConfig().setVerboseColor(selectedColor);
                    break;
                default :
                    logger.warn("Got an actionPerformed for an unknown button !");
                    return;

            }

            logger.info("Selected color: " + selectedColor);
        }
    }

    /**
     * @see be.vanvlerken.bert.logmonitor.configuration.IConfigurationListener#configUpdated()
     */
    public void configUpdated()
    {
        if ( logDisplay != null )
        {
            logDisplay.setNewestAtTheTop(config.getLoggingConfig().isNewestAtTheTop());
        }
    }
}
