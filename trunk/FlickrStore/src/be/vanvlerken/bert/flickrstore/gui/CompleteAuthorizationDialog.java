/**
 * @author wItspirit
 * 22-okt-2005
 * RequestAuthorizationDialog.java
 */

package be.vanvlerken.bert.flickrstore.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class CompleteAuthorizationDialog extends JDialog implements ActionListener {    
    private static final long serialVersionUID = 1L;

    public enum DialogState {
	CANCEL, COMPLETE_AUTHORIZE
    };

    private JButton cancelButton;
    private JButton completeAuthorizeButton;

    private DialogState dialogState = DialogState.CANCEL;

    public CompleteAuthorizationDialog() throws HeadlessException {
	generateLayout();
    }

    private void generateLayout() {
	setTitle("Return to FlickrStore...");
	setSize(300, 200);
	setModal(true);
	setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

	JPanel dialogPanel = new JPanel(new BorderLayout());
	dialogPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

	JLabel dialogMessage = new JLabel(
		"<html>Return to this window after you have finished the authorization process on flickr.com. <br><br> Once you are done, click the 'Complete Authorization' button below and you can begin using FlickrStore.</html>");

	JPanel buttonPanel = new JPanel(new BorderLayout(10, 0));
	cancelButton = new JButton("Cancel");
	cancelButton.addActionListener(this);
	completeAuthorizeButton = new JButton("Complete Authorization");
	completeAuthorizeButton.addActionListener(this);
	buttonPanel.add(completeAuthorizeButton, BorderLayout.CENTER);
	buttonPanel.add(cancelButton, BorderLayout.EAST);

	dialogPanel.add(dialogMessage, BorderLayout.CENTER);
	dialogPanel.add(buttonPanel, BorderLayout.SOUTH);

	setContentPane(dialogPanel);
    }

    public DialogState showDialog() {
	setVisible(true);
	return dialogState;
    }

    public void actionPerformed(ActionEvent ae) {
	if (ae.getSource() == cancelButton) {
	    dialogState = DialogState.CANCEL;
	    dispose();
	} else if (ae.getSource() == completeAuthorizeButton) {
	    dialogState = DialogState.COMPLETE_AUTHORIZE;
	    dispose();
	}
    }

    public void setSize(int width, int height) {
	GraphicsEnvironment gE = GraphicsEnvironment.getLocalGraphicsEnvironment();
	Rectangle screenRect = gE.getMaximumWindowBounds();
	int xLoc = (screenRect.width - width) / 2;
	int yLoc = (screenRect.height - height) / 2;
	super.setLocation(new Point(xLoc, yLoc));
	super.setSize(width, height);
    }

    public void setSize(Dimension dim) {
	setSize(dim.width, dim.height);
    }
}
