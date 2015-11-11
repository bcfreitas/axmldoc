package desktop.client;

import java.io.File;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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

public class ReadXMLFile {

	public static void main(String argv[]) {

		try {

			File fXmlFile = new File("livro.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("livro");

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
				AxmlDoc axmlDoc = new AxmlDoc(fXmlFile, fXmlFile);

				// read a string value
				// System.out.println(xPath.compile(pathExpression).evaluate(doc));

				// Usuário escolhe o capítulo 1 para leitura:
				pathExpression = xPath.compile("/livro/capitulo[1]");
				node = (Node) pathExpression.evaluate(doc, XPathConstants.NODE);
				System.out.println("");
				System.out.println("---> Usuário escolhe o capítulo 1 para leitura:");
				System.out.println(node.getTextContent());
				System.out.println("");

				// Usuário escolhe o capítulo 2 para leitura:
				pathExpression = xPath.compile("/livro/capitulo[2]");
				node = (Node) pathExpression.evaluate(doc, XPathConstants.NODE);
				System.out.println("");
				System.out.println("---> Usuário escolhe o capítulo 2 para leitura:");

				pathExpression = xPath.compile("/livro/capitulo[2]/*[name()='axml:call']");
				remoteCallList = (NodeList) pathExpression.evaluate(doc, XPathConstants.NODESET);
				if (remoteCallList != null && remoteCallList.getLength() == 0) {
					System.out.println(node.getTextContent());
				} else {
					for (int remoteCallCount = 1; remoteCallCount <= remoteCallList.getLength(); remoteCallCount++) {
						pathExpression = xPath.compile("/livro/capitulo[2]/axml:call["
								+ remoteCallCount + "]");
						remoteCall = (Node) pathExpression.evaluate(doc, XPathConstants.NODE);
						System.out.println(axmlDoc.materializarElemento(node, ReturnType.STRING).getTextContent());
					}

				}
				System.out.println("");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}