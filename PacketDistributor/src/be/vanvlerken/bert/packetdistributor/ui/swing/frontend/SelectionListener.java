/**
 * @author wItspirit
 * 13-mei-2003
 * SelectionListener.java
 */

package be.vanvlerken.bert.packetdistributor.ui.swing.frontend;

import java.util.EventListener;


/**
 * Provides an interface to get notified of selection changes
 */
public interface SelectionListener extends EventListener
{
    public void selected(SelectionEventObject seo);
}
