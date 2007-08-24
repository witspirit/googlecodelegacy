/**
 * @author wItspirit
 * 28-dec-2003
 * ZFPriceMgt.java
 */

package be.vanvlerken.bert.zfpricemgt;

import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import be.vanvlerken.bert.zfpricemgt.database.DatabaseFactory;
import be.vanvlerken.bert.zfpricemgt.database.IZFPriceDatabase;
import be.vanvlerken.bert.zfpricemgt.gui.PriceGui;

/**
 * This is a small application that will retrieve price information from the
 * database based on a product number
 */
public class ZFPriceMgt implements Runnable
{
//    static
//    {
//        Locale.setDefault(Locale.ENGLISH);
//    }
    
    private static final ResourceBundle msgs = ResourceBundle.getBundle("be.vanvlerken.bert.zfpricemgt.localization.ZFPriceMgt");

    private IZFPriceDatabase            database;
    private PriceGui                    gui;

    public static void main(String[] args)
    {
        ZFPriceMgt priceRetrieval = new ZFPriceMgt();
        priceRetrieval.run();

    }

    /**
     * @param args
     */
    public ZFPriceMgt()
    {
        try
        {
            database = new DatabaseFactory().getDatabase();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, msgs.getString("problem") + e.getLocalizedMessage());
            System.exit(1);
        }
        gui = new PriceGui(database);
    }

    /**
     * 
     */
    public void run()
    {
        gui.show();
    }

}
