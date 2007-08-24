/**
 * @author vlerkenb
 * 7-mei-2003
 * OverlayTypeWriter.java
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.util.Random;

import javax.swing.JFrame;

/**
 * This application uses the Full-Screen Exclusive Mode API to write in type-writer style on screen. Just for fun
 */
public class OverlayTypeWriter {

    private Graphics screen;
    private int screenWidth;
    private int screenHeight;

    public static void main(String[] args) {
        OverlayTypeWriter typeWriter = new OverlayTypeWriter();
        typeWriter.start(args);
    }

    public OverlayTypeWriter() {
        screen = null;
        screenWidth = 0;
        screenHeight = 0;
    }

    /**
     * Method start.
     * 
     * @param args
     */
    private void start(String[] args) {
        /* Discard arguments */

        try {
            setFullScreenExclusiveMode(true);
            screen.setColor(Color.RED);
            screen.setFont(Font.decode("Arial-BOLDITALIC-40"));
            Point currentPosition = new Point(100, 100);
            currentPosition = typeText(currentPosition, "I am wItspirit");
            currentPosition.y += 400;
            currentPosition = typeText(currentPosition, "I am haunting YOU");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("Got interrupted...");
            }
            decayScreen();
        } finally {
            setFullScreenExclusiveMode(false);
            System.exit(0);
        }

    }

    /**
     * Method decayScreen. Performs a screen crumbling effect
     */
    private void decayScreen() {
        int[] crumbleHeight = new int[screenWidth];
        for (int i = 0; i < crumbleHeight.length; i++) {
            crumbleHeight[i] = screenHeight;
        }

        screen.setColor(Color.BLACK);

        Random random = new Random();
        int selectedColumn;

        int crumbs = screenHeight * screenWidth;
        while (crumbs != 0) {
            selectedColumn = random.nextInt(screenWidth);
            while (crumbleHeight[selectedColumn] == 0) {
                selectedColumn = random.nextInt(screenWidth);
            }
            screen.fillRect(selectedColumn, screenHeight - crumbleHeight[selectedColumn], 1, 1);
            crumbleHeight[selectedColumn]--;
            crumbs--;
        }
    }

    /**
     * Method typeText. Types some text at a specific location in type-writer style
     * 
     * @param location
     * @param string
     */
    private Point typeText(Point location, String string) {
        char[] text = string.toCharArray();
        FontMetrics fontMetrics = screen.getFontMetrics();
        int myX = location.x;
        for (int i = 0; i < text.length; i++) {
            screen.drawChars(text, i, 1, myX, location.y);
            myX += fontMetrics.charWidth(text[i]);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Got interrupted...");
            }
            // screen.drawString(string, x, y);
        }
        return new Point(myX, location.y);
    }

    /**
     * Method setFullScreenExclusiveMode.
     */
    private void setFullScreenExclusiveMode(boolean goFullScreen) {
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();

        if (goFullScreen) {
            JFrame frame = new JFrame();
            frame.setUndecorated(true);
            frame.setIgnoreRepaint(true);

            if (graphicsDevice.isFullScreenSupported()) {
                graphicsDevice.setFullScreenWindow(frame);
            }
            screen = frame.getGraphics();
            screenWidth = frame.getWidth();
            screenHeight = frame.getHeight();
        } else {
            screen.dispose();
            graphicsDevice.setFullScreenWindow(null);
        }
    }
}
