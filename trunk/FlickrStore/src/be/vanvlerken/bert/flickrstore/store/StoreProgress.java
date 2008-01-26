/**
 * @author wItspirit
 * 1-nov-2005
 * StoreProgress.java
 */

package be.vanvlerken.bert.flickrstore.store;

public interface StoreProgress {
    public void setMaximum(int max);

    public void setCurrent(int current);
}
