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
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class AxmlDoc extends AbstractAxmlDoc {

	public enum ReturnType {
		STRING, XMLTREE
	};

	public enum HandleStrategy {
		LAZY,LAZY_PERSIST,LAZY_PERSIST_WITH_EXCLUSION, EAGGER 
	}
	
	public HandleStrategy handleStrategy;
	
	public AxmlDoc(Document documentToDecorate) {
		super(documentToDecorate);
	}
	
	public AxmlDoc(File xmlFile, HandleStrategy handleStrategy){
		super(xmlFile, handleStrategy);
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
}
