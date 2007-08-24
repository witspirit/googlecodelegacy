/**
 * @author wItspirit
 * 15-nov-2003
 * LogDisplay.java
 */

package be.vanvlerken.bert.logmonitor.gui;

import java.awt.Color;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;

import be.vanvlerken.bert.logmonitor.logging.ILogView;

/**
 * Provides a table to display data from a LoggingTableModel
 */
public class LogDisplay implements TableModelListener
{
    private ILogView logView;
    private LoggingTableModel model;

    private JComponent logDisplay;
    private JTable logTable;

    public LogDisplay(ILogView logView)
    {
        commonInit(logView, Color.RED, Color.ORANGE, Color.WHITE, Color.WHITE, true);
    }

    public LogDisplay(ILogView logView, boolean newestAtTheTop)
    {
        commonInit(logView, Color.RED, Color.ORANGE, Color.WHITE, Color.WHITE, newestAtTheTop);
    }

    public LogDisplay(ILogView logView, Color errorColor, Color warningColor, Color infoColor, Color verboseColor)
    {
        commonInit(logView, errorColor, warningColor, infoColor, verboseColor, true);
    }

    public LogDisplay(ILogView logView, Color errorColor, Color warningColor, Color infoColor, Color verboseColor, boolean newestAtTheTop)
    {
        commonInit(logView, errorColor, warningColor, infoColor, verboseColor, newestAtTheTop);
    }

    protected void commonInit(ILogView logView, Color errorColor, Color warningColor, Color infoColor, Color verboseColor, boolean newestAtTheTop)
    {
        this.logView = logView;

        model = new LoggingTableModel(this.logView, true, newestAtTheTop);
        if (!newestAtTheTop)
        {
            model.addTableModelListener(this);
        }
        model.setErrorColor(errorColor);
        model.setWarningColor(warningColor);
        model.setInfoColor(infoColor);
        model.setVerboseColor(verboseColor);

        logDisplay = buildLogDisplay(model);
    }

    /**
     * @return
     */
    protected JComponent buildLogDisplay(LoggingTableModel loggingModel)
    {
        logTable = new JTable(loggingModel);

        /* Initialising some Table properties */
        logTable.setGridColor(Color.lightGray);
        logTable.setShowVerticalLines(false);

        logTable.setDefaultRenderer(SeqNrCell.class, new ColoredLongCellRenderer(ColoredLongCellRenderer.RIGHT));
        logTable.setDefaultRenderer(IdentifierCell.class, new ColoredTextCellRenderer(ColoredTextCellRenderer.CENTER));
        logTable.setDefaultRenderer(TimeStampCell.class, new ColoredTextCellRenderer(ColoredTextCellRenderer.CENTER));
        logTable.setDefaultRenderer(ModuleNameCell.class, new ColoredTextCellRenderer(ColoredTextCellRenderer.LEFT));
        logTable.setDefaultRenderer(MessageCell.class, new ColoredTextCellRenderer(ColoredTextCellRenderer.LEFT));

        /* Initialising the Column widths... */
        TableColumn column = null;

        /* Seq. nr. Column */
        column = logTable.getColumnModel().getColumn(0);
        column.setPreferredWidth(70);

        /* Type Column */
        column = logTable.getColumnModel().getColumn(1);
        column.setPreferredWidth(100);

        /* TimeStamp Column */
        column = logTable.getColumnModel().getColumn(2);
        column.setPreferredWidth(130);

        /* Module Column */
        column = logTable.getColumnModel().getColumn(3);
        column.setPreferredWidth(150);

        /* Message Column */
        column = logTable.getColumnModel().getColumn(4);
        column.setPreferredWidth(2000);

        /* To get a horizontal scrollbar, the autoresize mode should be disabled from the table.
         * Otherwise the table always 'scales' to the available Viewport size.
         * See: http://developer.java.sun.com/developer/qow/archive/50
         */
        logTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JScrollPane scrollPane = new JScrollPane(logTable);

        return scrollPane;
    }

    public JComponent getDisplay()
    {
        return logDisplay;
    }

    public LoggingTableModel getLoggingTableModel()
    {
        return model;
    }

    public ILogView getLogView()
    {
        return logView;
    }

    /**
     * Returns the formatted.
     * @return boolean
     */
    public boolean isFormatted()
    {
        return model.isFormatted();
    }

    /**
     * Returns the errorColor.
     * @return Color
     */
    public Color getErrorColor()
    {
        return model.getErrorColor();
    }

    /**
     * Returns the infoColor.
     * @return Color
     */
    public Color getInfoColor()
    {
        return model.getInfoColor();
    }

    /**
     * Returns the verboseColor.
     * @return Color
     */
    public Color getVerboseColor()
    {
        return model.getVerboseColor();
    }

    /**
     * Returns the warningColor.
     * @return Color
     */
    public Color getWarningColor()
    {
        return model.getWarningColor();
    }

    /**
     * Sets the errorColor.
     * @param errorColor The errorColor to set
     */
    public void setErrorColor(Color errorColor)
    {
        model.setErrorColor(errorColor);
    }

    /**
     * Sets the infoColor.
     * @param infoColor The infoColor to set
     */
    public void setInfoColor(Color infoColor)
    {
        model.setInfoColor(infoColor);
    }

    /**
     * Sets the verboseColor.
     * @param verboseColor The verboseColor to set
     */
    public void setVerboseColor(Color verboseColor)
    {
        model.setVerboseColor(verboseColor);
    }

    /**
     * Sets the warningColor.
     * @param warningColor The warningColor to set
     */
    public void setWarningColor(Color warningColor)
    {
        model.setWarningColor(warningColor);
    }

    public boolean isNewestAtTheTop()
    {
        return model.isNewestAtTheTop();
    }

    public void setNewestAtTheTop(boolean newestAtTheTop)
    {
        if (newestAtTheTop != model.isNewestAtTheTop())
        {
            model.setNewestAtTheTop(newestAtTheTop);
            if (newestAtTheTop)
            {
                model.removeTableModelListener(this);
                logTable.scrollRectToVisible(logTable.getCellRect(0,0,true));
            }
            else
            {
                model.addTableModelListener(this);
            }
        }
    }

    /**
     * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
     */
    public void tableChanged(TableModelEvent tme)
    {
        if (logTable != null && tme.getType() == TableModelEvent.INSERT)
        {
            logTable.scrollRectToVisible(logTable.getCellRect(tme.getLastRow(), 0, true));
        }
    }
}
