package witspirit.appengine.winkelplanner.data.model;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ShoppingList {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private User author;
    
    @Persistent
    private Date insertionTimestamp;
    
    @Persistent
    private Date lastModifiedTimestamp;

    @Persistent
    private String name;
    
    @Persistent
    private String productList;
    
    public ShoppingList(User author, String name) {
        this.author = author;
        this.name = name;
        this.productList = "";
        this.insertionTimestamp = new Date();
        this.lastModifiedTimestamp = this.insertionTimestamp;
    }

    public Key getKey() {
        return key;
    }

    public User getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.lastModifiedTimestamp = new Date();
    }
    
    public String getProductList() {
        return productList;
    }
    
    public void setProductList(String productList) {
        if (productList == null) {
            productList = "";
        }
        this.productList = productList;
        this.lastModifiedTimestamp = new Date();
    }

    public Date getInsertionTimestamp() {
        return insertionTimestamp;
    }

    public Date getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    
}
