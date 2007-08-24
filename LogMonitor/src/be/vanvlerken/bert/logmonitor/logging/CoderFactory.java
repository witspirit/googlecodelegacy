/**
 * @author wItspirit
 * 1-nov-2003
 * CoderFactory.java
 */

package be.vanvlerken.bert.logmonitor.logging;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.xml.sax.SAXException;

import be.vanvlerken.bert.xmlparser2.IXmlTagHandler;
import be.vanvlerken.bert.xmlparser2.TextXmlHandler;
import be.vanvlerken.bert.xmlparser2.XmlInterpreter;
import be.vanvlerken.bert.xmlparser2.XmlIterationHandler;
import be.vanvlerken.bert.xmlparser2.XmlSequenceHandler;

/**
 * Produces coders
 */
public class CoderFactory
{
    private Map decoders;
    private Map encoders;

    private ILogEntryDecoder defaultDecoder;
    private ILogEntryEncoder defaultEncoder;

    public CoderFactory() throws SAXException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException
    {
        decoders = new HashMap();
        encoders = new HashMap();

        populateMaps();
    }

    /**
     * Populates the maps with all known coders
     */
    private void populateMaps() throws SAXException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException
    {
        // The original way, was just hardwired...
        //        /* Encoders */
        //        encoders.put(NullEncoder.class.getName(), new NullEncoder());
        //        encoders.put(NormalEncoder.class.getName(), new NormalEncoder());
        //
        //        /* Decoders */
        //        decoders.put(NullDecoder.class.getName(), new NullDecoder());
        //        decoders.put(NormalDecoder.class.getName(), new NormalDecoder());
        //        decoders.put(ProxyMonitorDecoder.class.getName(), new ProxyMonitorDecoder());
        //        decoders.put(LogMessageDecoder.class.getName(), new LogMessageDecoder());
        //        decoders.put(LogMessageWithFileInfoDecoder.class.getName(), new LogMessageWithFileInfoDecoder());

        /* The new way, is reading our configuration from an XmlFile */
        /* Prepare the Xml Interpreter... Defines the XML Schema */
        TextXmlHandler defaultDecoderHandler = new TextXmlHandler("DefaultDecoder");
        TextXmlHandler defaultEncoderHandler = new TextXmlHandler("DefaultEncoder");

        TextXmlHandler encoderHandler = new TextXmlHandler("Encoder");
        TextXmlHandler decoderHandler = new TextXmlHandler("Decoder");

        XmlIterationHandler encoderIterationHandler = new XmlIterationHandler(encoderHandler, 1, XmlIterationHandler.UNBOUND);
        XmlIterationHandler decoderIterationHandler = new XmlIterationHandler(decoderHandler, 1, XmlIterationHandler.UNBOUND);

        XmlSequenceHandler encodersHandler = new XmlSequenceHandler("Encoders");
        encodersHandler.addTagHandler(encoderIterationHandler);

        XmlSequenceHandler decodersHandler = new XmlSequenceHandler("Decoders");
        decodersHandler.addTagHandler(decoderIterationHandler);

        XmlSequenceHandler rootHandler = new XmlSequenceHandler("Coders");
        rootHandler.addTagHandler(defaultEncoderHandler);
        rootHandler.addTagHandler(defaultDecoderHandler);
        rootHandler.addTagHandler(encodersHandler);
        rootHandler.addTagHandler(decodersHandler);

        /* Time to read the XML file */
        FileInputStream fileStream = new FileInputStream(new File(System.getProperty("user.dir") + "/coders.xml"));

        XmlInterpreter xmlInterpreter = new XmlInterpreter(fileStream, rootHandler);
        xmlInterpreter.analyze();

        /* Data is available */
        /* Read the default encoder/decoder */
        defaultEncoder = (ILogEntryEncoder) ClassLoader.getSystemClassLoader().loadClass(defaultEncoderHandler.getText()).newInstance();
        defaultDecoder = (ILogEntryDecoder) ClassLoader.getSystemClassLoader().loadClass(defaultDecoderHandler.getText()).newInstance();

        IXmlTagHandler handlers[];
        
        /* Read the encoders */
        handlers = encoderIterationHandler.getChildHandlers();
        for (int i = 0; i < handlers.length; i++)
        {
            TextXmlHandler textHandler = (TextXmlHandler) handlers[i];
            String className = textHandler.getText();
            Class realClass = ClassLoader.getSystemClassLoader().loadClass(className);
            encoders.put(className, realClass.newInstance());
        }

        /* Read the decoders */
        handlers = decoderIterationHandler.getChildHandlers();
        for (int i = 0; i < handlers.length; i++)
        {
            TextXmlHandler textHandler = (TextXmlHandler) handlers[i];
            String className = textHandler.getText();
            Class realClass = ClassLoader.getSystemClassLoader().loadClass(className);
            decoders.put(className, realClass.newInstance());
        }

    }

    public ILogEntryDecoder getDecoder(String coderId) throws ClassNotFoundException
    {
        ILogEntryDecoder decoder = (ILogEntryDecoder) decoders.get(coderId);
        if (decoder == null)
        {
            throw new ClassNotFoundException("Could not find " + coderId + " in the list of available decoders");
        }
        return decoder;
    }

    public ILogEntryDecoder getDefaultDecoder()
    {
        return defaultDecoder;
    }

    public ILogEntryEncoder getEncoder(String coderId) throws ClassNotFoundException
    {
        ILogEntryEncoder encoder = (ILogEntryEncoder) encoders.get(coderId);
        if (encoder == null)
        {
            throw new ClassNotFoundException("Could not find " + coderId + " in the list of available encoders");
        }
        return encoder;
    }

    public ILogEntryEncoder getDefaultEncoder()
    {
        return defaultEncoder;
    }

    public String[] getAvailableDecoders()
    {
        String avDecoders[];
        Set decSet = decoders.keySet();

        avDecoders = new String[decSet.size()];
        Iterator it = decSet.iterator();
        int i = 0;
        while (it.hasNext())
        {
            avDecoders[i] = (String) it.next();
            i++;
        }

        return avDecoders;
    }

    public String[] getAvailableEncoders()
    {
        String avEncoders[];
        Set encSet = encoders.keySet();

        avEncoders = new String[encSet.size()];
        Iterator it = encSet.iterator();
        int i = 0;
        while (it.hasNext())
        {
            avEncoders[i] = (String) it.next();
            i++;
        }

        return avEncoders;
    }

    public void printCoders()
    {
        System.out.println("Available encoders:");
        Set coderSet = encoders.keySet();
        Iterator it = coderSet.iterator();
        while (it.hasNext())
        {
            System.out.println((String) it.next());
        }

        System.out.println("Available decoders:");
        coderSet = decoders.keySet();
        it = coderSet.iterator();
        while (it.hasNext())
        {
            System.out.println((String) it.next());
        }
    }
}
