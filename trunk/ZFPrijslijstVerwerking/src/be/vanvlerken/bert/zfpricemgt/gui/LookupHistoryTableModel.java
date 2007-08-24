/**
 * @author wItspirit
 * 11-jan-2004
 * LookupHistoryTableModel.java
 */

package be.vanvlerken.bert.zfpricemgt.gui;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.table.AbstractTableModel;

import be.vanvlerken.bert.zfpricemgt.IProduct;


/**
 * Contains the lookup history of previous price lookups
 */
public class LookupHistoryTableModel extends AbstractTableModel
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3258126942791087927L;
    private List<IProduct>    products;
        
    public LookupHistoryTableModel()
    {
        products = new ArrayList<IProduct>();
    }
    
    
    public void addProduct(IProduct product)
    {
        products.add(product);
        fireTableRowsInserted(products.size()-1, products.size());
    }
    
    public IProduct getProduct(int index)
    {
        return products.get(index);
    }
    
    public void clearHistory()
    {
        int size = products.size();
        products.clear();
        this.fireTableRowsDeleted(0, size);
    }

    /**
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount()
    {
        return products.size();
    }

    /**
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount()
    {
        return 4;
    }

    /**
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int column)
    {
        Object value = null;
        IProduct product = products.get(row);
        
        switch ( column )
        {
            case 0: value = product.getNumber(); break;
            case 1: value = product.getDescription(); break;
            case 2: value = Double.toString(product.getPrice()); break;
            case 3:
                DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRANCE);
                value = df.format(product.getValidSince()); break;
        }
        
        return value;
    }    
}
