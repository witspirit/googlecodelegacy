package be.vanvlerken.bert.zfpricemgt.database;

import java.io.Serializable;

public class JdbcDriverConfig implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String				description;
	
	private String              dbDriver;
    private String              dbUrl;
    private String              dbUser;
    private String              dbPassword;
    private String              pricesTable;
	
    public String getDbDriver() {
		return dbDriver;
	}
	public void setDbDriver(String dbDriver) {
		this.dbDriver = dbDriver;
	}
	public String getDbPassword() {
		return dbPassword;
	}
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
	public String getDbUrl() {
		return dbUrl;
	}
	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}
	public String getDbUser() {
		return dbUser;
	}
	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}
	public String getPricesTable() {
		return pricesTable;
	}
	public void setPricesTable(String pricesTable) {
		this.pricesTable = pricesTable;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
