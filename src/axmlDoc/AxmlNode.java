package axmlDoc;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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

	@Override
	public String getTextContent() throws DOMException {
		String retorno = null;
		NodeList remoteCallList;
		Node remoteCall;
		try {
			remoteCallList = (NodeList) ((Element)this.decoratedNode).getElementsByTagName("axml:call");
		} catch (NullPointerException npe) {
			remoteCallList = null;
		}

		if (remoteCallList == null || remoteCallList.getLength() == 0) {
			retorno = this.decoratedNode.getTextContent();
			remoteCallList = null;
		} else {
			for (int remoteCallCount = 0; remoteCallCount < remoteCallList.getLength(); remoteCallCount++) {
				remoteCall = (Node) remoteCallList.item(remoteCallCount);
				try {
					
					//TODO - parametrizar/arrumar
				    Document document = this.decoratedNode.getOwnerDocument();
				    materializeNode(remoteCall, ReturnType.STRING);
				    //this.decoratedNode.replaceChild(remoteCall, materializedNode);
				    
					

				    
				    //document.replaceChild(materializeNode(remoteCall, ReturnType.STRING), this.decoratedNode);

				    // Use a Transformer for output
				    TransformerFactory tFactory =
				    TransformerFactory.newInstance();
				    Transformer transformer = null;
					try {
						transformer = tFactory.newTransformer();
					} catch (TransformerConfigurationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					PrintWriter writer = new PrintWriter("livro2.txt", "ISO-8859-1");
				    DOMSource source = new DOMSource(document);
				    StreamResult result = new StreamResult(writer);
				    try {
						transformer.transform(source, result);
						writer.close();
					} catch (TransformerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
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

		}
		return retorno;
	}

	public void materializeNode(Node no, ReturnType returnType) throws IOException, SAXException,
			ParserConfigurationException {
		//pegando o Node filho do tipo Texto
		Node textNode = no.getFirstChild();
		
		//pegando o no pai do no a ser materializado
		Node parentNode = no.getParentNode();
		
		
		Element chamadaRemota = (Element) no;
		// captura endere�o do webservice
		URL enderecoRemoto = new URL(chamadaRemota.getAttribute("service"));

		// estabelece conexao
		HttpURLConnection conexao = (HttpURLConnection) enderecoRemoto.openConnection();

//		// recebe dados
//		InputStream content = conexao.getInputStream();
//
//		// cria documento XML com dados recebidos
//		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//		Document retornoXML = dBuilder.parse(content);

		// retorna objeto Node com dados obtidos
		if (returnType.equals(ReturnType.STRING)) {
//			textNode.setTextContent(retornoXML.getDocumentElement().getFirstChild().getTextContent());
			textNode.setTextContent("funfou");
			parentNode.removeChild(no);
			parentNode.appendChild(textNode);
		
		} else if (returnType.equals(ReturnType.XMLTREE)) {
//			no.appendChild(retornoXML.getDocumentElement());
		}

	}

}
