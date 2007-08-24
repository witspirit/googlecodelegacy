/**
 * @author wItspirit
 * 17-apr-2005
 * OldPriceDetector.java
 */

package be.vanvlerken.bert.zfpricemgt.gui;

import java.awt.Color;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.prefs.Preferences;

import javax.swing.JTextField;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.vanvlerken.bert.zfpricemgt.IProduct;

/**
 * This class makes sure that old prices are detected and visualised
 */
public class OldPriceDetector
{
    private static final String KEY_DISPLAYTHRESHOLD = "displayThreshold";
    private static final String KEY_THRESHOLDDATE    = "thresholdDate";

    private static final Log    log                  = LogFactory.getLog(OldPriceDetector.class);

    private DateFormat          df;
    private boolean             displayThreshold;
    // Date at which it is marked as 'old'
    private Date                thresholdDate;
    private Preferences         prefs;

    public OldPriceDetector()
    {
        df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRANCE);
        prefs = Preferences.userNodeForPackage(OldPriceDetector.class);

        displayThreshold = prefs.getBoolean(KEY_DISPLAYTHRESHOLD, false);
        String thresholdDateStr = prefs.get(KEY_THRESHOLDDATE, "01/01/2005");
        setThresholdDate(thresholdDateStr);
    }

    public void applyDetector(IProduct product, JTextField field)
    {
        if (displayThreshold && product.getValidSince().before(thresholdDate))
        {
            field.setBackground(Color.YELLOW);
        }
        else
        {
            field.setBackground(Color.WHITE);
        }
    }

    public Date getThresholdDate()
    {
        return thresholdDate;
    }

    public void setThresholdDate(String thresholdDateStr)
    {
        Date thrDate;
        try
        {
            thrDate = df.parse(thresholdDateStr);
            setThresholdDate(thrDate);
        }
        catch (ParseException e)
        {
            log.warn("Could not parse " + thresholdDateStr + " as being a valid date");
            log.warn("Disabling old price detection.");
            setDisplayThreshold(false);
            setThresholdDate(new Date());
        }
    }

    public void setThresholdDate(Date date)
    {
        thresholdDate = date;
        prefs.put(KEY_THRESHOLDDATE, df.format(date));
    }

    public boolean isDisplayThreshold()
    {
        return displayThreshold;
    }

    public void setDisplayThreshold(boolean displayThreshold)
    {
        this.displayThreshold = displayThreshold;
        prefs.putBoolean(KEY_DISPLAYTHRESHOLD, displayThreshold);
    }
}
