/**
 * @author wItspirit
 * 13-apr-2003
 * AboutMessage.java
 */
package be.vanvlerken.bert.components.gui.applicationwindow;

/**
 * This class holds the information that will be displayed in the 'About...' box
 * It is implemented as a Singleton
 * When an application wants to change the contents of the about box, it retrieves
 * an instance of this AboutMessage via the getInstance method and sets the values
 * it wants. After that, it can forget about the AboutMessage. The next time the 
 * About dialog is opened, it will display the new contents.
 */
public class AboutMessage
{
    private static AboutMessage instance;
    
    private String programName;
    private String version;
    private String author;
    private String aboutMessage;
    private String copyrightMessage;
    
    public static AboutMessage getInstance()
    {
        if ( instance == null )
        {
            instance = new AboutMessage();
        }
        return instance;
    }
    
    protected AboutMessage()
    {
        programName = "ApplicationWindow";
        version = "0.1";
        author = "Bert Van Vlerken";
        aboutMessage = "This is a great base component for building GUIs rapidly";
        copyrightMessage = "All copyrights remain with the author";
    }
    
    public String getFormattedMessage()
    {
        String formattedMessage = 
            programName +"\n" +
            "Version "+version+"\n" +
            "\n" +
            "Author: "+author+"\n" +
            "\n"+
            aboutMessage+"\n"+
            copyrightMessage+"\n";
        return formattedMessage;
    } 
   
    /**
     * Returns the aboutMessage.
     * @return String
     */
    public String getAboutMessage()
    {
        return aboutMessage;
    }

    /**
     * Returns the author.
     * @return String
     */
    public String getAuthor()
    {
        return author;
    }

    /**
     * Returns the copyrightMessage.
     * @return String
     */
    public String getCopyrightMessage()
    {
        return copyrightMessage;
    }

    /**
     * Returns the programName.
     * @return String
     */
    public String getProgramName()
    {
        return programName;
    }

    /**
     * Returns the version.
     * @return String
     */
    public String getVersion()
    {
        return version;
    }

    /**
     * Sets the aboutMessage.
     * @param aboutMessage The aboutMessage to set
     */
    public void setAboutMessage(String aboutMessage)
    {
        this.aboutMessage = aboutMessage;
    }

    /**
     * Sets the author.
     * @param author The author to set
     */
    public void setAuthor(String author)
    {
        this.author = author;
    }

    /**
     * Sets the copyrightMessage.
     * @param copyrightMessage The copyrightMessage to set
     */
    public void setCopyrightMessage(String copyrightMessage)
    {
        this.copyrightMessage = copyrightMessage;
    }

    /**
     * Sets the programName.
     * @param programName The programName to set
     */
    public void setProgramName(String programName)
    {
        this.programName = programName;
    }

    /**
     * Sets the version.
     * @param version The version to set
     */
    public void setVersion(String version)
    {
        this.version = version;
    }

}
