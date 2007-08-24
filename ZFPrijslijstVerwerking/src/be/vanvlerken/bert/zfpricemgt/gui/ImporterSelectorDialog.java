/**
 * @author wItspirit
 * 3-feb-2005
 * ImporterSelectorDialog.java
 */

package be.vanvlerken.bert.zfpricemgt.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import be.vanvlerken.bert.zfpricemgt.database.importers.Importer;


/**
 * Dialog for selecting one of the valid importers
 */
public class ImporterSelectorDialog extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private static final ResourceBundle msgs = ResourceBundle.getBundle("be.vanvlerken.bert.zfpricemgt.gui.localization.ImporterSelectionDialog");
    
    private JList importerList;
    private JButton okButton;
    private Importer selectedImporter;

    public ImporterSelectorDialog(JFrame parent)
    {
        super(parent, msgs.getString("title"), true);
        
        add(buildLayout());
        
        setSize(300, 300);
    }
    /**
     * @return
     */
    private JPanel buildLayout()
    {
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        JTextArea explanation = new JTextArea(msgs.getString("info.message"));
        explanation.setLineWrap(true);
        explanation.setWrapStyleWord(true);
        explanation.setBackground(this.getBackground());
        importerList = new JList();
        JScrollPane listScroller = new JScrollPane(importerList);
        listScroller.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        okButton = new JButton(msgs.getString("ok.button"));
        okButton.setMnemonic(msgs.getString("ok.button.mnemonic").charAt(0));
        okButton.addActionListener(this);
        
        rootPanel.add(explanation, BorderLayout.NORTH);
        rootPanel.add(listScroller, BorderLayout.CENTER);
        rootPanel.add(okButton, BorderLayout.SOUTH);
        
        return rootPanel;
    }
    
    /**
     * @see be.vanvlerken.bert.zfpricemgt.database.importers.ImporterSelector#selectImporter(java.util.List)
     */
    public Importer selectImporter(List<Importer> importers)
    {
        importerList.setListData(importers.toArray());
        setVisible(true);
        return selectedImporter;
    }
    
    public void setSize(int width, int height)
    {
        GraphicsEnvironment gE = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle screenRect = gE.getMaximumWindowBounds();
        int xLoc = (screenRect.width - width) / 2;
        int yLoc = (screenRect.height - height) / 2;
        this.setLocation(new Point(xLoc, yLoc));
        super.setSize(width, height);
    }

    public void setSize(Dimension newSize)
    {
        setSize(newSize.width, newSize.height);
    }
    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae)
    {
        if ( ae.getSource() == okButton )
        {
            selectedImporter = (Importer) importerList.getSelectedValue();
            if ( selectedImporter == null )
            {
                JOptionPane.showMessageDialog(this, msgs.getString("please.select"), null, JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                dispose();
            }
        }
    }
}
