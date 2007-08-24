/**
 * @author wItspirit
 * 8-jan-2006
 * SudokuRow.java
 */

package be.vanvlerken.bert.sudokusolver;

public class SudokuRow extends SudokuGroup
{
    public SudokuRow(SudokuChar[][] sudoku, int row)
    {
        for (int i = 0; i < 9; i++)
        {
            entries[i] = sudoku[row][i];
        }
    }
}
