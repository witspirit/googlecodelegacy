/**
 * @author wItspirit
 * 8-jan-2006
 * SudokuSquare.java
 */

package be.vanvlerken.bert.sudokusolver;


public class SudokuSquare extends SudokuGroup
{
    public SudokuSquare(SudokuChar[][] sudoku, int row, int column)
    {
        for (int i=0; i < 9; i++)
            entries[i] = sudoku[row*3+i/3][column*3+i%3];
    }
     
    public SudokuChar getEntry(int row, int column)
    {
        return entries[row*3+column];
    }        
}
