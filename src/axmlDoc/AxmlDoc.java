package axmlDoc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class AxmlDoc extends AbstractAxmlDoc {

	private HandleStrategy handleStrategy;
	private XPath xPath;

	public enum ReturnType {
		STRING, XMLTREE
	};

	public enum HandleStrategy {
		LAZY, LAZY_PERSIST, LAZY_PERSIST_WITH_EXCLUSION, EAGGER
	}

	public AxmlDoc(Document documentToDecorate) {
		super(documentToDecorate);
	}

	public AxmlDoc(File xmlFile, HandleStrategy handleStrategy, ReturnType returnType) {
		super(xmlFile, handleStrategy, returnType);
		XPathFactory xPathFactory = XPathFactory.newInstance();
		this.xPath = xPathFactory.newXPath();
	}

	public List<Node> verificarChamadasRemotas() {
		XPath xPath = XPathFactory.newInstance().newXPath();
		String busca = "//axml:call";

		// read a string value
		// String email = xPath.compile(busca).evaluate(this.getXml());

		// read an xml node using xpath
		// Node node = (Node) xPath.compile(expression).evaluate(xmlDocument,
		// XPathConstants.NODE);

		// read a nodelist using xpath
		// NodeList nodeList = (NodeList)
		// xPath.compile(expression).evaluate(xmlDocument,
		// XPathConstants.NODESET);

		// Busca todas os nos com chamadas a serviço remoto
		NodeList remoteCalls = this.getElementsByTagName("axml:call");

		List<Node> remoteCallsParents = new ArrayList<Node>();
		for (int n = remoteCalls.getLength(); n > 0; n--) {
			Node noPai = remoteCalls.item(n - 1).getParentNode();
			remoteCallsParents.add(noPai);
		}

		return remoteCallsParents;

	}

	public Node materializarElemento(Node no, ReturnType returnType) throws IOException,
			SAXException, ParserConfigurationException {
		Element chamadaRemota = (Element) no;
		// captura endereço do webservice
		URL enderecoRemoto = new URL(chamadaRemota.getAttribute("service"));

		// estabelece conexao
		HttpURLConnection conexao = (HttpURLConnection) enderecoRemoto.openConnection();

		// recebe dados
		InputStream content = conexao.getInputStream();

		// cria documento XML com dados recebidos
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document retornoXML = dBuilder.parse(content);

		// retorna objeto Node com dados obtidos
		if (returnType.equals(ReturnType.STRING)) {
			no.setTextContent(retornoXML.getDocumentElement().getFirstChild().getTextContent());
		} else if (returnType.equals(ReturnType.XMLTREE)) {
			no.appendChild(retornoXML.getDocumentElement());
		}

		return no;
	}

	/**
	 * getTextFromSingleNode
	 * 
	 * Receives a xPath string expression that refers to a unique node element
	 * from DOM document, and return the string from text child element. If a
	 * remote call exists, this node will be materialized.
	 * 
	 * @param xPathString
	 *            - String xPath expression
	 * @return String from text child or from materialized remote call.
	 * @throws AxmlDocException
	 */
	public String getTextFromSingleNode(String xPathString) throws AxmlDocException {
		AxmlNode axmlNode;
		Node node;
		XPathExpression xPathExpression;

		try {
			xPathExpression = xPath.compile(xPathString);
			node = (Node) xPathExpression.evaluate(this.decoratedDocument, XPathConstants.NODE);
			axmlNode = new AxmlNode((Element) node);
		} catch (XPathExpressionException xpe) {
			throw new AxmlDocException(
					"xPath error. Check if your expression is ok and if refer to a only one node element.");
		}

		return axmlNode.getTextContent();
	}
}
