/**
 * @author wItspirit
 * 5-mei-2005
 * DBConfigurator.java
 */

package be.vanvlerken.bert.zfpricemgt.database;

import javax.swing.JFrame;

/**
 * This is the interface for a Database Configurator The responsibilities of the
 * DBConfigurator are: 1. Provide a graphical dialog to configure whatever
 * settings are required for this db implementation 2. Interact with the DB
 * interface implementation in order to enforce configuration changes 3. Store
 * the configuration persistently, such that it will retain over application
 * restarts
 * 
 * @see IZFPriceDatabase#getConfigurator(JFrame parent)
 */
public interface DBConfigurator
{
    /**
     * Shows a dialog to configure the DB
     * @param parent
     *            The JFrame that is used as the parent of the graphical part of
     *            the configurator
     */
    public void showConfigurator(JFrame parent);
}
