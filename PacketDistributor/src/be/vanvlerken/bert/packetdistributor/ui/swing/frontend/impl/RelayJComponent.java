/**
 * @author wItspirit
 * 28-apr-2003
 * RelayJComponent.java
 */
package be.vanvlerken.bert.packetdistributor.ui.swing.frontend.impl;

import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Provides an input component for a DummyTrafficRelay
 * A DummyTrafficRelay is identified by a unique number (which is allowed to be null) and a
 * name that is used for easy identification in the GUI
 */
public class RelayJComponent extends JPanel
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3256446906254503992L;
    private JTextField  idField;
    private JTextField  nameField;
    
    public RelayJComponent()
    {
        setupComponent();
    }

    /**
     * Method setupComponent.
     */
    private void setupComponent()
    {
        idField = new JTextField(5);
        idField.setEditable(false);
        nameField = new JTextField(20);
        
        add(idField);
        add(nameField);
    }
    
    public int getId()
    {
        return Integer.parseInt(idField.getText());
    }
    
    public String getName()
    {
        return nameField.getText();
    }
    
    public void setId(int id)
    {
        idField.setText(Integer.toString(id));
    }

    public void setName(String name)
    {
        nameField.setText(name);
    }

    public void clearFields()
    {
        idField.setText("");
        nameField.setText("");
    }

}
