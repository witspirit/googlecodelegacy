/**
 * @author wItspirit
 * 8-feb-2003
 * SeqNrCell.java
 */
package be.vanvlerken.bert.logmonitor.gui;

import java.awt.Color;

/**
 * Container for SeqNr associated with some extra properties
 */
public class SeqNrCell implements IColoredLongCell
{
    private long sequenceNr;
    private Color color;
    
    public SeqNrCell()
    {
        this.sequenceNr = 0;
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
	 * Returns the sequenceNr.
	 * @return int
	 */
	public long getValue()
	{
		return sequenceNr;
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
	 * Sets the sequenceNr.
	 * @param sequenceNr The sequenceNr to set
	 */
	public void setSequenceNr(long value)
	{
		this.sequenceNr = value;
	}
    
    public String toString()
    {
        return (new Long(sequenceNr)).toString();
    }
}
