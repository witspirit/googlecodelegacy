/**
 * @author wItspirit
 * 22-okt-2005
 * RequestAuthorizationDialog.java
 */

package be.vanvlerken.bert.flickrstore.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
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


public class RequestAuthorizationDialog extends JDialog implements ActionListener
{
    public enum DialogState {cancel, authorize};  
    
    private JButton publicButton;
    private JButton authorizeButton;
    
    private DialogState dialogState = DialogState.cancel;
    
    public RequestAuthorizationDialog() throws HeadlessException
    {
        generateLayout();
    }

    private void generateLayout()
    {
        setTitle("FlickrStore requests your authorization");        
        setSize(300, 250);
        setModal(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        
        JPanel dialogPanel = new JPanel(new BorderLayout());
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        JLabel dialogMessage = new JLabel(
                "<html>FlickrStore requires your authorization before it can read all your photos and data on Flickr.<br><br>Authorizing is a simple process which takes place in your web browser. When you're finished, return to this window to complete authorization and begin using FlickrStore.<br><br>If you just want to browse public photos, you can choose 'Public'.</html>");
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2,10,10));        
        publicButton = new JButton("Public");
        publicButton.addActionListener(this);
        authorizeButton = new JButton("Authorize...");
        authorizeButton.addActionListener(this);
        buttonPanel.add(authorizeButton);
        buttonPanel.add(publicButton);

        dialogPanel.add(dialogMessage, BorderLayout.CENTER);
        dialogPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        setContentPane(dialogPanel);
    }
    
    public DialogState showDialog()
    {
        setVisible(true);
        return dialogState;
    }
    
    public void actionPerformed(ActionEvent ae)
    {
        if (ae.getSource() == publicButton)
        {
            dialogState = DialogState.cancel;
            dispose();
        }
        else if (ae.getSource() == authorizeButton)
        {
            dialogState = DialogState.authorize;
            dispose();
        }
    }
    
    public void setSize(int width, int height)
    {
        GraphicsEnvironment gE = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle screenRect = gE.getMaximumWindowBounds();
        int xLoc = (screenRect.width - width) / 2;
        int yLoc = (screenRect.height - height) / 2;
        super.setLocation(new Point(xLoc, yLoc));
        super.setSize(width, height);
    }
    
    public void setSize(Dimension dim)
    {
        setSize(dim.width, dim.height);
    }
}
