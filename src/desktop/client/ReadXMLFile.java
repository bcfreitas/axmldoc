package desktop.client;

import java.io.File;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import axmlDoc.AxmlDoc;
import axmlDoc.AxmlDoc.ReturnType;
import axmlDoc.AxmlNode;
import axmlDoc.AxmlDoc.HandleStrategy;

public class ReadXMLFile {

	public static void main(String argv[]) {

		try {

			File fXmlFile = new File("livro.xml");
			AxmlDoc axmlDoc = new AxmlDoc(fXmlFile, HandleStrategy.LAZY, ReturnType.STRING);

			axmlDoc.getDocumentElement().normalize();

			NodeList nList = axmlDoc.getElementsByTagName("livro");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					// Exibe nome do Livro
					System.out.println("Nome do livro: " + eElement.getAttribute("titulo"));

					// Exibe lista de capítulos do livro
					NodeList capitulos = nNode.getChildNodes();
					for (int c = 0; c < capitulos.getLength(); c++) {
						Node capituloNode = capitulos.item(c);
						if (capituloNode.getNodeType() == Node.ELEMENT_NODE) {
							Element capitulo = (Element) capituloNode;
							System.out.println("Capitulo " + capitulo.getAttribute("numero") + ": "
									+ capitulo.getAttribute("titulo"));
						}
					}
				}

				// Iniciando simulação de uso do usuário

				XPathFactory xPathFactory = XPathFactory.newInstance();
				XPath xPath = xPathFactory.newXPath();
				XPathExpression pathExpression;
				Node node;
				Node remoteCall;
				NodeList remoteCallList;

				// read a string value
				// System.out.println(xPath.compile(pathExpression).evaluate(doc));

				// Usuário escolhe o capítulo 1 para leitura:
//				pathExpression = xPath.compile("/livro/capitulo[1]");
//				node = (Node) pathExpression.evaluate(axmlDoc, XPathConstants.NODE);
//				AxmlNode axmlNode = new AxmlNode((Element) node);
//				System.out.println("");
//				System.out.println("---> Usuário escolhe o capítulo 1 para leitura:");
//				System.out.println(axmlNode.getTextContent());
//				System.out.println("");
				
				System.out.println(axmlDoc.getTextFromSingleNode("/livro/capitulo[1]"));

//				// Usuário escolhe o capítulo 2 para leitura:
//				pathExpression = xPath.compile("/livro/capitulo[2]");
//				node = (Node) pathExpression.evaluate(axmlDoc, XPathConstants.NODE);
//				axmlNode = new AxmlNode(node);
//				System.out.println("");
//				System.out.println("---> Usuário escolhe o capítulo 2 para leitura:");
//				System.out.println(axmlNode.getTextContent());
				System.out.println(axmlDoc.getTextFromSingleNode("/livro/capitulo[2]"));
				// if (remoteCallList != null && remoteCallList.getLength() ==
				// 0) {
				// System.out.println(node.getTextContent());
				// } else {
				// for (int remoteCallCount = 1; remoteCallCount <=
				// remoteCallList.getLength(); remoteCallCount++) {
				// pathExpression =
				// xPath.compile("/livro/capitulo[2]/axml:call["
				// + remoteCallCount + "]");
				// remoteCall = (Node) pathExpression.evaluate(doc,
				// XPathConstants.NODE);
				// System.out.println(axmlDoc.materializarElemento(node,
				// ReturnType.STRING).getTextContent());
				// }
				//
				// }
			    TransformerFactory tFactory =
					    TransformerFactory.newInstance();
					    Transformer transformer = null;
						try {
							transformer = tFactory.newTransformer();
						} catch (TransformerConfigurationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				
				System.out.println("");
			    DOMSource source = new DOMSource(axmlDoc);
			    StreamResult result = new StreamResult(System.out);
			    try {
					transformer.transform(source, result);
				} catch (TransformerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}