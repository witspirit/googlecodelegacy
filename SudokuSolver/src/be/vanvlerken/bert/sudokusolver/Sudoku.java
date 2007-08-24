/**
 * @author wItspirit
 * 8-jan-2006
 * Sudoku.java
 */

package be.vanvlerken.bert.sudokusolver;

public class Sudoku
{
    private SudokuChar[][]   sudoku;    // Authorative structure
    private SudokuSquare[][] squares;   // Square SudokuGroup
    private SudokuRow[]      rows;      // Row SudokuGroup
    private SudokuColumn[]   columns;   // Column SudokuGroup

    public Sudoku()
    {
        // Create Authorative structure
        sudoku = new SudokuChar[9][9];
        for (int row=0; row < 9; row++)
            for (int column=0; column < 9; column++)
                sudoku[row][column] = new SudokuChar(row, column);
        
        // Craete Square sudokugroups
        squares = new SudokuSquare[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
            {
                squares[i][j] = new SudokuSquare(sudoku, i, j);
            }

        // Create Row and Column sudokugroups
        rows = new SudokuRow[9];
        columns = new SudokuColumn[9];
        for (int i=0; i < 9; i++)
        {
            rows[i] = new SudokuRow(sudoku, i);
            columns[i] = new SudokuColumn(sudoku, i);
        }
    }

    public SudokuSquare getSquare(int row, int column)
    {
        return squares[row][column];
    }
    
    public SudokuRow getRow(int row)
    {
        return rows[row];
    }
    
    public SudokuColumn getColumn(int column)
    {
        return columns[column];
    }
    
    public SudokuChar getChar(int row, int column)
    {
        return sudoku[row][column];
    }
    
    public void clear()
    {
        for (int row=0; row < 9; row++)        
            for (int column=0; column < 9; column++)
            {
                sudoku[row][column].reset(); 
            }        
    }
    
    public boolean isValid()
    {
        // Check if all squares are valid
        for (int i=0; i < 3; i++)
            for (int j=0; j < 3; j++)
            {
                if ( !squares[i][j].isValid() )
                {
                    return false;
                }
            }
        // Check if all rows and columns are valid
        for (int i=0; i < 9; i++)
        {
            if ( !rows[i].isValid() || !columns[i].isValid() )
            {
                return false;
            }
        }
        return true;
    }
}
