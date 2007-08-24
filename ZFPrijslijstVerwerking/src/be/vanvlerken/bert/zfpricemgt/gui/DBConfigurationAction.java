/**
 * @author wItspirit
 * 5-mei-2005
 * DBConfigurationAction.java
 */

package be.vanvlerken.bert.zfpricemgt.gui;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

import be.vanvlerken.bert.zfpricemgt.database.DBConfigurator;

/**
 * Allows the configuration of the DB if required
 */
public class DBConfigurationAction extends AbstractAction
{    
	private static final long serialVersionUID = 1L;

	private static final ResourceBundle msgs = ResourceBundle.getBundle("be.vanvlerken.bert.zfpricemgt.gui.localization.DBConfigurationAction");
    
    private JFrame parent;
    private DBConfigurator dbConfigurator;
    
    public DBConfigurationAction(JFrame parent, DBConfigurator dbConfigurator)
    {
        super(msgs.getString("title"));
        this.parent = parent;
        this.dbConfigurator = dbConfigurator;
        
        if ( dbConfigurator == null )
        {
            this.setEnabled(false);
        }
    }
    
    public void actionPerformed(ActionEvent ae)
    {
        dbConfigurator.showConfigurator(parent);
    }

}
