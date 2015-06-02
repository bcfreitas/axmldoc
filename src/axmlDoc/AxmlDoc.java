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

public class AxmlDoc {
		private Document xml;
		private Document schema;
		
		public AxmlDoc(File xml, File schema){	
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			Document doc = null, doc2 = null;
			try {
				dBuilder = dbFactory.newDocumentBuilder();
				doc = dBuilder.parse(xml);
				doc2 = dBuilder.parse(schema);

			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
			this.setXml(doc);
			this.setSchema(doc2);
		}

		public Document getXml() {
			return xml;
		}

		public void setXml(Document xml) {
			this.xml = xml;
		}

		public Document getSchema() {
			return schema;
		}

		public void setSchema(Document schema) {
			this.schema = schema;
		}
		
		public List<Node> verificarChamadasRemotas(){
			XPath xPath =  XPathFactory.newInstance().newXPath();
			String busca = "//axml:call";
			 
			//read a string value
			//	String email = xPath.compile(busca).evaluate(this.getXml());
			 
			//read an xml node using xpath
			//Node node = (Node) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODE);
			 
			//read a nodelist using xpath
			//NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
			
			//Busca todas os nos com chamadas a serviço remoto
			NodeList chamadasRemotas = this.getXml().getElementsByTagName("axml:call");
			System.out.println("tamanho: " + chamadasRemotas.getLength());
			
			List<Node> nosComCR = new ArrayList<Node>();
			for(int n = chamadasRemotas.getLength(); n>0; n-- ){
				System.out.println("aaa " + chamadasRemotas.item(n-1).getParentNode());
				Node noPai = chamadasRemotas.item(n-1).getParentNode();
				nosComCR.add(noPai);
			}
			
			return nosComCR;

		}

		public Node materializarElemento(Node no) throws IOException, SAXException, ParserConfigurationException{
			Element elemento = (Element)no;
			//captura endereço do webservice
			Element chamadaRemota = (Element)elemento.getElementsByTagName("axml:call").item(0);
			URL enderecoRemoto = new URL(chamadaRemota.getAttribute("service"));
			System.out.println(enderecoRemoto.toString());
			
			//estabelece conexao
			HttpURLConnection conexao = (HttpURLConnection) enderecoRemoto.openConnection();
			
			//recebe dados
			InputStream content = conexao.getInputStream();
			
			//cria documento XML com dados recebidos
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document retornoXML = dBuilder.parse(content);
			
			//retorna objeto Node com dados obtidos
			no.setNodeValue(retornoXML.getDocumentElement().getNodeValue());
			return no;
		}
}
