/**
 * @author wItspirit
 * 1-apr-2004
 * PhysicsParameters.java
 */

package be.vanvlerken.bert.physics;

import java.awt.Dimension;

/**
 * Hosts some application constants
 */
public final class PhysicsParameters {
    // Startup values
    public static final int simulationWidth = 800;
    public static final int simulationHeight = 600;
    public static final Dimension spinnerDim = new Dimension(15, 1);
    public static final Dimension scrollDim = new Dimension(300, 100);

    // Settings requiring simulation restart
    public static int nrOfElements = 30;
    public static int maxRadius = 20;
    public static double maxInitialSpeed = 50;

    // Settings that can be modified during a simulation run
    public static int msBetweenIterations = 10;
    public static int nrOfTimeSteps = 100;
    public static double timeStep = 0.001;

    public static double gravityCte = 9.81;
    public static boolean applyGravity = true;
    public static boolean transparentBounds = false;

}
