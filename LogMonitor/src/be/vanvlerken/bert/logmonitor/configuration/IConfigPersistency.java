/**
 * @author wItspirit
 * 22-feb-2003
 * IConfigPersistency.java
 */
package be.vanvlerken.bert.logmonitor.configuration;

import java.lang.String;
import java.io.Writer;
import be.vanvlerken.bert.xmlparser2.IXmlTagHandler;
import java.io.IOException;

/**
 * Interface that defines the methods required to support Configuration persistency
 */
public interface IConfigPersistency
{
	IXmlTagHandler getXmlTagHandler();
	void save(Writer writer, String prefix) throws IOException;
}