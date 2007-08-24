/**
 * @author wItspirit
 * 26-okt-2003
 * IColoredTextCell.java
 */

package be.vanvlerken.bert.logmonitor.gui;

import java.awt.Color;

/**
 * description
 */
public interface IColoredTextCell
{
    /**
     * Returns the color.
     * @return Color
     */
    public abstract Color getColor();
    /**
     * Returns the text.
     * @return String
     */
    public abstract String getText();
}