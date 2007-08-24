/**
 * @author wItspirit
 * 8-feb-2003
 * ColoredTextCellRenderer.java
 */
package be.vanvlerken.bert.logmonitor.gui;

import java.awt.Component;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Renders a MessageCell
 */
public class ColoredTextCellRenderer extends JLabel implements TableCellRenderer
{
    public ColoredTextCellRenderer(int alignment)
    {
        super();
        setHorizontalAlignment(alignment);
        setFont(new Font("courier new",Font.PLAIN,12));
    }

	/**
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(
		JTable table,
		Object value,
		boolean isSelected,
		boolean hasFocus,
		int row,
		int column)
	{
        IColoredTextCell cell = (IColoredTextCell) value;
        
        setBackground(cell.getColor());
        setText(cell.getText());
        
		return this;
	}

    /**
     * Overrides for performance reasons
     * @see javax.swing.table.DefaultTableCellRenderer
     * @see java.awt.Component#firePropertyChange(java.lang.String, boolean, boolean)
     */
    public void firePropertyChange(String arg0, boolean arg1, boolean arg2)
    {
        /* Do nothing for performance reasons */
    }

    /**
     * Overrides for performance reasons
     * @see javax.swing.table.DefaultTableCellRenderer
     * @see java.awt.Component#firePropertyChange(java.lang.String, java.lang.Object, java.lang.Object)
     */
    protected void firePropertyChange(String arg0, Object arg1, Object arg2)
    {
        /* Do nothing for performance reasons */
    }

    /**
     * Overrides for performance reasons
     * @see javax.swing.table.DefaultTableCellRenderer
     * @see java.awt.Component#isOpaque()
     */
    public boolean isOpaque()
    {
        return true;
    }

    /**
     * Overrides for performance reasons
     * @see javax.swing.table.DefaultTableCellRenderer
     * @see java.awt.Component#repaint(long, int, int, int, int)
     */
    public void repaint(long arg0, int arg1, int arg2, int arg3, int arg4)
    {
        /* Do nothing for performance reasons */
    }

    /**
     * Overrides for performance reasons
     * @see javax.swing.table.DefaultTableCellRenderer
     * @see javax.swing.JComponent#repaint(java.awt.Rectangle)
     */
    public void repaint(Rectangle arg0)
    {
        /* Do nothing for performance reasons */
    }

    /**
     * Overrides for performance reasons
     * @see javax.swing.table.DefaultTableCellRenderer
     * @see javax.swing.JComponent#revalidate()
     */
    public void revalidate()
    {
        /* Do nothing for performance reasons */
    }

    /**
     * Overrides for performance reasons
     * @see javax.swing.table.DefaultTableCellRenderer
     * @see java.awt.Component#validate()
     */
    public void validate()
    {
        /* Do nothing for performance reasons */
    }

}
