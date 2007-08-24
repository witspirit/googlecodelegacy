/**
 * @author wItspirit
 * 28-mrt-2004
 * Physics.java
 */

package be.vanvlerken.bert.physics;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import be.vanvlerken.bert.components.gui.applicationwindow.ApplicationWindow;
import be.vanvlerken.bert.components.gui.applicationwindow.ShutdownListener;

/**
 * This application performs a small simulation of a gravitational force on
 * some objects. It's been done many times by lot's of people,but I want to
 * understand it as well...
 */
public class Physics implements Runnable, ShutdownListener, ActionListener
{
    private static final Logger logger = Logger.getLogger(Physics.class);

    private ApplicationWindow appWindow;
    private TheGrid grid;

    private Simulation sim;

    private JButton startStopButton;
    private JButton resetButton;

    private JPanel buttonPanel;
    private ParameterPanel paramPanel;
    private JPanel rootPanel;

    private JSplitPane gridSplit;

    /**
     * @param args
     */
    public Physics(String[] args)
    {
        initApplicationWindow();
        initGrid();
        initButtons();
        initParamPanel();
                
        JScrollPane paramScroller = new JScrollPane(paramPanel); 
        paramScroller.setPreferredSize(PhysicsParameters.scrollDim);
        gridSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, paramScroller, grid);        
        gridSplit.setOneTouchExpandable(true);
        rootPanel = new JPanel(new BorderLayout());
        rootPanel.add(buttonPanel, BorderLayout.NORTH);
        rootPanel.add(gridSplit, BorderLayout.CENTER);
        
        appWindow.getContentPane().add(rootPanel);
    }

    /**
     * 
     */
    private void initParamPanel()
    {
        paramPanel = new ParameterPanel();
    }

    /**
     *  
     */
    private void initButtons()
    {
        buttonPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
        
        startStopButton = new JButton("Start");
        startStopButton.addActionListener(this);
        resetButton = new JButton("Reset");
        resetButton.addActionListener(this);
        
        buttonPanel.add(startStopButton);
        buttonPanel.add(resetButton);
    }

    private void initGrid()
    {
        logger.info("Creating a grid ");
        grid = new TheGrid();        
    }

    private void initApplicationWindow()
    {
        logger.info("Initializing application window");
        appWindow = new ApplicationWindow("Physics");
        logger.debug(
            "Setting the ApplicationWindow size to "
                + PhysicsParameters.simulationWidth
                + "x"
                + PhysicsParameters.simulationHeight);
        appWindow.setSize(
            PhysicsParameters.simulationWidth,
            PhysicsParameters.simulationHeight);
        logger.debug("Setting the ShutdownListener");
        appWindow.setShutdownListener(this);
        logger.debug("Disabling resize");
        appWindow.setResizable(true);
    }

    /**
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
        logger.info("Activating Application Window");
        appWindow.show();
    }

    public static void main(String[] args)
    {
        PropertyConfigurator.configure("log4j.properties");
        Physics gravity = new Physics(args);
        gravity.run();
    }

    /**
     * @see be.vanvlerken.bert.components.gui.applicationwindow.ShutdownListener#performShutdown()
     */
    public void performShutdown()
    {
        logger.info("Shutting down all services...");
        stopSim();
        logger.info("Physics cleanly terminated");
    }

    private void startSim()
    {
        sim = new Simulation(grid);
        Thread simThr = new Thread(sim);
        simThr.start();
        startStopButton.setText("Stop");
    }

    /**
     *  
     */
    private void stopSim()
    {
        if ( sim != null )
        {    
            sim.stopSimulation();
        }
        sim = null;
        startStopButton.setText("Start");
    }

    /**
     * 
     */
    private void resetGrid()
    {        
        if ( sim != null )
        {
            stopSim();
        }
        
        gridSplit.remove(grid);
        initGrid();
        gridSplit.add(grid, JSplitPane.RIGHT);
        gridSplit.revalidate();
    }
    
    
    /**
     * *
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == startStopButton)
        {
            if (sim == null)
            {
                startSim();                
            }
            else
            {
                stopSim();                
            }
        }
        else if ( e.getSource() == resetButton)
        {
            resetGrid();
        }
    }
}
