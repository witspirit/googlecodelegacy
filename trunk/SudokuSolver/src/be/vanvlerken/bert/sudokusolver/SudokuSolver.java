/**
 * @author wItspirit
 * 8-jan-2006
 * SudokuSolver.java
 */

package be.vanvlerken.bert.sudokusolver;


public class SudokuSolver implements Runnable
{
    private SudokuSolverGUI gui;
    private Sudoku sudoku;
    
    /**
     * @param args Arguments are ignored
     */
    public static void main(String[] args)
    {
        SudokuSolver solver = new SudokuSolver();
        solver.run();
    }

    public SudokuSolver()
    {
        sudoku = new Sudoku();
        gui = new SudokuSolverGUI(sudoku);
    }

    public void run()
    {
        gui.show();
    }
}
