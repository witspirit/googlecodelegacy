/**
 * @author wItspirit
 * 27-apr-2003
 * AddRemoveModel.java
 */
package be.vanvlerken.bert.packetdistributor.ui.swing.frontend;

import javax.swing.JComponent;
import javax.swing.ListModel;

/**
 * The model that should be implemented for displaying data via the AddRemovePanel
 */
public interface AddRemoveModel
{
    public String getTitle();
    public String getElementName();
    public ListModel getListModel();
    public JComponent getInputComponent();
    public void add();
    public void remove(int selectedItem);
    public void selected(int selectedItem);
    public boolean isActive();    
}
