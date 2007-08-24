/**
 * @author wItspirit
 * 8-jan-2006
 * SudokuEntry.java
 */

package be.vanvlerken.bert.sudokusolver;

import java.awt.Color;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;


public class SudokuEntry extends JFormattedTextField
{
    public SudokuEntry()
    {
        this.setColumns(1); // Only one character allowed
        this.setHorizontalAlignment(JFormattedTextField.CENTER);
        this.setBorder(BorderFactory.createLineBorder(Color.GRAY,1));
        AbstractFormatter formatter = createFormatter();
        this.setFormatterFactory(new DefaultFormatterFactory(formatter));
    }

    private AbstractFormatter createFormatter()
    {
        try
        {
            MaskFormatter formatter = new MaskFormatter("#");
            formatter.setInvalidCharacters("0");
            return formatter;
        }
        catch (ParseException e)
        {
            // Should never happen ! Since this should be code tested !
            e.printStackTrace();
        }
        return null;
    }
}
