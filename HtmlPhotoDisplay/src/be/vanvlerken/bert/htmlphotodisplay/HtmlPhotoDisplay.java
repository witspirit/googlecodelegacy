/**
 * @author wItspirit
 * Jul 10, 2004
 * HtmlPhotoDisplay.java
 */

package be.vanvlerken.bert.htmlphotodisplay;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

/**
 * Template parser for creating image output
 */
public class HtmlPhotoDisplay implements Runnable
{
    private static final Logger log = Logger.getLogger(HtmlPhotoDisplay.class);

    /**
     * @param args
     */
    public HtmlPhotoDisplay(String[] args)
    {
        try
        {
            PropertyConfigurator.configure("log4j.properties");
            // Ignore arguments
            Velocity.init();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        HtmlPhotoDisplay photoDisplay = new HtmlPhotoDisplay(args);
        photoDisplay.run();
    }

    /**
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
        Properties props = new Properties();
        try
        {
            props.load(new FileInputStream("photodisplay.properties"));
            String template = props.getProperty("templateFile");
            String outputFile = props.getProperty("outputFile");
            String timeout = props.getProperty("secondsBetweenRefresh");
            String imageFolder = props.getProperty("imageFolder");

            List images = fetchImages(imageFolder);
            if (images.size() <= 0)
            {
                log.warn("No images found.");
            }
            else
            {
                parseTemplate(template, outputFile, timeout, images);
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @param template
     * @param outputFile
     * @param timeout
     * @param images
     */
    private void parseTemplate(String template, String outputFile,
            String timeout, List images)
    {
        try
        {
            String imageStr = generateImageString(images);
            String firstSlide = "\"" + ((File) images.get(0)).toURL() + "\"";

            VelocityContext vCtx = new VelocityContext();
            vCtx.put("timeout", timeout);
            vCtx.put("images", imageStr);
            vCtx.put("firstSlide", firstSlide);

            Writer writer = new FileWriter(outputFile);

            Velocity.mergeTemplate(template, "UTF-8",vCtx, writer);
            
            writer.flush();
            writer.close();

        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (ResourceNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (ParseErrorException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @param images
     * @return
     */
    private String generateImageString(List images)
    {
        StringBuffer imageStr = new StringBuffer();
        Iterator it = images.iterator();
        while (it.hasNext())
        {
            File imageFile = (File) it.next();
            try
            {
                imageStr.append("\"" + imageFile.toURL() + "\",");
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
        }
        imageStr.deleteCharAt(imageStr.length() - 1);
        return imageStr.toString();
    }

    /**
     * @param imageFolder
     * @return
     */
    private List fetchImages(String imageFolder)
    {
        String[] imageFolders = imageFolder.split(";");
        List images = new ArrayList();
        for (int i = 0; i < imageFolders.length; i++)
        {
            File imageDir = new File(imageFolders[i]);
            log.info("Searching for images in " + imageDir.getAbsolutePath());

            if (imageDir.isDirectory())
            {
                log.info(imageDir.getAbsolutePath() + " is a folder");
                File[] imageFiles = imageDir.listFiles(new ImageFileFilter());
                log.info(imageFiles.length + " images found.");
                for (int j = 0; j < imageFiles.length; j++)
                {
                    images.add(imageFiles[j]);
                }
            }
        }
        return images;
    }
}