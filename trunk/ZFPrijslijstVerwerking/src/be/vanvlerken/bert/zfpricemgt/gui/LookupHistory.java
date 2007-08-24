/**
 * @author wItspirit
 * 30-jan-2005
 * LookupHistory.java
 */

package be.vanvlerken.bert.zfpricemgt.gui;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import be.vanvlerken.bert.zfpricemgt.IProduct;

/**
 * Provides a narrow API towards a JTable implementation for displaying a lookup
 * history
 */
public class LookupHistory implements ListSelectionListener
{
    private LookupHistoryTableModel        tableModel;
    private LookupHistoryColumnModel       columnModel;
    private ListSelectionModel             selectionModel;
    private JTable                         table;
    private int                            lastSelectedIndex = -1;
    private List<ProductSelectionListener> productSelectionListeners;

    public LookupHistory()
    {
        productSelectionListeners = new LinkedList<ProductSelectionListener>();

        tableModel = new LookupHistoryTableModel();
        columnModel = new LookupHistoryColumnModel();
        
        table = new JTable(tableModel, columnModel);
        table.setCellSelectionEnabled(false);
        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectionModel = table.getSelectionModel();
        selectionModel.addListSelectionListener(this);
    }

    public JTable getTable()
    {
        return table;
    }

    public void addProduct(IProduct product)
    {
        tableModel.addProduct(product);
        int index = table.getRowCount() - 1;
        // Required to avoid that we send a spurious update of the selected Row
        // The check for -1 is to cover the case where there has been a clearHistory, followed
        // by an addProduct. 
        // Otherwise, we would have lastSelectedIndex = 0 and the selectionEvent would contain 0 as
        // well, although there has been a change.
        if (lastSelectedIndex != -1)
        {
            lastSelectedIndex = index;
        }
        table.setRowSelectionInterval(index, index);
        table.scrollRectToVisible(table.getCellRect(index,0,true)); // Auto-scroll mode
    }

    public void clearHistory()
    {
        // Required to avoid that we send a spurious update of the selected Row
        lastSelectedIndex = -1;
        tableModel.clearHistory();
    }

    /**
     * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    public void valueChanged(ListSelectionEvent selectionEvent)
    {
        int selectedIndex = table.getSelectedRow();

        if (selectedIndex != lastSelectedIndex)
        {
            lastSelectedIndex = selectedIndex;
            // System.out.println("Selected lookupHistory row = " +
            // selectedIndex);
            fireProductSelectionEvent(tableModel.getProduct(selectedIndex));
        }
    }

    public void addProductSelectionListener(ProductSelectionListener listener)
    {
        synchronized (productSelectionListeners)
        {
            productSelectionListeners.add(listener);
        }
    }

    public void removeProductSelectionListener(ProductSelectionListener listener)
    {
        synchronized (productSelectionListeners)
        {
            productSelectionListeners.remove(listener);
        }
    }

    protected void fireProductSelectionEvent(IProduct product)
    {
        List<ProductSelectionListener> stableListeners;
        synchronized (productSelectionListeners)
        {
            stableListeners = new LinkedList<ProductSelectionListener>(productSelectionListeners);
        }
        for (ProductSelectionListener listener : stableListeners)
        {
            listener.selectedProduct(product);
        }
    }
}
