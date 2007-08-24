/**
 * @author wItspirit
 * 28-mrt-2005
 * ThresholdConfigAction.java
 */

package be.vanvlerken.bert.zfpricemgt.gui;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

/**
 * Allows the configuration of the threshold before which records are marked 'old'
 */
public class ThresholdConfigAction extends AbstractAction
{
	private static final long serialVersionUID = 1L;
	
	private static final ResourceBundle msgs = ResourceBundle.getBundle("be.vanvlerken.bert.zfpricemgt.gui.localization.ThresholdConfigAction");
    private JFrame parent;
    private OldPriceDetector oldPriceDetector;
    
    public ThresholdConfigAction(JFrame parent, OldPriceDetector oldPriceDetector)
    {
        super(msgs.getString("title"));
        this.parent = parent;
        this.oldPriceDetector = oldPriceDetector;
    }
    
    public void actionPerformed(ActionEvent ae)
    {
        ThresholdConfigDialog configDialog = new ThresholdConfigDialog(parent);
        configDialog.setOriginalDisplay(oldPriceDetector.isDisplayThreshold());
        configDialog.setOriginalDate(oldPriceDetector.getThresholdDate());
        int status = configDialog.showThresholdConfigDialog();
        if ( status == ThresholdConfigDialog.OK )
        {
            oldPriceDetector.setDisplayThreshold(configDialog.isSelectedDisplay());
            oldPriceDetector.setThresholdDate(configDialog.getSelectedDate());
        }        
    }

}
