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
public class Product {
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
    
    public Product(User author, String name) {
        this.author = author;
        this.name = name;
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

    public Date getInsertionTimestamp() {
        return insertionTimestamp;
    }

    public Date getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    
}
