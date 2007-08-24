/**
 * @author wItspirit
 * 8-jan-2006
 * SudokuView.java
 */

package be.vanvlerken.bert.sudokusolver;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class SudokuView extends JPanel
{
    private SudokuEntry[][] entries;
    private Sudoku          sudoku;

    public SudokuView(Sudoku sudoku)
    {
        this.sudoku = sudoku;
        generateLayout();
    }

    private void generateLayout()
    {
        // Create all the entries
        entries = new SudokuEntry[9][9];
        for (int i = 0; i < 9; i++)
            // Row
            for (int j = 0; j < 9; j++)
                // Column
                entries[i][j] = new SudokuEntry();

        // Put them in the proper layout
        this.setLayout(new GridLayout(3, 3, 0, 0));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        for (int i = 0; i < 3; i++)
            // Row
            for (int j = 0; j < 3; j++) // Column
            {
                JPanel squarePanel = new JPanel(new GridLayout(3, 3, 0, 0));
                squarePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                for (int ii = 0; ii < 3; ii++)
                    // Row
                    for (int jj = 0; jj < 3; jj++) // Column
                    {
                        squarePanel.add(entries[i * 3 + ii][j * 3 + jj]);
                    }
                this.add(squarePanel);
            }
    }

    public void store()
    {
        for (int row = 0; row < 9; row++)
        {            
            for (int column = 0; column < 9; column++)
            {
                SudokuChar sChar = sudoku.getChar(row, column);
                String entryContent = (String) entries[row][column].getValue();
                if (entryContent != null)
                {                    
                    sChar.setValue(Integer.parseInt(entryContent));
                }
            }
        }
    }

    public void update()
    {
        for (int row = 0; row < 9; row++)
        {
            for (int column = 0; column < 9; column++)
            {
                SudokuChar sChar = sudoku.getChar(row, column);
                if (sChar.getValue() != SudokuChar.EMPTY)
                {                    
                    entries[row][column].setValue(sChar.getValue());
                }
                else
                {
                    entries[row][column].setValue(null);
                }
            }
        }
    }
}
