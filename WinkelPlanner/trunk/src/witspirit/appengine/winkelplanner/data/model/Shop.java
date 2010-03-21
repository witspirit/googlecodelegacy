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
public class Shop {
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
    private String street;
    
    @Persistent
    private String number;
    
    @Persistent
    private String zipCode;
    
    @Persistent
    private String city;
    
    @Persistent
    private String countryCode; // ISO Country Code

    public Shop(User author, String name) {
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
        this.lastModifiedTimestamp = new Date();
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
        this.lastModifiedTimestamp = new Date();
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
        this.lastModifiedTimestamp = new Date();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
        this.lastModifiedTimestamp = new Date();
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
        this.lastModifiedTimestamp = new Date();
    }

    public Date getInsertionTimestamp() {
        return insertionTimestamp;
    }

    public Date getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    
}
