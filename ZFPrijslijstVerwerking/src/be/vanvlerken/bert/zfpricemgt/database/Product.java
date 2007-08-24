/**
 * @author wItspirit
 * 4-jan-2004
 * Product.java
 */

package be.vanvlerken.bert.zfpricemgt.database;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import be.vanvlerken.bert.zfpricemgt.IProduct;



/**
 * This class represents a Product, consisting of a number, description and price
 * The private setter methods and default constructor are for Hibernate and Hibernate only
 */
public class Product implements IProduct, Serializable
{
	private static final long serialVersionUID = 1L;

	private static final DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRANCE);
    
    private String  number;
    private String  description;
    private double  price;
    private Date    validSince;
    
    public boolean equals(Object obj)
    {
        if ( obj instanceof Product )
        {
            Product prod = (Product) obj;
            return number.equals(prod.number) && validSince.equals(prod.validSince);
        }
        return false;
    }
    
    public int hashCode()
    {
        return number.hashCode() ^ validSince.hashCode();
    }
    
    public Product(String number, String description, double price, Date validSince)
    {
        this.number = number;
        this.description = description;
        this.price = price;
        this.validSince = validSince;
    }
        
    /**
     * @return
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @return
     */
    public String getNumber()
    {
        return number;
    }

    /**
     * @return
     */
    public double getPrice()
    {
        return price;
    }

    /**
     * @param string
     */
    public void setDescription(String string)
    {
        description = string;
    }

    /**
     * @param string
     */
    @SuppressWarnings("unused") // Used for DB mapping
	private void setNumber(String string)
    {
        number = string;
    }

    /**
     * @param d
     */
    @SuppressWarnings("unused") // Used for DB mapping
    private void setPrice(double d)
    {
        price = d;
    }

    /**
     * @return Returns the validSince.
     */
    public Date getValidSince()
    {
        return validSince;
    }
    /**
     * @param validSince The validSince to set.
     */
    @SuppressWarnings("unused") // Used for DB mapping
    private void setValidSince(Date validSince)
    {
        this.validSince = validSince;
    }

    @Override
    public String toString()
    {
        return "Product("+number+", "+description+", "+price+", "+df.format(validSince)+")";
    }
}
