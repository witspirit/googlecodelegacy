/**
 * @author wItspirit
 * 16-nov-2003
 * ModuleList.java
 */

package be.vanvlerken.bert.logmonitor.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import be.vanvlerken.bert.logmonitor.logging.ILogView;


/**
 * This component takes infeed from a logView and displays all different moduleNames
 * and offers a selection mechanism to select one or more modules
 */
public class ModuleList implements ListSelectionListener
{
    private JComponent  display;
    private JList  jList;
    private ModuleListModel model;
    private String[]    selectedModules;
    
    private List        listeners;
    
    public ModuleList(ILogView logView)
    {
        model = new ModuleListModel(logView);
        selectedModules = new String[0];
        
        listeners = new ArrayList();
        
        display = buildList(model);
    }

    /**
     * @return
     */
    private JComponent buildList(ModuleListModel listModel)
    {
        jList = new JList(listModel);
        //jList.set(new Dimension(150,0)); This is NOT the way to go - this will render the ScrollPane useless
        jList.addListSelectionListener(this);        
        JScrollPane scrollPane = new JScrollPane(jList);        
        // scrollPane.setPreferredSize(new Dimension(150,0));         
        return scrollPane;
    }
    
    public JComponent getListDisplay()
    {
        return display;
    }

    public void addListSelectionListener(ListSelectionListener listener)
    {
        listeners.add(listener);
    }
    
    protected void notifyListeners(ListSelectionEvent lse)
    {
        List current = new ArrayList(listeners);
        for (int i=0; i < current.size(); i++ )
        {
            ListSelectionListener listener = (ListSelectionListener) listeners.get(i);
            listener.valueChanged(lse);
        }
    }

    /**
     * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    public void valueChanged(ListSelectionEvent lse)
    {
        if ( lse.getValueIsAdjusting() == false )   /* Only when user has 'calmed down' we take action */
        {
            Object[] selectedValues = jList.getSelectedValues();
            selectedModules = new String[selectedValues.length];
            for (int i=0; i < selectedValues.length; i++)
            {
                selectedModules[i] = (String) selectedValues[i];
            }
            notifyListeners(lse);
        }        
    }
    
    /**
     * @return
     */
    public String[] getSelectedModules()
    {        
        return selectedModules;
    }
}
