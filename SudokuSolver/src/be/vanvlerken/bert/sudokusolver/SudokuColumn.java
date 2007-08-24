/**
 * @author wItspirit
 * 8-jan-2006
 * SudokuColumn.java
 */

package be.vanvlerken.bert.sudokusolver;

public class SudokuColumn extends SudokuGroup
{
    public SudokuColumn(SudokuChar[][] sudoku, int column)
    {
        for (int i = 0; i < 9; i++)
        {
            entries[i] = sudoku[i][column];
        }
    }
}
