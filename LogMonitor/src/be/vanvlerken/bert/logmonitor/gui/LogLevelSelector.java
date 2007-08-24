/**
 * @author wItspirit
 * 15-nov-2003
 * LogLevelSelector.java
 */

package be.vanvlerken.bert.logmonitor.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * This class manages the log level selection
 * This consists of a checkbox for every log level and a a color chooser for every log level
 */
public class LogLevelSelector implements ActionListener
{
    public static final int UNKNOWN_SOURCE = -1;

    public static final int LOG_ERRORS_CHANGED = 0;
    public static final int LOG_WARNINGS_CHANGED = 1;
    public static final int LOG_INFO_CHANGED = 2;
    public static final int LOG_VERBOSE_CHANGED = 3;

    public static final int ERROR_COLOR_CHANGED = 10;
    public static final int WARNING_COLOR_CHANGED = 11;
    public static final int INFO_COLOR_CHANGED = 12;
    public static final int VERBOSE_COLOR_CHANGED = 13;

    private JPanel selectorPanel;

    private JCheckBox logErrorsCheck;
    private JCheckBox logWarningsCheck;
    private JCheckBox logInformationalCheck;
    private JCheckBox logVerboseCheck;

    private JButton errorColorButton;
    private JButton warningColorButton;
    private JButton infoColorButton;
    private JButton verboseColorButton;

    private List actionListeners;

    public LogLevelSelector(
        boolean logErrors,
        boolean logWarnings,
        boolean logInfo,
        boolean logVerbose,
        Color errorColor,
        Color warningColor,
        Color infoColor,
        Color verboseColor)
    {
        actionListeners = new ArrayList();

        logErrorsCheck = new JCheckBox("Errors", logErrors);
        logWarningsCheck = new JCheckBox("Warnings", logWarnings);
        logInformationalCheck = new JCheckBox("Informational", logInfo);
        logVerboseCheck = new JCheckBox("Verbose", logVerbose);

        errorColorButton = new JButton("");
        warningColorButton = new JButton("");
        infoColorButton = new JButton("");
        verboseColorButton = new JButton("");

        errorColorButton.setBackground(errorColor);
        warningColorButton.setBackground(warningColor);
        infoColorButton.setBackground(infoColor);
        verboseColorButton.setBackground(verboseColor);

        errorColorButton.addActionListener(this);
        warningColorButton.addActionListener(this);
        infoColorButton.addActionListener(this);
        verboseColorButton.addActionListener(this);

        selectorPanel = buildPanel();
    }

    /**
     * Puts all the components together to build the selectorPanel
     */
    protected JPanel buildPanel()
    {
        JPanel selectionPanel = new JPanel(new GridLayout(2, 2));

        Border surroundBorder = BorderFactory.createEtchedBorder();
        Border selectionBorder = BorderFactory.createTitledBorder(surroundBorder, "Logging levels");
        selectionPanel.setBorder(selectionBorder);

        JPanel errorPanel = new JPanel(new GridLayout(1, 2));
        JPanel warningPanel = new JPanel(new GridLayout(1, 2));
        JPanel infoPanel = new JPanel(new GridLayout(1, 2));
        JPanel verbosePanel = new JPanel(new GridLayout(1, 2));

        selectionPanel.add(errorPanel);
        selectionPanel.add(infoPanel);
        selectionPanel.add(warningPanel);
        selectionPanel.add(verbosePanel);

        JPanel errorColorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel warningColorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel infoColorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel verboseColorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        Dimension maxSize = new Dimension(15, 15);
        errorColorButton.setPreferredSize(maxSize);
        warningColorButton.setPreferredSize(maxSize);
        infoColorButton.setPreferredSize(maxSize);
        verboseColorButton.setPreferredSize(maxSize);

        errorColorPanel.add(errorColorButton);
        warningColorPanel.add(warningColorButton);
        infoColorPanel.add(infoColorButton);
        verboseColorPanel.add(verboseColorButton);

        errorPanel.add(logErrorsCheck);
        errorPanel.add(errorColorPanel);

        warningPanel.add(logWarningsCheck);
        warningPanel.add(warningColorPanel);

        infoPanel.add(logInformationalCheck);
        infoPanel.add(infoColorPanel);

        verbosePanel.add(logVerboseCheck);
        verbosePanel.add(verboseColorPanel);

        return selectionPanel;
    }

    public JPanel getPanel()
    {
        return selectorPanel;
    }

    public void addItemListener(ItemListener il)
    {
        logErrorsCheck.addItemListener(il);
        logWarningsCheck.addItemListener(il);
        logInformationalCheck.addItemListener(il);
        logVerboseCheck.addItemListener(il);
    }

    public int getItemSource(ItemEvent ie)
    {
        int source;

        if (ie.getSource() == logErrorsCheck)
        {
            source = LOG_ERRORS_CHANGED;
        }
        else if (ie.getSource() == logWarningsCheck)
        {
            source = LOG_WARNINGS_CHANGED;
        }
        else if (ie.getSource() == logInformationalCheck)
        {
            source = LOG_INFO_CHANGED;
        }
        else if (ie.getSource() == logVerboseCheck)
        {
            source = LOG_VERBOSE_CHANGED;
        }
        else
        {
            source = UNKNOWN_SOURCE;
        }

        return source;
    }

    public void addActionListener(ActionListener al)
    {
        actionListeners.add(al);
    }

    public int getActionSource(ActionEvent ae)
    {
        int source;

        if (ae.getSource() == errorColorButton)
        {
            source = ERROR_COLOR_CHANGED;
        }
        else if (ae.getSource() == warningColorButton)
        {
            source = WARNING_COLOR_CHANGED;
        }
        else if (ae.getSource() == infoColorButton)
        {
            source = INFO_COLOR_CHANGED;
        }
        else if (ae.getSource() == verboseColorButton)
        {
            source = VERBOSE_COLOR_CHANGED;
        }
        else
        {
            source = UNKNOWN_SOURCE;
        }

        return source;
    }

    public boolean logErrors()
    {
        return logErrorsCheck.isSelected();
    }

    public boolean logWarnings()
    {
        return logWarningsCheck.isSelected();
    }

    public boolean logInfo()
    {
        return logInformationalCheck.isSelected();
    }

    public boolean logVerbose()
    {
        return logVerboseCheck.isSelected();
    }

    public Color getErrorColor()
    {
        return errorColorButton.getBackground();
    }

    public Color getWarningColor()
    {
        return warningColorButton.getBackground();
    }

    public Color getInfoColor()
    {
        return infoColorButton.getBackground();
    }

    public Color getVerboseColor()
    {
        return verboseColorButton.getBackground();
    }

    protected void notifyActionListeners(ActionEvent ae)
    {
        List currentList = new ArrayList(actionListeners);
        for (int i = 0; i < currentList.size(); i++)
        {
            ActionListener al = (ActionListener) currentList.get(i);
            al.actionPerformed(ae);
        }
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae)
    {
        switch (getActionSource(ae))
        {
            case ERROR_COLOR_CHANGED :
                errorColorButton.setBackground(
                    JColorChooser.showDialog(selectorPanel, "Select color for displaying error messages", errorColorButton.getBackground()));
                break;
            case WARNING_COLOR_CHANGED :
                warningColorButton.setBackground(
                    JColorChooser.showDialog(selectorPanel, "Select color for displaying warning messages", warningColorButton.getBackground()));
                break;
            case INFO_COLOR_CHANGED :
                infoColorButton.setBackground(
                    JColorChooser.showDialog(selectorPanel, "Select color for displaying informational messages", infoColorButton.getBackground()));
                break;
            case VERBOSE_COLOR_CHANGED :
                verboseColorButton.setBackground(
                    JColorChooser.showDialog(selectorPanel, "Select color for displaying verbose messages", verboseColorButton.getBackground()));
                break;
        }

        notifyActionListeners(ae);
    }

}
