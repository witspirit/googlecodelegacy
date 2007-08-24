/**
 * @author vlerkenb
 * 6-mei-2003
 * FullScreenHelloWorld.java
 */

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.Random;

import javax.swing.JWindow;

/**
 * Displays Hello World in a random screen mode
 */
public class FullScreenHelloWorld {

    public static void main(String args[]) {
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
        DisplayMode displayModes[] = graphicsDevice.getDisplayModes();
        DisplayMode originalDisplayMode = graphicsDevice.getDisplayMode();
        JWindow window = new JWindow() {

            private static final long serialVersionUID = 1L;

            public void paint(Graphics g) {
                g.setColor(Color.blue);
                g.drawString("Hello, World!", 50, 50);
            }
        };
        try {
            if (graphicsDevice.isFullScreenSupported()) {
                graphicsDevice.setFullScreenWindow(window);
            }
            Random random = new Random();
            int mode = random.nextInt(displayModes.length);
            DisplayMode displayMode = displayModes[mode];
            System.out.println(displayMode.getWidth() + "x" + displayMode.getHeight() + " \t" + displayMode.getRefreshRate() + " / "
                    + displayMode.getBitDepth());
            if (graphicsDevice.isDisplayChangeSupported()) {
                graphicsDevice.setDisplayMode(displayMode);
            }
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        } finally {
            graphicsDevice.setDisplayMode(originalDisplayMode);
            graphicsDevice.setFullScreenWindow(null);
        }
        System.exit(0);
    }
}
