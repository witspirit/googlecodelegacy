/**
 * @author wItspirit
 * 26-okt-2003
 * IColoredLongCell.java
 */

package be.vanvlerken.bert.logmonitor.gui;

import java.awt.Color;

/**
 * description
 */
public interface IColoredLongCell
{
    /**
     * Returns the color.
     * @return Color
     */
    public abstract Color getColor();
    /**
     * Returns the value.
     * @return int
     */
    public abstract long getValue();
}