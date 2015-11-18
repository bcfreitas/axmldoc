package axmlDoc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class AxmlDoc extends AbstractAxmlDoc {

	private XPath xPath;

	/**
	 * ReturnType - defines the return type of read data from remote call: TEXT
	 * - Data read from remote call returns as string of text. XML - Data read
	 * from remote call returns as XML structure, defined by provider.
	 * 
	 * @author bruno_e_elis
	 *
	 */
	public enum ReturnType {
		TEXT, XML
	};

	/**
	 * HandleStrategy - defines the handle strategy to manage XML File and
	 * remote calls: EAGER - Materialize all remote calls when create axmlDoc.
	 * EAGER_PERSIST - Materialize all remote calls when create axmlDoc and
	 * replace original XML File. LAZY - On demand materialize. Web access is
	 * always needed to materialize. LAZY_PERSIST - On demand materialize and
	 * persist read data in original XML File. LAZY_PERSIST_WITH_EXCLUSION - On
	 * demand materialize and persist read data in original XML File, but only
	 * one per round, reverting previous materialized data to respective remote
	 * call.
	 * 
	 * @author bruno_e_elis
	 *
	 */
	public enum HandleStrategy {
		LAZY, LAZY_PERSIST, LAZY_PERSIST_WITH_EXCLUSION, EAGER, EAGER_PERSIST
	}

	public AxmlDoc(Document documentToDecorate) {
		super(documentToDecorate);
	}

	public AxmlDoc(File xmlFile, HandleStrategy handleStrategy, ReturnType returnType) {
		super(xmlFile, handleStrategy, returnType);
		XPathFactory xPathFactory = XPathFactory.newInstance();
		this.xPath = xPathFactory.newXPath();
		this.tFactory = TransformerFactory.newInstance();
		switch (this.handleStrategy) {
		case EAGER:
			this.materializeAllRemoteCalls(this.verificarChamadasRemotas());
			break;
		case EAGER_PERSIST:
			this.materializeAllRemoteCalls(this.verificarChamadasRemotas());
			this.persist();
			break;
		default:

		}
	}

	public NodeList verificarChamadasRemotas() {
		// XPath xPath = XPathFactory.newInstance().newXPath();
		// String busca = "//axml:call";

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

		// List<Node> remoteCallsParents = new ArrayList<Node>();
		// for (int n = remoteCalls.getLength(); n > 0; n--) {
		// Node noPai = remoteCalls.item(n - 1).getParentNode();
		// remoteCallsParents.add(noPai);
		// }

		return remoteCalls;

	}

	public void materializeAllRemoteCalls(NodeList nodeList) {
		while (nodeList.getLength() > 0) {
			AxmlNode axmlNode = new AxmlNode((Element) nodeList.item(0).getParentNode());
			axmlNode.getTextContent(this.returnType);
		}
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
		if (returnType.equals(ReturnType.TEXT)) {
			no.setTextContent(retornoXML.getDocumentElement().getFirstChild().getTextContent());
		} else if (returnType.equals(ReturnType.XML)) {
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

		String retorno;

		// Checking handle strategy
		switch (this.handleStrategy) {
		case LAZY_PERSIST:
			retorno = axmlNode.getTextContent(this.returnType);
			this.persist();
			break;
		case LAZY_PERSIST_WITH_EXCLUSION:
			this.revertMaterializedData();
			retorno = axmlNode.getTextContent(this.returnType);
			this.persist();
			break;
		default:
			retorno = axmlNode.getTextContent(this.returnType);
		}

		return retorno;
	}

	public void revertMaterializedData() {
		try {
			// Get all nodes with the attribute dataFrom
			String xPathString = "//*[@dataFrom]";
			XPathExpression expr = this.xPath.compile(xPathString);
			NodeList nodesWithMaterializedData = (NodeList) expr.evaluate(this.decoratedDocument,
					XPathConstants.NODESET);
			// Iterate in that list
			for (int i = 0; i < nodesWithMaterializedData.getLength(); i++) {
				// Change each node data from the list for a new Node with
				// remote
				// call.
				Element nodeToRevertData = (Element) nodesWithMaterializedData.item(i);

				// -- getting the remote call URI.
				String remoteCallURI = nodeToRevertData.getAttribute("dataFrom");

				// -- removing the dataFrom Atribute.
				nodeToRevertData.removeAttribute("dataFrom");

				// -- removing all child nodes from node to revert.
				this.removeAllChildNodes(nodeToRevertData);

				// -- creating the new element with the remote call.
				Element remoteCallNode = this.decoratedDocument.createElement("axml:call");
				remoteCallNode.setAttribute("service", remoteCallURI);
				remoteCallNode.setTextContent("");
				// -- adding the new node to document
				nodeToRevertData.appendChild(remoteCallNode);
			}

		} catch (XPathExpressionException e) {
			throw new AxmlDocException("Error reverting previous materialized data.");
		}
	}

	public void persist() {
		try {
			Transformer transformer = null;
			try {
				transformer = tFactory.newTransformer();
			} catch (TransformerConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			PrintWriter writer = new PrintWriter(this.xmlFilePath, "UTF-8");
			DOMSource source = new DOMSource(this.decoratedDocument);
			StreamResult result = new StreamResult(writer);

			transformer.transform(source, result);
			writer.close();

		} catch (IOException | TransformerException e) {
			throw new AxmlDocException("Output error while trying to save data received.");
		}
	}

	private void removeAllChildNodes(Node node) {
		NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node childNode = childNodes.item(i);
			if (childNode.hasChildNodes()) // edit to remove children of
											// children
			{
				removeAllChildNodes(childNode);
				node.removeChild(childNode);
			} else
				node.removeChild(childNode);
		}
	}
}
