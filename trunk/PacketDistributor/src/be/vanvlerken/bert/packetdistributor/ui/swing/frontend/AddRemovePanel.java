/**
 * @author wItspirit
 * 27-apr-2003
 * AddRemovePanel.java
 */
package be.vanvlerken.bert.packetdistributor.ui.swing.frontend;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Generic panel that displays a list of items and shows an Add and Remove button
 * The Add/Remove buttons will trigger an event on the selected item.
 */
public class AddRemovePanel extends JPanel implements ActionListener, ListSelectionListener
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3257005453798946611L;
    private static final Log log = LogFactory.getLog(AddRemovePanel.class);

    private AddRemoveModel  model;
    
    private JList           listBox;
    private JButton         addButton;
    private JButton         removeButton;
    
    public AddRemovePanel(AddRemoveModel model)
    {
        this.model = model;
        
        drawPanel();
    }

    /**
     * Method drawPanel.
     */
    private void drawPanel()
    {
        setLayout(new BorderLayout());
        
        Border etchedBorder = BorderFactory.createEtchedBorder();
        Border titledBorder = BorderFactory.createTitledBorder(etchedBorder, model.getTitle());
        setBorder(titledBorder);
        
        listBox = new JList(model.getListModel());
        listBox.addListSelectionListener(this);
        JScrollPane scrollPane = new JScrollPane(listBox);
        
        addButton = new JButton("Add");
        addButton.addActionListener(this);
        
        removeButton = new JButton("Remove");
        removeButton.addActionListener(this);
        
        JPanel buttonPanel = new JPanel(new GridLayout(1,2));
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(model.getInputComponent(), BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
     */
    public void actionPerformed(ActionEvent ae)
    {
        if ( !model.isActive() )
        {
            log.debug("Model is not active");
            JOptionPane.showMessageDialog(this, "This component is not yet active.", "Not active", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if ( ae.getSource() == addButton )
        {
            log.debug("Adding Element");
            model.add();
        }
        else if ( ae.getSource() == removeButton )
        {
            int selectedIndex = listBox.getSelectedIndex();
            if ( selectedIndex == -1 )
            {
                JOptionPane.showMessageDialog(this, "Please select a "+model.getElementName(), "No "+model.getElementName()+" selected", JOptionPane.ERROR_MESSAGE);
                return;
            }
            log.debug("Removing Element");
            model.remove(selectedIndex);
        }
        log.debug("Propagating new selection");
        newSelectionActivated();
    }

    /**
     * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    public void valueChanged(ListSelectionEvent lse)
    {
        newSelectionActivated();
    }
    
    private void newSelectionActivated()
    {
        int selectedIndex = listBox.getSelectedIndex();
        // Adjust selectedIndex when out of bounds
        int modelSize = model.getListModel().getSize();
        if (modelSize == 0 )
        {
            // There is nothing to select !
            log.debug("There is nothing to select");
            selectedIndex = -1;
        }
        else if ( modelSize <= selectedIndex )
        {
            selectedIndex = modelSize - 1;
            listBox.setSelectedIndex(selectedIndex);
        }
        log.debug("New selected element = "+selectedIndex);
        if ( selectedIndex == -1 )
        {
            removeButton.setEnabled(false);
        }
        else
        {
            removeButton.setEnabled(true);
        }
        model.selected(selectedIndex);
    }

}
