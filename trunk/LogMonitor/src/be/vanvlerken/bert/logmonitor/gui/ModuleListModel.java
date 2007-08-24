/**
 * @author wItspirit
 * 16-nov-2003
 * ModuleListModel.java
 */

package be.vanvlerken.bert.logmonitor.gui;

import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.AbstractListModel;

import be.vanvlerken.bert.logmonitor.logging.IDatabaseListener;
import be.vanvlerken.bert.logmonitor.logging.ILogEntry;
import be.vanvlerken.bert.logmonitor.logging.ILogView;

/**
 * Provides a dynamic ListModel for the JList of the ModuleList
 */
public class ModuleListModel extends AbstractListModel implements IDatabaseListener
{
    private ILogView logView;
    private SortedMap moduleMap;
    
    private String[]    keyCache;

    public ModuleListModel(ILogView logView)
    {
        this.logView = logView;
        moduleMap = new TreeMap();        
        keyCache = new String[0];

        load();
        logView.addDatabaseListener(this);
    }

    /**
     * Goes over all values currently in logView and extracts all distinct moduleNames
     */
    private void load()
    {
        moduleMap.clear();
        for (int i = 0; i < logView.getSize(); i++)
        {
            ILogEntry entry = logView.get(i);
            if (!moduleMap.containsKey(entry.getModuleName()))
            {
                moduleMap.put(entry.getModuleName(), null);
            }
        }
        updateSet();
    }

    /**
     * @see javax.swing.ListModel#getSize()
     */
    public int getSize()
    {
        return keyCache.length;
    }

    /**
     * @see javax.swing.ListModel#getElementAt(int)
     */
    public Object getElementAt(int index)
    {
        return keyCache[index];
    }

    /**
     * @see be.vanvlerken.bert.logmonitor.logging.IDatabaseListener#entryAdded(be.vanvlerken.bert.logmonitor.logging.ILogEntry)
     */
    public void entryAdded(ILogEntry entry)
    {
        if (!moduleMap.containsKey(entry.getModuleName()))
        {
            moduleMap.put(entry.getModuleName(), null);
            updateSet();
        }
    }

    /**
     * Updates the cache of keys currently stored. This is necessary
     * as generally frequent display of sparsely updating content
     * involves quite heavy operations on the map which is not really suited
     * to random access.
     */
    private void updateSet()
    {
        // int currentSize = keyCache.length;
        Set keys = moduleMap.keySet();
        keyCache = new String[0];
        keyCache = (String[]) keys.toArray(keyCache);
        this.fireContentsChanged(this, 0, keyCache.length);
        // int newSize = keyCache.length;
        
//        if ( newSize > currentSize )
//        {
//            this.fireContentsChanged(this, 0, currentSize);
//            this.fireIntervalAdded(this, currentSize, newSize);
//        }
//        else if ( newSize < currentSize )
//        {
//            this.fireContentsChanged(this, 0, newSize);
//            this.fireIntervalRemoved(this, newSize, currentSize);
//        }
//        else
//        {
//            this.fireContentsChanged(this, 0, newSize);
//        }
    }

    /**
     * @see be.vanvlerken.bert.logmonitor.logging.IDatabaseListener#entriesModified(int, int)
     */
    public void entriesModified(int startIndex, int endIndex)
    {
        load();
    }

    /**
     * @see be.vanvlerken.bert.logmonitor.logging.IDatabaseListener#databaseModified()
     */
    public void databaseModified()
    {
        load();
    }

}
