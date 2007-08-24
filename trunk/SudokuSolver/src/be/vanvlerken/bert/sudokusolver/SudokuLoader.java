/**
 * @author wItspirit
 * 15-jan-2006
 * SudokuLoader.java
 */

package be.vanvlerken.bert.sudokusolver;


public class SudokuLoader
{
    private static final int EMPTY = 0;
    private static final int[][] easyTestSudoku =
    {
        {0,0,0,0,9,7,0,0,3},
        {0,2,0,8,1,6,0,0,0},
        {0,0,8,0,0,0,7,0,0},
        {0,0,2,0,0,0,0,5,0},
        {0,1,4,0,7,0,6,0,0},
        {0,0,0,0,4,0,2,0,9},
        {0,0,0,4,8,0,0,0,0},
        {3,0,7,6,0,1,4,0,0},
        {6,4,9,0,2,3,0,0,0}
    };
    private static final int[][] veryHardTestSudoku =
    {
        {0,0,0,1,0,0,0,0,0},
        {0,7,0,0,0,4,0,6,0},
        {0,0,4,0,6,2,0,9,0},
        {0,0,9,0,3,6,7,0,1},
        {5,3,0,0,0,0,0,8,6},
        {6,0,8,9,7,0,4,0,0},
        {0,2,0,6,1,0,3,0,0},
        {0,9,0,5,0,0,0,2,0},
        {0,0,0,0,0,8,0,0,0}
    };
    private static final int[][] testSudoku = veryHardTestSudoku;
    
    public SudokuLoader()
    {
        
    }
    
    public void load(Sudoku sudoku)
    {
        for (int row=0; row < 9; row++)
        {
            for (int column=0; column < 9; column++)
            {
                if ( testSudoku[row][column] != EMPTY )
                {
                    sudoku.getChar(row, column).setValue(testSudoku[row][column]);
                }
            }
        }
    }
}
