package axmlDoc;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class AxmlDocDemo {
	public static void main(String args[]) throws ParserConfigurationException, SAXException, IOException{
		File fXmlFile = new File("staff.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		AxmlDoc doc = (AxmlDoc) dBuilder.parse(fXmlFile);
		
		//Document axmlTeste = new AxmlDoc(doc);
	}

}
