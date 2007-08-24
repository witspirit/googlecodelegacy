/**
 * @author wItspirit
 * 30-jan-2005
 * HistoryDialog.java
 */

package be.vanvlerken.bert.zfpricemgt.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import be.vanvlerken.bert.zfpricemgt.IProduct;
import be.vanvlerken.bert.zfpricemgt.database.IZFPriceDatabase;

/**
 * Dialog window that shows History information of a certain product
 */
public class HistoryDialog extends JDialog implements ActionListener, ProductSelectionListener
{
	private static final long serialVersionUID = 1L;

	private static final ResourceBundle msgs = ResourceBundle.getBundle("be.vanvlerken.bert.zfpricemgt.gui.localization.HistoryDialog");

    private IZFPriceDatabase db;
    private String           productNumber;
    private LookupHistory    lookupHistory;
    private JButton          deleteButton;
    private JButton          closeButton;

    private IProduct         selectedProduct = null;

    public HistoryDialog(JFrame parent, String productNumber, IZFPriceDatabase db)
    {
        super(parent, msgs.getString("history")+" - " + productNumber);
        this.productNumber = productNumber;
        this.db = db;

        add(gui());
        setSize(400, 340);

        populate();
        lookupHistory.addProductSelectionListener(this);
    }

    /**
     * 
     */
    private void populate()
    {
        lookupHistory.clearHistory();
        IProduct[] products = db.getProducts(productNumber);
        for (IProduct product : products)
        {
            lookupHistory.addProduct(product);
            // To make sure there is always a product selected
            selectedProduct = product;
        }
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
     * @return
     */
    private Component gui()
    {
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Main table view
        lookupHistory = new LookupHistory();
        JScrollPane scrollPane = new JScrollPane(lookupHistory.getTable());

        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        deleteButton = new JButton(msgs.getString("delete"));
        deleteButton.setMnemonic(msgs.getString("delete.mnemonic").charAt(0));
        deleteButton.addActionListener(this);

        closeButton = new JButton(msgs.getString("close"));
        closeButton.setMnemonic(msgs.getString("close.mnemonic").charAt(0));
        closeButton.addActionListener(this);

        buttonPanel.add(deleteButton);
        buttonPanel.add(closeButton);

        // Position parts on the root pane
        rootPanel.add(scrollPane, BorderLayout.CENTER);
        rootPanel.add(buttonPanel, BorderLayout.SOUTH);
        return rootPanel;
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae)
    {
        if (ae.getSource() == deleteButton)
        {
            deleteAction();
        }
        else if (ae.getSource() == closeButton)
        {
            closeAction();
        }
    }

    /**
     * 
     */
    private void closeAction()
    {
        dispose();
    }

    /**
     * 
     */
    private void deleteAction()
    {
        if (selectedProduct != null)
        {
            db.deleteProduct(selectedProduct);
            populate();
        }
    }

    /**
     * @see be.vanvlerken.bert.zfpricemgt.gui.ProductSelectionListener#selectedProduct(be.vanvlerken.bert.zfpricemgt.IProduct)
     */
    public void selectedProduct(IProduct product)
    {
        selectedProduct = product;
    }
}
