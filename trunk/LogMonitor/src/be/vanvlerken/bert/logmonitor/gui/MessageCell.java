/**
 * @author wItspirit
 * 8-feb-2003
 * MessageCell.java
 */
package be.vanvlerken.bert.logmonitor.gui;

import java.awt.Color;

/**
 * Container for a text with some extra properties
 */
public class MessageCell implements IColoredTextCell
{
    private String message;
    private Color color;
    
    public MessageCell()
    {
        this.message = "";
        this.color = Color.white;
    }

	/**
	 * Returns the color.
	 * @return Color
	 */
	public Color getColor()
	{
		return color;
	}

	/**
	 * Returns the text.
	 * @return String
	 */
	public String getText()
	{
		return message;
	}

	/**
	 * Sets the color.
	 * @param color The color to set
	 */
	public void setColor(Color color)
	{
		this.color = color;
	}

	/**
	 * Sets the text.
	 * @param text The text to set
	 */
	public void setMessage(String message)
	{
		this.message = message;
	}
    
    public String toString()
    {
        return message;
    }

}
