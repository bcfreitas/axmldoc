package axmlDoc;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import axmlDoc.AxmlDoc.ReturnType;

public class AxmlNode extends AbstractAxmlNode {

	public AxmlNode(Node nodeToDecorate) {
		super(nodeToDecorate);
	}

	/*
	 * If used out of axmlDoc context, use String.
	 * 
	 * @see axmlDoc.AbstractAxmlNode#getTextContent()
	 */
	@Override
	public String getTextContent() throws DOMException {
		return getTextContent(ReturnType.TEXT);
	}

	public void materializeNode(Node no, ReturnType returnType) throws IOException, SAXException,
			ParserConfigurationException {

		// pegando o no pai do no a ser materializado
		Element parentNode = (Element) no.getParentNode();

		Element chamadaRemota = (Element) no;
		// captura endereço do webservice
		URL enderecoRemoto = new URL(chamadaRemota.getAttribute("service"));

		// estabelece conexao
		HttpURLConnection conexao = (HttpURLConnection) enderecoRemoto.openConnection();

		 // recebe dados
		 InputStream content = conexao.getInputStream();
		
		 // cria documento XML com dados recebidos
		 DocumentBuilderFactory dbFactory =
		 DocumentBuilderFactory.newInstance();
		 DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		 Document retornoXML = dBuilder.parse(content);

		// Saving the remoteCall in parent node.
		parentNode.setAttribute("dataFrom", chamadaRemota.getAttribute("service"));
		
		// retorna objeto Node com dados obtidos
		if (returnType.equals(ReturnType.TEXT)) {
			String receivedData = retornoXML.getDocumentElement().getFirstChild().getTextContent();
			Node receivedDataNode = this.decoratedNode.getOwnerDocument().createTextNode(receivedData);
			parentNode.removeChild(no);
			parentNode.appendChild(receivedDataNode);

		} else if (returnType.equals(ReturnType.XML)) {
			// no.appendChild(retornoXML.getDocumentElement());
		}

	}

	public String getTextContent(ReturnType returnType) {
		String retorno = null;
		NodeList remoteCallList;
		Node remoteCall;
		try {
			// Searching for remote calls in children of the node.
			remoteCallList = (NodeList) ((Element) this.decoratedNode)
					.getElementsByTagName("axml:call");
		} catch (NullPointerException npe) {
			remoteCallList = null;
		}

		if (remoteCallList == null || remoteCallList.getLength() == 0) {
			// If not have any remote call in child elements, return the text
			// node;
			retorno = this.decoratedNode.getTextContent();
			remoteCallList = null;
		} else {
			for (int remoteCallCount = 0; remoteCallCount < remoteCallList.getLength(); remoteCallCount++) {
				remoteCall = (Element) remoteCallList.item(remoteCallCount);
				try {

					// TODO - parametrizar/arrumar
					Document document = this.decoratedNode.getOwnerDocument();
					materializeNode(remoteCall, returnType);
					// this.decoratedNode.replaceChild(remoteCall,
					// materializedNode);

					// document.replaceChild(materializeNode(remoteCall,
					// ReturnType.STRING), this.decoratedNode);



				} catch (IOException e) {
					throw new DOMException(DOMException.NOT_FOUND_ERR,
							"Web service call failed. Remote call: "
									+ remoteCall.getAttributes().getNamedItem("service")
											.getTextContent());
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					throw new DOMException(DOMException.INVALID_STATE_ERR,
							"Invalid XML return from web service call. Remote call: "
									+ remoteCall.getAttributes().getNamedItem("service"));
				} catch (ParserConfigurationException e) {
					throw new DOMException(DOMException.INVALID_STATE_ERR,
							"Internal error. ParserConfigurationException");
				}
			}
			
			retorno = this.decoratedNode.getTextContent();

		}
		return retorno;
	}

}
