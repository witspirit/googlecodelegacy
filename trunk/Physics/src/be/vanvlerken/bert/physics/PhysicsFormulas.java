/**
 * @author wItspirit
 * 4-apr-2004
 * PhysicsFormulas.java
 */

package be.vanvlerken.bert.physics;

/**
 * Harbors the basic PhysicsFormulas formulas
 */
public class PhysicsFormulas {
    public static final double INFINITE_MASS = -1.0; // A negative mass is not used in our application

    public static double calculateCollisionSpeed(double massOwn, double speedOwn, double massOther, double speedOther) {
	// v = 2m1v1+(m2-m1)v2 / m1+m2
	if (massOther == INFINITE_MASS) { // Don't seem to get the formula to work properly when using an 'infinite' mass, always end up with NaN
	    return -speedOwn;
	} 
	return (2 * massOther * speedOther + (massOwn - massOther) * speedOwn) / (massOwn + massOther);
    }

    public static double calculateNewLocationSpeed(double currentLocation, double speed, double timeDelta) {
	// x = x0 + v(t-t0)
	return currentLocation + (speed * timeDelta);
    }

    public static double calculateNewLocationGravity(double currentLocation, double gravityCte, double timeDelta) {
	// x = x0 + 1/2 g(t-t0)^2
	return currentLocation + (0.5 * gravityCte * timeDelta * timeDelta);
    }

    public static double calculateNewSpeed(double currentSpeed, double gravityCte, double timeDelta) {
	// v = v0 + g(t-t0)
	return currentSpeed + (gravityCte * timeDelta);
    }
}
