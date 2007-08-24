/**
 * @author wItspirit
 * 11-jan-2006
 * SolverEngine.java
 */

package be.vanvlerken.bert.sudokusolver;

import java.util.LinkedList;
import java.util.List;

public class SolverEngine
{
    private static final int MAX_ITERATIONS = 10;
    private Sudoku sudoku;

    public SolverEngine(Sudoku sudoku)
    {
        this.sudoku = sudoku;
    }

    public void solveCompletely()
    {
        long startTime = System.nanoTime();
        int iterationCount = 0;
        while ( !solveIteration() && iterationCount < MAX_ITERATIONS )
        {
            iterationCount++;
        }
        long endTime = System.nanoTime();
        if ( iterationCount >= MAX_ITERATIONS )
        {
            System.out.println("Unable to solve the Sudoku in "+MAX_ITERATIONS+" iterations.");
        }
        else
        {
            System.out.println("Solved the Sudoku in "+iterationCount+" iterations, taking "+(endTime-startTime)+" nano seconds.");
        }
    }

    private boolean solveIteration()
    {
        boolean solved = true;
        
        // Technique: Imagine a number and see where it could fit. A fit is found when        
        // a SudokuGroup only contains one place where it fits.
        for (int number=1; number < 10; number++)
        {
            if ( !solveFor(number) )
            {
                solved = false;
            }
        }
        
        // Technique: For each SudokuGroup you see what the possibilities are. If there is a 
        // position where only one possibility remains, then we have found a fit.
        for (int row=0; row < 9; row++)
            for (int column=0; column < 9; column++)
            {
                SudokuChar sChar = sudoku.getChar(row, column);
                if ( !solveSinglePossibility(sChar) )
                {
                    solved = false;
                }
            }
        
        return solved;
    }
    
    private boolean solveFor(int number)
    {
        boolean solved = true;

        // The ROWS
        for (int row = 0; row < 9; row++)
        {
            SudokuRow sRow = sudoku.getRow(row);
            if (!solveGroupFor(number, sRow))
            {
                solved = false;
            }
        }

        // The COLUMNS
        for (int column = 0; column < 9; column++)
        {
            SudokuColumn sColumn = sudoku.getColumn(column);
            if (!solveGroupFor(number, sColumn))
            {
                solved = false;
            }
        }

        // The SQUARES
        for (int row = 0; row < 3; row++)
        {
            for (int column = 0; column < 3; column++)
            {
                SudokuSquare sSquare = sudoku.getSquare(row, column);
                if (!solveGroupFor(number, sSquare))
                {
                    solved = false;
                }
            }
        }

        return solved;
    }

    private boolean solveGroupFor(int number, SudokuGroup group)
    {
        List<SudokuChar> possibleChars = getCharsFor(number, group);
        if (possibleChars.size() == 0) // This number is already solved for
                                        // this group
        {
            return true;
        }
        else if (possibleChars.size() == 1) // We have found a new solution !
        {
            SudokuChar sChar = possibleChars.get(0);
            sChar.setValue(number);
            return true;
        }
        return false; // We could not yet solve this number, for this group
    }

    private List<SudokuChar> getCharsFor(int number, SudokuGroup group)
    {
        List<SudokuChar> chars = new LinkedList<SudokuChar>();

        // If this group not yet contains the number, then we have possibilities
        // here
        if (!group.contains(number))
        {
            List<SudokuChar> freeEntries = group.getFreeEntries();
            for (SudokuChar sChar : freeEntries)
            {
                // If one of the free chars is available for this number, then
                // add
                // it to the list of chars that can hold this number.
                if (canContain(number, sChar))
                {
                    chars.add(sChar);
                }
            }
        }

        return chars;
    }

    private boolean canContain(int number, SudokuChar sChar)
    {
        // Rule 1: If number exists in this chars row -> FALSE
        // Rule 2: If number exists in this chars column -> FALSE
        // Rule 3: If number exists ni this char square -> FALSE
        if (rowContains(number, sChar) || columnContains(number, sChar)
                || squareContains(number, sChar)) { return false; }

        // Else: TRUE
        return true;
    }
    
    private boolean solveSinglePossibility(SudokuChar sChar)
    {
        if ( sChar.isFixed() )
        {
            return true;
        }
        
        int canContainCount = 0;
        int singleNumber = 0;
        for (int number=1; number < 10; number++)
        {
            if ( canContain(number, sChar) )
            {
                canContainCount++;
                singleNumber = number;
            }
        }        
        if ( canContainCount == 1 )
        {
            sChar.setValue(singleNumber);
            return true;
        }
        return false;
    }    

    /**
     * @param number
     * @param sChar
     * @return
     */
    private boolean squareContains(int number, SudokuChar sChar)
    {
        return sudoku.getSquare(sChar.getRow() / 3, sChar.getColumn() / 3).contains(number);
    }

    /**
     * @param number
     * @param sChar
     * @return
     */
    private boolean columnContains(int number, SudokuChar sChar)
    {
        return sudoku.getColumn(sChar.getColumn()).contains(number);
    }

    /**
     * @param number
     * @param sChar
     * @return
     */
    private boolean rowContains(int number, SudokuChar sChar)
    {
        return sudoku.getRow(sChar.getRow()).contains(number);
    }

    public void solveOneChar()
    {

    }
}
