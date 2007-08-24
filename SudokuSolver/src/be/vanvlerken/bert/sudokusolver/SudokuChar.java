/**
 * @author wItspirit
 * 8-jan-2006
 * SudokuChar.java
 */

package be.vanvlerken.bert.sudokusolver;

public class SudokuChar
{
    public static final int EMPTY = 0;
    private int currentValue;
    // SudokuChar's know their own positions !
    private int row;
    private int column;
    
    public SudokuChar(int row, int column)
    {
        this.row = row;
        this.column = column;
        reset();
    }    
    
    public int getRow()
    {
        return row;
    }
    
    public int getColumn()
    {
        return column;
    }

    /**
     * This method resets the character back to it's defaults: EMPTY, all available
     */
    public void reset()
    {
        currentValue = EMPTY;
    }
    
    public int getValue()
    {
        return currentValue;
    }
    
    public boolean isFixed()
    {
        return currentValue != EMPTY;
    }
    
    /**
     * Give this character a determined value. Will set the currentValue and will set possibleValues to false (except this one)
     * @param value Number from 1 to 9
     */
    public void setValue(int value)
    {
        currentValue = value;
    }
}
