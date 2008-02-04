package be.vanvlerken.bert.flickrstore.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class ExitAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
    
    public ExitAction() {
	super("E&xit");
    }

    public void actionPerformed(ActionEvent e) {
	System.exit(0);	
    }
}
