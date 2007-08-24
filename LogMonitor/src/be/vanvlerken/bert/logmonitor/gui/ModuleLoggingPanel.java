/**
 * @author wItspirit
 * 16-nov-2003
 * ModuleLoggingPanel.java
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
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import be.vanvlerken.bert.logmonitor.configuration.Configuration;
import be.vanvlerken.bert.logmonitor.configuration.IConfigurationListener;
import be.vanvlerken.bert.logmonitor.logging.CompositeFilter;
import be.vanvlerken.bert.logmonitor.logging.ILogView;
import be.vanvlerken.bert.logmonitor.logging.LogLevelFilter;
import be.vanvlerken.bert.logmonitor.logging.LogView;
import be.vanvlerken.bert.logmonitor.logging.ModuleFilter;

/**
 * This panel displays module specific logging
 * It uses a moduleFilter to log for one or more specific modules
 */
public class ModuleLoggingPanel extends JPanel implements ItemListener, ActionListener, ListSelectionListener, IConfigurationListener
{
    private LogView logView;
    private IStatusBar statusBar;
    private Configuration config;

    private static final boolean logErrorsDefault = true;
    private static final boolean logWarningsDefault = true;
    private static final boolean logInfoDefault = true;
    private static final boolean logVerboseDefault = true;

    private ModuleFilter moduleFilter;
    private LogLevelFilter logLevelFilter;

    private LogDisplay logDisplay;
    private LogLevelSelector selector;
    private ModuleList moduleList;

    private JButton toggleRealtimeLogging;

    public ModuleLoggingPanel(ILogView logDb, IStatusBar statusBar)
    {
        super();

        this.statusBar = statusBar;
        config = Configuration.getInstance();
        config.addConfigurationListener(this);

        // Prepare the necessary filters
        logLevelFilter = new LogLevelFilter(logErrorsDefault, logWarningsDefault, logInfoDefault, logVerboseDefault);
        moduleFilter = new ModuleFilter(false, new String[0]); // It will block everything, except those in the filter
        CompositeFilter compositeFilter = new CompositeFilter();
        compositeFilter.addFilter(logLevelFilter);
        compositeFilter.addFilter(moduleFilter);

        // Prepare a new logView with the necessary filters
        logView = new LogView(logDb, compositeFilter);

        // Prepare the GUI components
        prepareGUIComponents(logDb, logView);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, moduleList.getListDisplay(), logDisplay.getDisplay());
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(150);

        // Add the GUI components on the Panel
        setLayout(new BorderLayout());
        add(createTopPanel(), BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }

    /**
     * @return
     */
    private JPanel createTopPanel()
    {
        JPanel horizontalPanel = new JPanel(new BorderLayout());

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

        horizontalPanel.add(selector.getPanel(), BorderLayout.CENTER);
        horizontalPanel.add(buttonPanel, BorderLayout.EAST);

        return horizontalPanel;
    }

    private void prepareGUIComponents(ILogView rawView, ILogView filteredView)
    {
        logDisplay =
            new LogDisplay(
                filteredView,
                config.getLoggingConfig().getErrorColor(),
                config.getLoggingConfig().getWarningColor(),
                config.getLoggingConfig().getInfoColor(),
                config.getLoggingConfig().getVerboseColor(),
                config.getLoggingConfig().isNewestAtTheTop());

        selector =
            new LogLevelSelector(
                logErrorsDefault,
                logWarningsDefault,
                logInfoDefault,
                logVerboseDefault,
                logDisplay.getErrorColor(),
                logDisplay.getWarningColor(),
                logDisplay.getInfoColor(),
                logDisplay.getVerboseColor());
        selector.addActionListener(this);
        selector.addItemListener(this);

        moduleList = new ModuleList(rawView);
        moduleList.addListSelectionListener(this);
    }

    /**
         * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
         */
    public synchronized void itemStateChanged(ItemEvent ie)
    {
        // Prepare the necessary filters
        logLevelFilter = new LogLevelFilter(selector.logErrors(), selector.logWarnings(), selector.logInfo(), selector.logVerbose());
        CompositeFilter compositeFilter = new CompositeFilter();
        compositeFilter.addFilter(logLevelFilter);
        compositeFilter.addFilter(moduleFilter);
        logView.setFilter(compositeFilter);

        //        /* Notify configuration of this change */
        //        switch (selector.getItemSource(ie))
        //        {
        //            case LogLevelSelector.LOG_ERRORS_CHANGED :
        //                config.getLoggingConfig().logErrors(selector.logErrors());
        //                break;
        //            case LogLevelSelector.LOG_WARNINGS_CHANGED :
        //                config.getLoggingConfig().logWarnings(selector.logWarnings());
        //                break;
        //            case LogLevelSelector.LOG_INFO_CHANGED :
        //                config.getLoggingConfig().logInfo(selector.logInfo());
        //                break;
        //            case LogLevelSelector.LOG_VERBOSE_CHANGED :
        //                config.getLoggingConfig().logVerbose(selector.logVerbose());
        //                break;
        //        }
        //
        //        config.configUpdated();
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
            Color selectedColor;

            switch (selector.getActionSource(ae))
            {
                case LogLevelSelector.ERROR_COLOR_CHANGED :
                    selectedColor = selector.getErrorColor();
                    logDisplay.setErrorColor(selectedColor);
                    // config.getLoggingConfig().setErrorColor(selectedColor);
                    break;
                case LogLevelSelector.WARNING_COLOR_CHANGED :
                    selectedColor = selector.getWarningColor();
                    logDisplay.setWarningColor(selectedColor);
                    // config.getLoggingConfig().setWarningColor(selectedColor);
                    break;
                case LogLevelSelector.INFO_COLOR_CHANGED :
                    selectedColor = selector.getInfoColor();
                    logDisplay.setInfoColor(selectedColor);
                    // config.getLoggingConfig().setInfoColor(selectedColor);
                    break;
                case LogLevelSelector.VERBOSE_COLOR_CHANGED :
                    selectedColor = selector.getVerboseColor();
                    logDisplay.setVerboseColor(selectedColor);
                    // config.getLoggingConfig().setVerboseColor(selectedColor);
                    break;
                default :
                    return;

            }

            // config.configUpdated();
        }
    }

    public void activate()
    {
        logView.activate();
        logView.setRealTime(true);
        toggleRealtimeLogging.setText("Freeze");
        statusBar.setStatus("Displaying Real-Time logging information...");
    }

    public void deactivate()
    {
        logView.deactivate();
        logView.setRealTime(false);
        toggleRealtimeLogging.setText("Track ");
        statusBar.setStatus("Real-Time information update is paused.");
    }

    /**
     * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    public void valueChanged(ListSelectionEvent lse)
    {
        moduleFilter = new ModuleFilter(false, moduleList.getSelectedModules()); // It will block everything, except those in the filter
        CompositeFilter compositeFilter = new CompositeFilter();
        compositeFilter.addFilter(logLevelFilter);
        compositeFilter.addFilter(moduleFilter);
        logView.setFilter(compositeFilter);
    }

    /**
     * @see be.vanvlerken.bert.logmonitor.configuration.IConfigurationListener#configUpdated()
     */
    public void configUpdated()
    {
        if (logDisplay != null)
        {
            logDisplay.setNewestAtTheTop(config.getLoggingConfig().isNewestAtTheTop());
        }
    }
}
