/**
 * @author wItspirit
 * 5-apr-2004
 * Simulation.java
 */

package be.vanvlerken.bert.physics;

import org.apache.log4j.Logger;

/**
 * Handles the effective Simulation
 */
public class Simulation implements Runnable {
    private static final Logger logger = Logger.getLogger(Simulation.class);

    private TheGrid grid;
    private boolean go;

    public Simulation(TheGrid grid) {
	this.grid = grid;
	go = true;
    }

    /**
     * @see java.lang.Runnable#run()
     */
    public void run() {
	logger.info("Starting simulation");
	long i = 0;
	while (go) {
	    logger.info("Simulation iteration " + i);
	    try {
		Thread.sleep(PhysicsParameters.msBetweenIterations);
	    } catch (InterruptedException e) {
		logger.warn("Sleep got interrupted. (" + e.getMessage() + ")");
	    }
	    grid.displayNextIteration();
	    i++;
	}
	logger.info("Finished simulation");
    }

    public void stopSimulation() {
	go = false;
    }
}
