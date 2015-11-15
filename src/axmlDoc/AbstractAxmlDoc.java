package axmlDoc;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;
import org.xml.sax.SAXException;

import axmlDoc.AxmlDoc.HandleStrategy;
import axmlDoc.AxmlDoc.ReturnType;

public abstract class AbstractAxmlDoc implements Document {
	protected Document decoratedDocument;
	private String xmlFilePath;
	private HandleStrategy handleStrategy;
	private DocumentBuilderFactory dbFactory;
	private DocumentBuilder dBuilder;
	protected ReturnType returnType;
	protected Node uniqueMaterializedElement;

	public AbstractAxmlDoc(Document documentToDecorate) {
		this.decoratedDocument = documentToDecorate;
	}

	public AbstractAxmlDoc(File xmlFile, HandleStrategy handleStrategy, ReturnType returnType)
			throws AxmlDocException {
		try {
			dbFactory = DocumentBuilderFactory.newInstance();
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			this.decoratedDocument = doc;
			this.xmlFilePath = xmlFile.getPath();
			this.handleStrategy = handleStrategy;
			this.returnType = returnType;
		} catch (ParserConfigurationException | IOException | SAXException e) {
			throw new AxmlDocException("Error parsing XML file. It exists and the path is correct?");
		}
	}

	@Override
	public String getNodeName() {
		return this.decoratedDocument.getNodeName();
	}

	@Override
	public String getNodeValue() throws DOMException {
		return this.decoratedDocument.getNodeValue();
	}

	@Override
	public void setNodeValue(String nodeValue) throws DOMException {
		this.decoratedDocument.setNodeValue(nodeValue);
	}

	@Override
	public short getNodeType() {
		return this.decoratedDocument.getNodeType();
	}

	@Override
	public Node getParentNode() {
		return this.decoratedDocument.getParentNode();
	}

	@Override
	public NodeList getChildNodes() {
		return this.decoratedDocument.getChildNodes();
	}

	@Override
	public Node getFirstChild() {
		return this.decoratedDocument.getFirstChild();
	}

	@Override
	public Node getLastChild() {
		return this.decoratedDocument.getLastChild();
	}

	@Override
	public Node getPreviousSibling() {
		return this.decoratedDocument.getPreviousSibling();
	}

	@Override
	public Node getNextSibling() {
		return this.decoratedDocument.getNextSibling();
	}

	@Override
	public NamedNodeMap getAttributes() {
		return this.decoratedDocument.getAttributes();
	}

	@Override
	public Document getOwnerDocument() {
		return this.decoratedDocument.getOwnerDocument();
	}

	@Override
	public Node insertBefore(Node newChild, Node refChild) throws DOMException {
		return this.decoratedDocument.insertBefore(newChild, refChild);
	}

	@Override
	public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
		return this.decoratedDocument.replaceChild(newChild, oldChild);
	}

	@Override
	public Node removeChild(Node oldChild) throws DOMException {
		return this.decoratedDocument.removeChild(oldChild);
	}

	@Override
	public Node appendChild(Node newChild) throws DOMException {
		return this.decoratedDocument.appendChild(newChild);
	}

	@Override
	public boolean hasChildNodes() {
		return this.decoratedDocument.hasChildNodes();
	}

	@Override
	public Node cloneNode(boolean deep) {
		return this.decoratedDocument.cloneNode(deep);
	}

	@Override
	public void normalize() {
		this.decoratedDocument.normalize();
	}

	@Override
	public boolean isSupported(String feature, String version) {
		return this.decoratedDocument.isSupported(feature, version);
	}

	@Override
	public String getNamespaceURI() {
		return this.decoratedDocument.getNamespaceURI();
	}

	@Override
	public String getPrefix() {
		return this.decoratedDocument.getPrefix();
	}

	@Override
	public void setPrefix(String prefix) throws DOMException {
		this.decoratedDocument.setPrefix(prefix);
	}

	@Override
	public String getLocalName() {
		return this.decoratedDocument.getLocalName();
	}

	@Override
	public boolean hasAttributes() {
		return this.decoratedDocument.hasAttributes();
	}

	@Override
	public String getBaseURI() {
		return this.decoratedDocument.getBaseURI();
	}

	@Override
	public short compareDocumentPosition(Node other) throws DOMException {
		return this.decoratedDocument.compareDocumentPosition(other);
	}

	@Override
	// TODO Implementar materialização de nós com intensional data.
	public String getTextContent() throws DOMException {
		return this.decoratedDocument.getTextContent();
	}

	@Override
	public void setTextContent(String textContent) throws DOMException {
		this.decoratedDocument.setTextContent(textContent);
	}

	@Override
	public boolean isSameNode(Node other) {
		return isSameNode(other);
	}

	@Override
	public String lookupPrefix(String namespaceURI) {
		return this.decoratedDocument.lookupPrefix(namespaceURI);
	}

	@Override
	public boolean isDefaultNamespace(String namespaceURI) {
		return this.decoratedDocument.isDefaultNamespace(namespaceURI);
	}

	@Override
	public String lookupNamespaceURI(String prefix) {
		return this.decoratedDocument.lookupNamespaceURI(prefix);
	}

	@Override
	public boolean isEqualNode(Node arg) {
		return this.decoratedDocument.isEqualNode(arg);
	}

	@Override
	public Object getFeature(String feature, String version) {
		return this.decoratedDocument.getFeature(feature, version);
	}

	@Override
	public Object setUserData(String key, Object data, UserDataHandler handler) {
		return this.decoratedDocument.setUserData(key, data, handler);
	}

	@Override
	public Object getUserData(String key) {
		return this.decoratedDocument.getUserData(key);
	}

	@Override
	public DocumentType getDoctype() {
		return this.decoratedDocument.getDoctype();
	}

	@Override
	public DOMImplementation getImplementation() {
		return this.decoratedDocument.getImplementation();
	}

	@Override
	public Element getDocumentElement() {
		return this.decoratedDocument.getDocumentElement();
	}

	@Override
	public Element createElement(String tagName) throws DOMException {
		return this.decoratedDocument.createElement(tagName);
	}

	@Override
	public DocumentFragment createDocumentFragment() {
		return this.decoratedDocument.createDocumentFragment();
	}

	@Override
	public Text createTextNode(String data) {
		return this.decoratedDocument.createTextNode(data);
	}

	@Override
	public Comment createComment(String data) {
		return this.decoratedDocument.createComment(data);
	}

	@Override
	public CDATASection createCDATASection(String data) throws DOMException {
		return this.decoratedDocument.createCDATASection(data);
	}

	@Override
	public ProcessingInstruction createProcessingInstruction(String target, String data)
			throws DOMException {
		return this.decoratedDocument.createProcessingInstruction(target, data);
	}

	@Override
	public Attr createAttribute(String name) throws DOMException {
		return this.decoratedDocument.createAttribute(name);
	}

	@Override
	public EntityReference createEntityReference(String name) throws DOMException {
		return this.decoratedDocument.createEntityReference(name);
	}

	@Override
	public NodeList getElementsByTagName(String tagname) {
		return this.decoratedDocument.getElementsByTagName(tagname);
	}

	@Override
	public Node importNode(Node importedNode, boolean deep) throws DOMException {
		return this.decoratedDocument.importNode(importedNode, deep);
	}

	@Override
	public Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
		return this.decoratedDocument.createElementNS(namespaceURI, qualifiedName);
	}

	@Override
	public Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
		return this.decoratedDocument.createAttributeNS(namespaceURI, qualifiedName);
	}

	@Override
	public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
		return this.decoratedDocument.getElementsByTagNameNS(namespaceURI, localName);
	}

	@Override
	public Element getElementById(String elementId) {
		return this.decoratedDocument.getElementById(elementId);
	}

	@Override
	public String getInputEncoding() {
		return this.decoratedDocument.getInputEncoding();
	}

	@Override
	public String getXmlEncoding() {
		return this.decoratedDocument.getXmlEncoding();
	}

	@Override
	public boolean getXmlStandalone() {
		return this.decoratedDocument.getXmlStandalone();
	}

	@Override
	public void setXmlStandalone(boolean xmlStandalone) throws DOMException {
		this.decoratedDocument.setXmlStandalone(xmlStandalone);
	}

	@Override
	public String getXmlVersion() {
		return this.decoratedDocument.getXmlVersion();
	}

	@Override
	public void setXmlVersion(String xmlVersion) throws DOMException {
		this.decoratedDocument.setXmlVersion(xmlVersion);
	}

	@Override
	public boolean getStrictErrorChecking() {
		return this.decoratedDocument.getStrictErrorChecking();
	}

	@Override
	public void setStrictErrorChecking(boolean strictErrorChecking) {
		this.decoratedDocument.setStrictErrorChecking(strictErrorChecking);
	}

	@Override
	public String getDocumentURI() {
		return this.decoratedDocument.getDocumentURI();
	}

	@Override
	public void setDocumentURI(String documentURI) {
		this.decoratedDocument.setDocumentURI(documentURI);
	}

	@Override
	public Node adoptNode(Node source) throws DOMException {
		return this.decoratedDocument.adoptNode(source);
	}

	@Override
	public DOMConfiguration getDomConfig() {
		return this.decoratedDocument.getDomConfig();
	}

	@Override
	public void normalizeDocument() {
		this.decoratedDocument.normalizeDocument();
	}

	@Override
	public Node renameNode(Node n, String namespaceURI, String qualifiedName) throws DOMException {
		return this.decoratedDocument.renameNode(n, namespaceURI, qualifiedName);
	}

}
