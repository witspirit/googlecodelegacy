/**
 * @author wItspirit
 * 30-jan-2005
 * LookupHistoryColumnModel.java
 */

package be.vanvlerken.bert.zfpricemgt.gui;

import java.util.ResourceBundle;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;


/**
 * A specific implementation of the TableColumnModel to influence the formatting 
 * of the columns in the LookupHistoryTableModel
 */
public class LookupHistoryColumnModel extends DefaultTableColumnModel
{
	private static final long serialVersionUID = 1L;

	private static final ResourceBundle msgs = ResourceBundle.getBundle("be.vanvlerken.bert.zfpricemgt.gui.localization.LookupHistoryColumnModel");
    
    private static final int PRODUCT_NUMBER = 0;
    private static final int DESCRIPTION = 1;
    private static final int PRICE = 2;
    private static final int VALID_SINCE = 3;
    
    private static final String[] columnNames = {msgs.getString("product.nr.column"),
                                                 msgs.getString("description.column"),
                                                 msgs.getString("price.column"),
                                                 msgs.getString("valid.since.column")};
    
    public LookupHistoryColumnModel()
    {
        super();
        DefaultTableCellRenderer rightAlignRenderer = new DefaultTableCellRenderer();
        rightAlignRenderer.setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
        
        TableColumn[] columns = new TableColumn[4];
        columns[PRODUCT_NUMBER] = new TableColumn(PRODUCT_NUMBER, 50);
        columns[DESCRIPTION] = new TableColumn(DESCRIPTION, 100);
        columns[PRICE] = new TableColumn(PRICE, 40, rightAlignRenderer, null);
        columns[VALID_SINCE] = new TableColumn(VALID_SINCE, 50, rightAlignRenderer, null);
        
        for (int i=0; i < columns.length; i++)
        {
            TableColumn column = columns[i];
            column.setHeaderValue(columnNames[i]);
            addColumn(column);
        }
    }    
}
