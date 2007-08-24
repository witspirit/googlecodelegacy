/**
 * @author wItspirit
 * 8-jan-2006
 * SudokuGroup.java
 */

package be.vanvlerken.bert.sudokusolver;

import java.util.LinkedList;
import java.util.List;


public abstract class SudokuGroup
{
    protected SudokuChar[] entries;
    
    public SudokuGroup()
    {
        entries = new SudokuChar[9];
    }
    
    public SudokuChar getEntry(int index)
    {
        return entries[index];
    }
    
    public List<SudokuChar> getFreeEntries()
    {
        List<SudokuChar> freeEntries = new LinkedList<SudokuChar>();
        for (int i=0; i < 9; i++)
        {
            // If the entry is not yet fixed, then it is Free :-)
            if ( !entries[i].isFixed() )
            {
                freeEntries.add(entries[i]);
            }       
        }
        return freeEntries;
    }
    
    public boolean isValid()
    {
        int[] contained = countValues();
        
        // Validity check phase - We verify that there is never more than 1 for each number
        for (int i=1; i < 10; i++) // We don't check the empty fields
        {
            if ( contained[i] > 1 )
            {
                return false;
            }
        }
        return true;
    }

    /**
     * @return
     */
    private int[] countValues()
    {
        int[] contained = new int[10];
        // Set to zero - redundant, but clean
        for (int i=0; i < 10; i++)
            contained[i] = 0;
        
        // Count phase - We count all numbers contained
        for (int i=0; i < 9; i++)
            contained[entries[i].getValue()]++;
        return contained;
    }
    
    public boolean contains(int value)
    {
        int[] contained = countValues();
        if ( contained[value] > 0 )
            return true;
        return false;
    }
}
