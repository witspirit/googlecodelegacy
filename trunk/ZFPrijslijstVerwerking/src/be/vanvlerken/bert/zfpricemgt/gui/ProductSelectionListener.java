/**
 * @author wItspirit
 * 30-jan-2005
 * ProductSelectionListener.java
 */

package be.vanvlerken.bert.zfpricemgt.gui;

import be.vanvlerken.bert.zfpricemgt.IProduct;


/**
 * Interface that provides notification of a product selection
 */
public interface ProductSelectionListener
{
    public abstract void selectedProduct(IProduct product);
}
