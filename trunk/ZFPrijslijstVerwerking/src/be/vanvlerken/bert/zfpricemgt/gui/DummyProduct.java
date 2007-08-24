/**
 * @author wItspirit
 * 4-jan-2004
 * Product.java
 */

package be.vanvlerken.bert.zfpricemgt.gui;

import java.util.Date;

import be.vanvlerken.bert.zfpricemgt.IProduct;


/**
 * This class represents a product that could not be found.
 */
public class DummyProduct implements IProduct
{
    private String  number;
    private String  description;
    private double  price;
    private Date    validSince;
    
    public DummyProduct(String number, String message)
    {
        this.number = number;
        this.description = message;
        this.price = 0.0;
        this.validSince = new Date();
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
     * @return Returns the validSince.
     */
    public Date getValidSince()
    {
        return validSince;
    }
}
