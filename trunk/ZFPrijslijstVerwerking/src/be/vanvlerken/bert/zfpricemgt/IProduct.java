/**
 * @author wItspirit
 * 5-jan-2004
 * IProduct.java
 */

package be.vanvlerken.bert.zfpricemgt;

import java.util.Date;

/**
 * This is the basic Product interface, containing the accessors towards it's properties
 */
public interface IProduct
{
    /**
     * @return The product number
     */
    public abstract String getNumber();

    /**
     * @return A short description of the product
     */
    public abstract String getDescription();

    /**
     * @return The price of the product, since the data specified by ValidSince
     */
    public abstract double getPrice();
    
    /**
     * @return The date since which the price is valid
     */
    public abstract Date getValidSince();
}