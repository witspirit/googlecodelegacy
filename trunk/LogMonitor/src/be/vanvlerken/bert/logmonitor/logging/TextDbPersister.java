/**
 * @author wItspirit
 * 16-feb-2003
 * TextDbPersister.java
 */
package be.vanvlerken.bert.logmonitor.logging;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * Will take care of the loading and saving of the Logging Database
 */
public class TextDbPersister implements IDatabasePersister
{
    private static Logger logger = Logger.getLogger("be.vanvlerken.bert.logmonitor");
    
    private ILogDatabase logDb;
    private File saveFile;
    
    private ILogEntryDecoder    decoder;
    private ILogEntryEncoder    encoder;
        
    public TextDbPersister(ILogDatabase logDb)
    {
        this.logDb = logDb;
        saveFile = null;
        
        decoder = new NormalDecoder();
        encoder = new NormalEncoder();
    }
    
    public void loadDb(File file) throws FileNotFoundException, IOException
    {
        logger.info("Loading Database from "+file);
        
        try
        {
            BufferedReader fileReader = new BufferedReader(new FileReader(file));
            
            String logLine = fileReader.readLine();
            ILogEntry logEntry;
            
            while ( logLine != null )
            {
                logEntry = decoder.decodeLogEntry(logLine);
                logDb.add(logEntry);
                logLine = fileReader.readLine();
            }
            
            fileReader.close();
            saveFile = file;
            
            logger.info("Database succesfully loaded from "+file);
            
		}
		catch (FileNotFoundException e)
		{
            logger.error("Could not read from "+file+". "+e.getMessage());
            throw e;
		} 
        catch (IOException ie)
		{
            logger.error("Error occured while reading "+file+". "+ie.getMessage());
            throw ie;
		}
    }
	
    
    public void save() throws FileNotFoundException, IOException
    {
        if ( saveFile == null )
        {
            logger.warn("Could not save configuration, because no file was selected");
            throw new FileNotFoundException("No file selected for saving");
        }
        else
        {
            saveAs(saveFile);
        }
    }
    
    public void saveAs(File file) throws IOException
    {
        logger.info("Saving Database to "+file);
        
        try
		{			
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file));
            
            ILogEntry logEntry;
            String logLine;
            for (int i=0; i < logDb.getSize(); i++)
            {
                logEntry = logDb.get(i);
                logLine = encoder.encodeLogEntry(logEntry);
                fileWriter.write(logLine+"\n");
            }
            
            fileWriter.flush();
            fileWriter.close();
            
            logger.info("Database saved succesfully to "+file);
		}
		catch (IOException e)
		{
            logger.error("Error occured while writing to "+file+". "+e.getMessage());
            throw e;
		}
    }   
}


