import java.awt.Color;
import java.awt.Graphics;

/**
 * @author vlerkenb
 * 6-mei-2003
 * RectangleDrawer.java
 */

/**
 * Is able to draw a specific Rectangle to a specific Graphics screen
 */
public class RectangleDrawer {

    private Color color;
    private int x;
    private int y;
    private int width;
    private int height;

    public RectangleDrawer(Color color, int x, int y, int width, int height) {
        this.color = color;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }
}
