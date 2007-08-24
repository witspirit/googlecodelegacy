/**
 * @author wItspirit
 * 4-apr-2004
 * PhysicsFormulas.java
 */

package be.vanvlerken.bert.physics;


/**
 * Harbors the basic PhysicsFormulas formulas
 */
public class PhysicsFormulas
{
    public static final double INFINITE_MASS = Double.MAX_VALUE;
    
    public static double calculateCollisionSpeed(double massOther, double massOwn, double speedOther, double speedOwn)
    {
        double speed;
        
        speed = (2*massOther*speedOther + (massOwn-massOther)*speedOwn) / (massOwn + massOther); 
        
        return speed;
    }
    
    public static double calculateNewLocationSpeed(double currentLocation, double speed, double timeDelta)
    {
        double location = currentLocation;
        
        location += speed*timeDelta;
        
        return location;
    }
    
    public static double calculateNewLocationGravity(double currentLocation, double timeDelta, double gravityCte)
    {
        double location = currentLocation;
        
        location += 0.5*gravityCte*timeDelta*timeDelta;
        
        return location;
    }
    
    public static double calculateNewSpeed(double currentSpeed, double timeDelta, double gravityCte)
    {
        double speed = currentSpeed;
        
        speed += gravityCte*timeDelta;
        
        return speed;
    }
    
}
