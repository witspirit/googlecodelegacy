/**
 * @author wItspirit
 * 29-mrt-2004
 * TheGrid.java
 */

package be.vanvlerken.bert.physics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

import org.apache.log4j.Logger;

/**
 * Provides the hosting environment for the elements
 */
public class TheGrid extends JComponent {
    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TheGrid.class);

    private Element elements[];
    private boolean elementsGenerated;

    private int currentWidth = 0;
    private int currentHeight = 0;

    public TheGrid() {
	elements = new Element[PhysicsParameters.nrOfElements];
	elementsGenerated = false;

	this.setBorder(BorderFactory.createEtchedBorder());
    }

    public void displayNextIteration() {
	for (int t = 0; t < PhysicsParameters.nrOfTimeSteps; t++) {
	    if (elementsGenerated) {
		// Apply speed should occur before applyGravity !
		for (Element element : elements) {
		    element.applySpeed();
		}

		if (PhysicsParameters.applyGravity) {
		    for (Element element : elements) {
			element.applyGravity(PhysicsParameters.gravityCte);
		    }
		}

		for (Element element : elements) {
		    element.applyBounds(currentWidth, currentHeight);
		}

		for (int i = 0; i < elements.length; i++) {
		    for (int j = i + 1; j < elements.length; j++) {
			if (elements[i].overlap(elements[j])) {
			    Element temp = new Element(elements[i]);
			    elements[i].applyCollision(elements[j]);
			    elements[j].applyCollision(temp);
			}
		    }
		}
	    }
	}
	repaint();
    }

    /**
     * @see java.awt.Component#paint(java.awt.Graphics)
     */
    public void paintComponent(Graphics g) {
	Insets myInsets = this.getInsets();

	if (logger.isDebugEnabled()) {
	    logger.debug("JComponent: width=" + getWidth() + ", height=" + getHeight());
	    logger.debug("Insets: top=" + myInsets.top + ", bottom=" + myInsets.bottom + ", left=" + myInsets.left + ", right=" + myInsets.right);
	}

	int effectiveWidth = getWidth() - myInsets.left - myInsets.right;
	int effectiveHeight = getHeight() - myInsets.top - myInsets.bottom;

	if (widthHasChanged(effectiveWidth, effectiveHeight)) {
	    updateDimensions(effectiveWidth, effectiveHeight);
	    if (!elementsGenerated) {
		generateElements();
	    } else {
		updateElements();
	    }
	}

	if (isOpaque()) {
	    g.setColor(getBackground());
	    g.fillRect(0, 0, getWidth(), getHeight());
	}

	g.setColor(Color.BLUE);
	for (int i = 0; i < elements.length; i++) {
	    elements[i].draw(g, myInsets.left, myInsets.top);
	}
    }

    /**
     * @param effectiveWidth
     * @param effectiveHeight
     */
    private void updateDimensions(int width, int height) {
	currentWidth = width;
	currentHeight = height;
    }

    /**
     * @param effectiveWidth
     * @param effectiveHeight
     */
    private void updateElements() {
	logger.info("Updating elements to new dimensions of environment...");

	for (Element element : elements) {
	    element.applyBounds(currentWidth, currentHeight);
	}

	for (Element element : elements) {
	    element.placeWithinBounds(currentWidth, currentHeight);
	}

	for (int i = 0; i < elements.length; i++) {
	    for (int j = i + 1; j < elements.length; j++) {
		if (elements[i].overlap(elements[j])) {
		    Element temp = new Element(elements[i]);
		    elements[i].applyCollision(elements[j]);
		    elements[j].applyCollision(temp);
		}
	    }
	}
    }

    /**
     * @param effectiveWidth
     * @param effectiveHeight
     * @return
     */
    private boolean widthHasChanged(int effectiveWidth, int effectiveHeight) {
	if (currentWidth != effectiveWidth || currentHeight != effectiveHeight)
	    return true;
	return false;
    }

    /**
     * @param nrOfElements
     */
    private void generateElements() {
	logger.info("Creating " + elements.length + " elements...");

	for (int i = 0; i < elements.length; i++) {
	    logger.debug("Creating element " + i);
	    elements[i] = createNonOverlappingElement(currentWidth, currentHeight);
	}

	logger.info(elements.length + " succesfully created.");

	elementsGenerated = true;
    }

    /**
     * @return
     */
    private Element createNonOverlappingElement(int width, int height) {
	Element el;
	int overloadCounter = 100;
	// Protects against impossible positioning attempts
	do {
	    el = Element.createRandomElement(width, height);
	    overloadCounter--;
	} while (overlapsExisting(el) != null && overloadCounter > 0);

	if (overloadCounter <= 0) {
	    logger.warn("Aborted overlap avoidance, maximum allowed attempts exceeded.");
	} else {
	    logger.info("Element created succesfully.");
	}
	return el;
    }

    /**
     * @param el
     * @return
     */
    private Element overlapsExisting(Element el) {
	logger.debug("Checking for overlap with existing elements.");

	for (int i = 0; i < elements.length; i++) {
	    if (elements[i] != null && elements[i] != el) {
		if (el.overlap(elements[i])) {
		    logger.debug("Overlap detected.");
		    return elements[i];
		}
	    }
	}

	logger.debug("No overlap detected.");
	return null;
    }
}
