/**
 * @author wItspirit
 * 4-jan-2004
 * PriceGui.java
 */

package be.vanvlerken.bert.zfpricemgt.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.print.PrinterException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import be.vanvlerken.bert.components.gui.applicationwindow.AboutMessage;
import be.vanvlerken.bert.components.gui.applicationwindow.ApplicationWindow;
import be.vanvlerken.bert.zfpricemgt.AlreadyExistsException;
import be.vanvlerken.bert.zfpricemgt.IProduct;
import be.vanvlerken.bert.zfpricemgt.database.IZFPriceDatabase;
import be.vanvlerken.bert.zfpricemgt.database.Product;

/**
 * Builds the GUI for the price retrieval application
 */
public class PriceGui implements ActionListener, KeyListener, ProductSelectionListener
{
    private static final ResourceBundle msgs = ResourceBundle.getBundle("be.vanvlerken.bert.zfpricemgt.gui.localization.PriceGui");
    
    private DateFormat        df;

    private ApplicationWindow appWindow;
    private IZFPriceDatabase  database;
    private LookupHistory     lookupHistory;

    private JTextField        numberField;
    private JTextField        descriptionField;
    private JTextField        priceField;
    private JTextField        validSinceField;

    private JButton           lookupButton;
    private JButton           resetButton;
    private JButton           historyButton;
    private JButton           addButton;
    private JButton           clearButton;
    private JButton           printButton;
    private JButton           exitButton;

    private OldPriceDetector oldPriceDetector;

    /**
     * @param database
     */
    public PriceGui(IZFPriceDatabase database)
    {
        this.database = database;

        df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRANCE);
        
        oldPriceDetector = new OldPriceDetector();
        
        appWindow = new ApplicationWindow(msgs.getString("title"));        
        appWindow.setSize(650, 400);
        
        JMenuBar menuBar = appWindow.getJMenuBar();
        JMenu fileMenu =menuBar.getMenu(0);
        JMenuItem importItem = new JMenuItem(new ImportAction(appWindow, database));
        JMenuItem exportItem = new JMenuItem(new ExportAction(appWindow, database));
        fileMenu.insert(importItem, 0);
        fileMenu.insert(exportItem, 1);
        
        JMenu configMenu = new JMenu("Config");
        JMenuItem thresholdConfig = new JMenuItem(new ThresholdConfigAction(appWindow, oldPriceDetector));
        JMenuItem dbConfig = new JMenuItem(new DBConfigurationAction(appWindow, database.getConfigurator()));
        configMenu.add(thresholdConfig);
        configMenu.add(dbConfig);
        menuBar.add(configMenu, 1);
        
        AboutMessage aboutMessage = AboutMessage.getInstance();
        aboutMessage.setAuthor("Bert - wItspirit- Van Vlerken");
        aboutMessage.setAboutMessage(msgs.getString("about.message"));
        aboutMessage.setProgramName(msgs.getString("program.name"));
        aboutMessage.setVersion(msgs.getString("program.version"));
        aboutMessage.setCopyrightMessage(msgs.getString("copyright.statement"));

        fillContentPane();
    }

    /**
     * 
     */
    private void fillContentPane()
    {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel retrievePanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));

        mainPanel.add(retrievePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // RetrievePanel
        JLabel numberLabel = new JLabel(msgs.getString("product.nr.label"));
        numberLabel.setHorizontalAlignment(JLabel.RIGHT);
        JLabel descriptionLabel = new JLabel(msgs.getString("description.label"));
        descriptionLabel.setHorizontalAlignment(JLabel.RIGHT);
        JLabel priceLabel = new JLabel(msgs.getString("price.label"));
        priceLabel.setHorizontalAlignment(JLabel.RIGHT);
        JLabel validSinceLabel = new JLabel(msgs.getString("valid.since.label"));
        validSinceLabel.setHorizontalAlignment(JLabel.RIGHT);

        numberField = new JTextField();
        numberField.setEditable(true);
        numberField.addKeyListener(this);
        descriptionField = new JTextField();
        descriptionField.setEditable(true);
        priceField = new JTextField();
        priceField.setEditable(true);
        validSinceField = new JTextField();
        validSinceField.setEditable(true);

        JPanel labelPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        JPanel fieldPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        retrievePanel.add(labelPanel, BorderLayout.WEST);
        retrievePanel.add(fieldPanel, BorderLayout.CENTER);
        retrievePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        labelPanel.add(numberLabel);
        labelPanel.add(descriptionLabel);
        labelPanel.add(priceLabel);
        labelPanel.add(validSinceLabel);

        fieldPanel.add(numberField);
        fieldPanel.add(descriptionField);
        fieldPanel.add(priceField);
        fieldPanel.add(validSinceField);

        // ButtonPanel
        lookupButton = new JButton(msgs.getString("lookup.button"));
        lookupButton.addActionListener(this);
        lookupButton.setMnemonic(msgs.getString("lookup.button.mnemonic").charAt(0));
        resetButton = new JButton(msgs.getString("reset.button"));
        resetButton.addActionListener(this);
        resetButton.setMnemonic(msgs.getString("reset.button.mnemonic").charAt(0));
        historyButton = new JButton(msgs.getString("history.button"));
        historyButton.addActionListener(this);
        historyButton.setMnemonic(msgs.getString("history.button.mnemonic").charAt(0));
        addButton = new JButton(msgs.getString("add.button"));
        addButton.addActionListener(this);
        addButton.setMnemonic(msgs.getString("add.button.mnemonic").charAt(0));

        buttonPanel.add(lookupButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(historyButton);
        buttonPanel.add(addButton);

        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JComponent tableView = createTableView();

        JPanel superPanel = new JPanel(new BorderLayout());
        superPanel.add(mainPanel, BorderLayout.NORTH);
        superPanel.add(tableView, BorderLayout.CENTER);

        appWindow.setContentPane(superPanel);
    }

    /**
     * @return
     */
    private JComponent createTableView()
    {
        JPanel tablePanel = new JPanel(new BorderLayout());

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        clearButton = new JButton(msgs.getString("clear.list.button"));
        clearButton.setMnemonic(msgs.getString("clear.list.button.mnemonic").charAt(0));
        clearButton.addActionListener(this);
        printButton = new JButton(msgs.getString("print.button"));
        printButton.setMnemonic(msgs.getString("print.button.mnemonic").charAt(0));
        printButton.addActionListener(this);        
        exitButton = new JButton(msgs.getString("stop.button"));
        exitButton.addActionListener(this);
        exitButton.setMnemonic(msgs.getString("stop.button.mnemonic").charAt(0));

        buttonPanel.add(clearButton);
        buttonPanel.add(printButton);
        buttonPanel.add(exitButton);

        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lookupHistory = new LookupHistory();
        lookupHistory.addProductSelectionListener(this);
        JScrollPane scrollPane = new JScrollPane(lookupHistory.getTable());        

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.add(buttonPanel, BorderLayout.SOUTH);

        return tablePanel;
    }

    /**
     * 
     */
    public void show()
    {
        appWindow.setVisible(true);
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae)
    {
        if (ae.getSource() == lookupButton)
        {
            lookupAction();
        }
        else if (ae.getSource() == resetButton)
        {
            resetAction();
        }
        else if (ae.getSource() == exitButton)
        {
            exitAction();
        }
        else if (ae.getSource() == clearButton)
        {
            clearAction();
        }
        else if (ae.getSource() == printButton)
        {
            printAction();
        }
        else if (ae.getSource() == historyButton)
        {
            historyAction();
        }
        else if (ae.getSource() == addButton)
        {
            addAction();
        }
    }

    /**
     * 
     */
    private void addAction()
    {
        try
        {
            String number = parseNumber(numberField.getText());
            String description = parseDescription(descriptionField.getText());
            double price = parsePrice(priceField.getText());
            Date validSince = parseValidSince(validSinceField.getText());
            Product newProduct = new Product(number, description, price, validSince);
            setFields(newProduct);

            database.addProduct(newProduct);
            lookupHistory.addProduct(newProduct);
        }
        catch (ParseException e)
        {
            // e.printStackTrace();
            JOptionPane.showMessageDialog(appWindow, e.getLocalizedMessage(), msgs.getString("input.problem"), JOptionPane.ERROR_MESSAGE);
        }
        catch (AlreadyExistsException e)
        {
            // e.printStackTrace();
            JOptionPane.showMessageDialog(appWindow, e.getLocalizedMessage(), msgs.getString("database.problem"), JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * @param text
     * @return
     * @throws ParseException
     */
    private Date parseValidSince(String validSinceStr) throws ParseException
    {
        return df.parse(validSinceStr);
    }

    /**
     * @param text
     * @return
     */
    private double parsePrice(String priceStr) throws ParseException
    {
        double price = 0.0;
        try
        {
            price = Double.parseDouble(priceStr);
        }
        catch (NumberFormatException e)
        {
            throw new ParseException(e.getLocalizedMessage(), 0);
        }
        return price;
    }

    /**
     * @param text
     * @return
     */
    private String parseDescription(String descriptionStr) throws ParseException
    {
        return descriptionStr;
    }

    /**
     * 
     */
    private void historyAction()
    {
        // Display another window, containing the history of a certain product
        String number;
        try
        {
            number = parseNumber(numberField.getText());
        }
        catch (ParseException e)
        {
            // e.printStackTrace();
            number = e.getLocalizedMessage();
        }
        HistoryDialog history = new HistoryDialog(appWindow, number, database);
        history.setVisible(true);
    }

    /**
     * 
     */
    private void printAction()
    {
        // Print the contents of lookupHistory
        try
        {
            lookupHistory.getTable().print();
        }
        catch (PrinterException e)
        {
            JOptionPane.showMessageDialog(appWindow, e.getLocalizedMessage(), msgs.getString("printing.problem"), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 
     */
    private void clearAction()
    {
        lookupHistory.clearHistory();
    }

    /**
     * 
     */
    private void exitAction()
    {
        appWindow.shutdown();
    }

    /**
     * 
     */
    private void resetAction()
    {
        numberField.setText("");
        descriptionField.setText("");
        priceField.setText("");
        validSinceField.setBackground(Color.WHITE);
        validSinceField.setText("");        

        numberField.grabFocus();
    }

    /**
     * 
     */
    private void lookupAction()
    {
        IProduct product = null;

        String number;
        try
        {
            number = parseNumber(numberField.getText());
        }
        catch (ParseException e)
        {
            // e.printStackTrace();
            number = e.getLocalizedMessage();
        }

        if (!number.equals(""))
        {
            product = database.getProduct(number);
            if (product == null)
            {
                product = new DummyProduct(number, msgs.getString("product.not.found"));
            }
            setFields(product);

            lookupHistory.addProduct(product);
        }
    }

    /**
     * @param product
     */
    private void setFields(IProduct product)
    {
        numberField.setText(product.getNumber());
        descriptionField.setText(product.getDescription());
        priceField.setText(Double.toString(product.getPrice()));        
        validSinceField.setText(df.format(product.getValidSince()));

        oldPriceDetector.applyDetector(product, validSinceField);
        
        numberField.grabFocus();
        numberField.setSelectionStart(0);
        numberField.setSelectionEnd(numberField.getText().length());
    }

    /**
     * Adapts the input format to the format expected by the database
     * 
     * @param string
     * @return
     */
    private String parseNumber(String inputNumber) throws ParseException
    {
        String parsedNumber = inputNumber.replace(',', ' ');
        parsedNumber = parsedNumber.replace('.', ' ');
        return parsedNumber;
    }

    /**
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
    public void keyTyped(KeyEvent ke)
    {
    }

    /**
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    public void keyPressed(KeyEvent ke)
    {
        if (ke.getKeyCode() == KeyEvent.VK_ENTER)
        {
            lookupAction();
        }
    }

    /**
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
    public void keyReleased(KeyEvent ke)
    {
    }

    /**
     * @see be.vanvlerken.bert.zfpricemgt.gui.ProductSelectionListener#selectedProduct(be.vanvlerken.bert.zfpricemgt.IProduct)
     */
    public void selectedProduct(IProduct product)
    {
        setFields(product);
    }


    
}
