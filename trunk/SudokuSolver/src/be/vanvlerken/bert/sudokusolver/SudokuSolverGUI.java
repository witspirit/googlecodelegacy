/**
 * @author wItspirit
 * 8-jan-2006
 * SudokuSolverGUI.java
 */

package be.vanvlerken.bert.sudokusolver;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import be.vanvlerken.bert.components.gui.applicationwindow.AboutMessage;
import be.vanvlerken.bert.components.gui.applicationwindow.ApplicationWindow;


public class SudokuSolverGUI implements ActionListener
{
    private ApplicationWindow appWindow;
    private SudokuView sudokuView;
    private Sudoku sudoku;
    
    private JButton storeClearButton;
    private JButton solveButton;
    private JButton loadButton;
    
    public SudokuSolverGUI(Sudoku sudoku)
    {
        
        this.sudoku = sudoku;
        appWindow = new ApplicationWindow("SudokuSolver");
        
        sudokuView = new SudokuView(sudoku);
        
        storeClearButton = new JButton("Store");
        storeClearButton.addActionListener(this);
        solveButton = new JButton("Solve");
        solveButton.setEnabled(false);
        solveButton.addActionListener(this);
        loadButton = new JButton("Load");
        loadButton.addActionListener(this);
        
        
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(storeClearButton, BorderLayout.WEST);
        buttonPanel.add(loadButton, BorderLayout.CENTER);
        buttonPanel.add(solveButton, BorderLayout.EAST);
        
        JPanel masterPanel = new JPanel(new BorderLayout());                
        masterPanel.add(sudokuView, BorderLayout.CENTER);
        masterPanel.add(buttonPanel, BorderLayout.NORTH);
                
        appWindow.getContentPane().add(masterPanel);
        setAbout();
    }
    
    private void setAbout()
    {
        AboutMessage about = AboutMessage.getInstance();
        about.setAboutMessage("Small application for Solving a standard Sudoku");
        about.setAuthor("Bert - witspirit - Van Vlerken");
        about.setProgramName("SudokuSolver");
        about.setVersion("1.0");
    }

    public void show()
    {
        appWindow.setVisible(true);
    }

    public void actionPerformed(ActionEvent ae)
    {
        if ( ae.getSource() == storeClearButton )
        {
            // Store/Clear button pressed
            if ( storeClearButton.getText() == "Store" )
            {
                // Store data in the model
                sudokuView.store();
                storeClearButton.setText("Clear");
                solveButton.setEnabled(true);
            }
            else
            {
                // Clear data from model
                sudoku.clear();                
                
                // Update view
                sudokuView.update();
                
                solveButton.setEnabled(false);
                storeClearButton.setText("Store");
            }
        }
        else if ( ae.getSource() == loadButton )
        {
            sudoku.clear();
            new SudokuLoader().load(sudoku);
            
            storeClearButton.setText("Clear");
            solveButton.setEnabled(true);
            sudokuView.update();                        
        }            
        else if ( ae.getSource() == solveButton )
        {
            // Solve button pressed
            // Start solver
            SolverEngine solverEngine = new SolverEngine(sudoku);
            solverEngine.solveCompletely();
            
            // Update view
            sudokuView.update();
        }
    }
}
