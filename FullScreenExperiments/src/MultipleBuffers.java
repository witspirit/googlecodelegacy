/**
 * @author vlerkenb
 * 6-mei-2003
 * MultipleBuffers.java
 */

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.Random;

/*
 * Uses Multiple buffers for taking it's own control of the screen writing No passive rendering anymore with paint like calls !
 */
public class MultipleBuffers {

    public static void main(String args[]) {
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
        DisplayMode originalDisplayMode = graphicsDevice.getDisplayMode();
        JFrame frame = new JFrame();
        frame.setUndecorated(true);
        frame.setIgnoreRepaint(true);
        try {
            if (graphicsDevice.isFullScreenSupported()) {
                graphicsDevice.setFullScreenWindow(frame);
            }
            Random random = new Random();
            // DisplayMode displayModes[] = graphicsDevice.getDisplayModes();
            // int mode = random.nextInt(displayModes.length);
            // DisplayMode displayMode = displayModes[mode];
            // System.out.println(
            // displayMode.getWidth()
            // + "x"
            // + displayMode.getHeight()
            // + " \t"
            // + displayMode.getRefreshRate()
            // + " / "
            // + displayMode.getBitDepth());
            // if (graphicsDevice.isDisplayChangeSupported())
            // {
            // graphicsDevice.setDisplayMode(displayMode);
            // }
            frame.createBufferStrategy(1);
            BufferStrategy bufferStrategy = frame.getBufferStrategy();
            int width = frame.getWidth();
            int height = frame.getHeight();

            for (int i = 0; i < 2; i++) {
                Graphics g = bufferStrategy.getDrawGraphics();
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, width, height);
                bufferStrategy.show();
                g.dispose();
            }

            RectangleDrawer oldRectangle = null;

            for (int i = 0; i < 1000; i++) {
                Graphics g = bufferStrategy.getDrawGraphics();
                if (oldRectangle != null) {
                    oldRectangle.draw(g);
                }
                RectangleDrawer newRectangle = new RectangleDrawer(new Color(random.nextInt()), random.nextInt(width), random
                        .nextInt(height), 100, 100);
                newRectangle.draw(g);
                bufferStrategy.show();
                g.dispose();
                oldRectangle = newRectangle;
                // Thread.sleep(1);
            }
        }
        // catch (InterruptedException e)
        // {
        // }
        finally {
            graphicsDevice.setDisplayMode(originalDisplayMode);
            graphicsDevice.setFullScreenWindow(null);
        }
        System.exit(0);
    }
}