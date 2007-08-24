/**
 * @author vlerkenb
 * 5-feb-2003
 * LoggingTableModel.java
 */
package be.vanvlerken.bert.logmonitor.gui;

import java.awt.Color;
import javax.swing.table.AbstractTableModel;

import be.vanvlerken.bert.logmonitor.logging.IDatabaseListener;
import be.vanvlerken.bert.logmonitor.logging.ILogEntry;
import be.vanvlerken.bert.logmonitor.logging.ILogView;

/**
 * Provides a table model of our data
 */
public class LoggingTableModel extends AbstractTableModel implements IDatabaseListener
{
    private ILogView logDb;

    private SeqNrCell seqNrCell;
    private IdentifierCell idCell;
    private TimeStampCell timeStampCell;
    private ModuleNameCell moduleNameCell;
    private MessageCell messageCell;

    private boolean formatted;
    private boolean newestAtTheTop;

    private Color errorColor;
    private Color warningColor;
    private Color infoColor;
    private Color verboseColor;

    /**
     * Constructor for LoggingTableModel.
     */
    public LoggingTableModel(ILogView logDb, boolean formatted, boolean newestAtTheTop)
    {
        super();
        this.logDb = logDb;
        logDb.addDatabaseListener(this);

        this.formatted = formatted;
        this.newestAtTheTop = newestAtTheTop;

        seqNrCell = new SeqNrCell();
        idCell = new IdentifierCell();
        timeStampCell = new TimeStampCell();
        moduleNameCell = new ModuleNameCell();
        messageCell = new MessageCell();

        errorColor = Color.red;
        warningColor = Color.orange;
        infoColor = Color.white;
        verboseColor = Color.white;
    }

    /**
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount()
    {
        return logDb.getSize();
    }

    /**
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount()
    {
        return 5;
    }

    /**
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        ILogEntry logEntry;

        if (newestAtTheTop)
        {
            logEntry = logDb.get((logDb.getSize() - 1) - rowIndex);
        }
        else
        {
            logEntry = logDb.get(rowIndex);
        }

        if (formatted)
        {
            Color color = selectColor(logEntry.getIdentifier());

            switch (columnIndex)
            {
                case 0 :
                    seqNrCell.setSequenceNr(logEntry.getSequenceNr());
                    seqNrCell.setColor(color);
                    return seqNrCell;
                case 1 :
                    idCell.setIdentifier(logEntry.getIdentifier());
                    idCell.setColor(color);
                    return idCell;
                case 2 :
                    timeStampCell.setTimeStamp(logEntry.getTimeStamp());
                    timeStampCell.setColor(color);
                    return timeStampCell;
                case 3 :
                    moduleNameCell.setModuleName(logEntry.getModuleName());
                    moduleNameCell.setColor(color);
                    return moduleNameCell;
                case 4 :
                    messageCell.setMessage(logEntry.getMessage());
                    messageCell.setColor(color);
                    return messageCell;
                default :
                    return null;
            }
        }
        else
        {
            switch (columnIndex)
            {
                case 0 :
                    seqNrCell.setSequenceNr(logEntry.getSequenceNr());
                    return seqNrCell.toString();
                case 1 :
                    idCell.setIdentifier(logEntry.getIdentifier());
                    return idCell.toString();
                case 2 :
                    timeStampCell.setTimeStamp(logEntry.getTimeStamp());
                    return timeStampCell.toString();
                case 3 :
                    moduleNameCell.setModuleName(logEntry.getModuleName());
                    return moduleNameCell.toString();
                case 4 :
                    messageCell.setMessage(logEntry.getMessage());
                    return messageCell.toString();
                default :
                    return null;
            }
        }
    }

    /**
     * Method selectColor.
     * @param i
     * @return Color
     */
    private Color selectColor(int i)
    {
        Color color;

        /* First level - The error level */
        switch (i)
        {
            case ILogEntry.ERROR :
                color = errorColor;
                break;
            case ILogEntry.WARNING :
                color = warningColor;
                break;
            case ILogEntry.INFO :
                color = infoColor;
                break;
            case ILogEntry.VERBOSE :
                color = verboseColor;
                break;
            default :
                color = Color.white;
        }

        return color;
    }

    /**
     * @see javax.swing.table.TableModel#isCellEditable(int, int)
     */
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
    }

    /**
     * @see javax.swing.table.TableModel#getColumnClass(int)
     */
    public Class getColumnClass(int columnIndex)
    {
        if (formatted)
        {
            switch (columnIndex)
            {
                case 0 :
                    return SeqNrCell.class;
                case 1 :
                    return IdentifierCell.class;
                case 2 :
                    return TimeStampCell.class;
                case 3 :
                    return ModuleNameCell.class;
                case 4 :
                    return MessageCell.class;
                default :
                    return String.class;
            }
        }
        else
        {
            return String.class;
        }
    }

    /**
     * @see javax.swing.table.TableModel#getColumnName(int)
     */
    public String getColumnName(int columnIndex)
    {
        switch (columnIndex)
        {
            case 0 :
                return "Seq. Nr";
            case 1 :
                return "Type";
            case 2 :
                return "TimeStamp";
            case 3 :
                return "Module";
            case 4 :
                return "Message";
            default :
                return "Unknown column";
        }
    }

    /**
     * @see be.vanvlerken.bert.logmonitor.IDatabaseListener#entryAdded(be.vanvlerken.bert.logmonitor.ILogEntry)
     */
    public void entryAdded(ILogEntry entry)
    {
        fireTableRowsInserted(logDb.getSize() - 1, logDb.getSize());
    }

    /**
     * @see be.vanvlerken.bert.logmonitor.IDatabaseListener#entriesModified(int, int)
     */
    public void entriesModified(int startIndex, int endIndex)
    {
        fireTableRowsUpdated(startIndex, endIndex);
    }

    /**
     * @see be.vanvlerken.bert.logmonitor.IDatabaseListener#databaseModified()
     */
    public void databaseModified()
    {
        fireTableDataChanged();
    }
    /**
     * Returns the formatted.
     * @return boolean
     */
    public boolean isFormatted()
    {
        return formatted;
    }

    /**
     * Returns the errorColor.
     * @return Color
     */
    public Color getErrorColor()
    {
        return errorColor;
    }

    /**
     * Returns the infoColor.
     * @return Color
     */
    public Color getInfoColor()
    {
        return infoColor;
    }

    /**
     * Returns the verboseColor.
     * @return Color
     */
    public Color getVerboseColor()
    {
        return verboseColor;
    }

    /**
     * Returns the warningColor.
     * @return Color
     */
    public Color getWarningColor()
    {
        return warningColor;
    }

    /**
     * Sets the errorColor.
     * @param errorColor The errorColor to set
     */
    public void setErrorColor(Color errorColor)
    {
        this.errorColor = errorColor;
    }

    /**
     * Sets the infoColor.
     * @param infoColor The infoColor to set
     */
    public void setInfoColor(Color infoColor)
    {
        this.infoColor = infoColor;
    }

    /**
     * Sets the verboseColor.
     * @param verboseColor The verboseColor to set
     */
    public void setVerboseColor(Color verboseColor)
    {
        this.verboseColor = verboseColor;
    }

    /**
     * Sets the warningColor.
     * @param warningColor The warningColor to set
     */
    public void setWarningColor(Color warningColor)
    {
        this.warningColor = warningColor;
    }
    
    /**
     * @return
     */
    public boolean isNewestAtTheTop()
    {
        return newestAtTheTop;
    }

    /**
     * @param newestAtTheTop
     */
    public void setNewestAtTheTop(boolean newestAtTheTop)
    {
        this.newestAtTheTop = newestAtTheTop;
        databaseModified();
    }

}
