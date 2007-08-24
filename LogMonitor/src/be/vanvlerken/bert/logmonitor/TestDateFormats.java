/**
 * @author wItspirit
 * 28-okt-2003
 * TestDateFormats.java
 */

package be.vanvlerken.bert.logmonitor;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * This simple class just prints the date in all possible formats
 */
public class TestDateFormats
{
    private Date currentDate;
    
    public TestDateFormats()
    {
        currentDate = new Date();
    }
    
    public static void main(String[] args)
    {
        TestDateFormats testDataFormats = new TestDateFormats();
        testDataFormats.displayDates();
    }

    /**
     * Displays the current data in all available formats
     */
    private void displayDates()
    {
        int styles[] = new int[4];
        styles[0] = DateFormat.FULL;
        styles[1] = DateFormat.LONG;
        styles[2] = DateFormat.MEDIUM;
        styles[3] = DateFormat.SHORT;
        
         Locale locales[] = DateFormat.getAvailableLocales();
//        Locale locales[] = new Locale[3];
//        locales[0] = Locale.UK;
//        locales[1] = Locale.US;
//        locales[2] = Locale.ENGLISH;        
        
        int dateStyle;
        int timeStyle;
        Locale curLocale;
        
        for ( int i=2; i < 4; i++ )
        {
            dateStyle = styles[i];
            for ( int j=2; j < 3; j++)
            {
                timeStyle = styles[j];
                for (int k=0; k < locales.length; k++)
                {
                    curLocale = locales[k];
                    DateFormat df = DateFormat.getDateTimeInstance(dateStyle, timeStyle, curLocale);
                    
                    System.out.println(dateStyle+"-"+timeStyle+"-"+curLocale+" : "+df.format(currentDate));
                }
            }
        }
    }
}
