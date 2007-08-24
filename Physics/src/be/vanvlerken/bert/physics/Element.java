/**
 * @author wItspirit
 * 29-mrt-2004
 * Element.java
 */

package be.vanvlerken.bert.physics;

import java.awt.Graphics;
import org.apache.log4j.Logger;

/**
 * Models an Element entity that can float around in TheGrid
 */
public class Element
{
    private static final Logger logger = Logger.getLogger(Element.class);

    private double              x;
    private double              y;
    private int                 radius;

    private double              Xspeed = 0;
    private double              Yspeed = 0;

    public static Element createRandomElement(int width, int height)
    {
        Element el;

        int radius = (int) Math
                .round((Math.random() * PhysicsParameters.maxRadius) + 0.5);
        double x = radius + Math.random() * (width - radius * 2);
        double y = radius + Math.random() * (height - radius * 2);
        double xSpeed = -PhysicsParameters.maxInitialSpeed
                + (Math.random() * (2*PhysicsParameters.maxInitialSpeed));
        double ySpeed = -PhysicsParameters.maxInitialSpeed
                + (Math.random() * (2*PhysicsParameters.maxInitialSpeed));

        logger.debug("Random generated values: x=" + x + ", y=" + y + ", Vx = "
                + xSpeed + ", Vy = " + ySpeed + ",r=" + radius);

        el = new Element(x, y, xSpeed, ySpeed, radius);
        return el;
    }

    public Element(double x, double y, double xSpeed, double ySpeed, int radius)
    {
        this.x = x;
        this.y = y;
        this.Xspeed = xSpeed;
        this.Yspeed = ySpeed;
        this.radius = radius;
    }

    public void applyGravity(double g)
    {
        // x = x0 + v0*t + 1/2*a*t*t -> x = x + Yspeed*1s + 1/2*g*1s
        y = PhysicsFormulas.calculateNewLocationGravity(y,
                PhysicsParameters.timeStep, g);

        // v = v0 + a*t -> Yspeed = Yspeed + g*1s
        Yspeed = PhysicsFormulas.calculateNewSpeed(Yspeed,
                PhysicsParameters.timeStep, g);
    }

    public void applySpeed(double g)
    {
        // x = x0 + v0*t + 1/2*a*t*t -> x = x + Yspeed*1s + 1/2*g*1s
        y = PhysicsFormulas.calculateNewLocationSpeed(y, Yspeed,
                PhysicsParameters.timeStep);

        x = PhysicsFormulas.calculateNewLocationSpeed(x, Xspeed,
                PhysicsParameters.timeStep);
    }

    /**
     * Checks if the Element bumps into the bounds and calculates change in
     * state accordingly
     * 
     * @param width
     * @param height
     */
    public boolean applyBounds(int width, int height)
    {
        boolean boundsReached = false;

        double minX = x - radius;
        double maxX = x + radius;

        double minY = y - radius;
        double maxY = y + radius;

        if (minX <= 0 || maxX >= width)
        {
            if (PhysicsParameters.transparentBounds)
            {
                if (minX <= 0)
                {
                    x += width;
                }
                else
                {
                    x -= width;
                }
            }
            else
            {
                // Collision against left or right wall...
                Xspeed = PhysicsFormulas.calculateCollisionSpeed(
                        PhysicsFormulas.INFINITE_MASS, this.radius, 0, Xspeed);
            }

            boundsReached = true;
        }

        if (minY <= 0 || maxY >= height)
        {
            if (PhysicsParameters.transparentBounds)
            {
                if (minY <= 0)
                {
                    y += width;
                }
                else
                {
                    y -= width;
                }
            }
            else
            {

                // Collision against top or bottom wall...
                Yspeed = PhysicsFormulas.calculateCollisionSpeed(
                        PhysicsFormulas.INFINITE_MASS, this.radius, 0, Yspeed);
            }
            
            boundsReached = true;
        }

        return boundsReached;
    }

    public void applyCollision(Element element)
    {
        // Totally elastic collision
        Xspeed = PhysicsFormulas.calculateCollisionSpeed(element.radius,
                this.radius, element.Xspeed, this.Xspeed);
        Yspeed = PhysicsFormulas.calculateCollisionSpeed(element.radius,
                this.radius, element.Yspeed, this.Yspeed);
    }

    /**
     * @param element
     * @return
     */
    private double getDistanceTo(Element element)
    {
        double h = Math.abs(this.x - element.x);
        double v = Math.abs(this.y - element.y);

        double distance = Math.sqrt(h * h + v * v);

        return distance;
    }

    public boolean overlap(Element element)
    {
        double distance = getDistanceTo(element);
        double minDistance = element.radius + this.radius;

        if (distance > minDistance)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public void draw(Graphics g, int xOffset, int yOffset)
    {
        int pixelX = (int) Math.round(x) + xOffset;
        int pixelY = (int) Math.round(y) + yOffset;
        g.fillOval(pixelX - radius, pixelY - radius, radius * 2, radius * 2);
    }

    public Element(Element el)
    {
        this.x = el.x;
        this.y = el.y;
        this.radius = el.radius;

        this.Xspeed = el.Xspeed;
        this.Yspeed = el.Yspeed;
    }

    /**
     * @param width
     * @param height
     */
    public void placeWithinBounds(int width, int height)
    {
        double minX = x - radius;
        double maxX = x + radius;

        double minY = y - radius;
        double maxY = y + radius;

        if (minX <= 0)
        {
            // Collision against left wall...
            x = radius;
        }
        else if (maxX >= width)
        {
            // Collision against right wall...
            x = width - radius;
        }

        if (minY <= 0)
        {
            // Collision against top wall...
            y = radius;
        }
        else if (maxY >= height)
        {
            // Collision against bottom wall...
            y = height - radius;
        }
    }
}