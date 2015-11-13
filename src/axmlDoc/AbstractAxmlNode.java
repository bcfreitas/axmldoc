package axmlDoc;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;

public class AbstractAxmlNode implements Element {

	protected Node decoratedNode;

	public AbstractAxmlNode(Node nodeToDecorate) {
		this.decoratedNode = nodeToDecorate;
	}

	@Override
	public String getNodeName() {
		return this.decoratedNode.getNodeName();
	}

	@Override
	public String getNodeValue() throws DOMException {
		return this.decoratedNode.getNodeValue();
	}

	@Override
	public void setNodeValue(String nodeValue) throws DOMException {
		this.decoratedNode.setNodeValue(nodeValue);
	}

	@Override
	public short getNodeType() {
		return this.decoratedNode.getNodeType();
	}

	@Override
	public Node getParentNode() {
		return this.decoratedNode.getParentNode();
	}

	@Override
	public NodeList getChildNodes() {
		return this.decoratedNode.getChildNodes();
	}

	@Override
	public Node getFirstChild() {
		return this.decoratedNode.getFirstChild();
	}

	@Override
	public Node getLastChild() {
		return this.decoratedNode.getLastChild();
	}

	@Override
	public Node getPreviousSibling() {
		return this.decoratedNode.getPreviousSibling();
	}

	@Override
	public Node getNextSibling() {
		return this.decoratedNode.getNextSibling();
	}

	@Override
	public NamedNodeMap getAttributes() {
		return this.decoratedNode.getAttributes();
	}

	@Override
	public Document getOwnerDocument() {
		return this.decoratedNode.getOwnerDocument();
	}

	@Override
	public Node insertBefore(Node newChild, Node refChild) throws DOMException {
		return this.decoratedNode.insertBefore(newChild, refChild);
	}

	@Override
	public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
		return this.decoratedNode.replaceChild(newChild, oldChild);
	}

	@Override
	public Node removeChild(Node oldChild) throws DOMException {
		return this.decoratedNode.removeChild(oldChild);
	}

	@Override
	public Node appendChild(Node newChild) throws DOMException {
		return this.decoratedNode.appendChild(newChild);
	}

	@Override
	public boolean hasChildNodes() {
		return this.decoratedNode.hasChildNodes();
	}

	@Override
	public Node cloneNode(boolean deep) {
		return this.decoratedNode.cloneNode(deep);
	}

	@Override
	public void normalize() {
		this.decoratedNode.normalize();
	}

	@Override
	public boolean isSupported(String feature, String version) {
		return this.decoratedNode.isSupported(feature, version);
	}

	@Override
	public String getNamespaceURI() {
		return this.decoratedNode.getNamespaceURI();
	}

	@Override
	public String getPrefix() {
		return this.decoratedNode.getPrefix();
	}

	@Override
	public void setPrefix(String prefix) throws DOMException {
		this.decoratedNode.setPrefix(prefix);
	}

	@Override
	public String getLocalName() {
		return this.decoratedNode.getLocalName();
	}

	@Override
	public boolean hasAttributes() {
		return this.decoratedNode.hasAttributes();
	}

	@Override
	public String getBaseURI() {
		return this.decoratedNode.getBaseURI();
	}

	@Override
	public short compareDocumentPosition(Node other) throws DOMException {
		return this.decoratedNode.compareDocumentPosition(other);
	}

	@Override
	public void setTextContent(String textContent) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSameNode(Node other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String lookupPrefix(String namespaceURI) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDefaultNamespace(String namespaceURI) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String lookupNamespaceURI(String prefix) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEqualNode(Node arg) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object getFeature(String feature, String version) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object setUserData(String key, Object data, UserDataHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getUserData(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTagName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAttribute(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAttribute(String name, String value) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeAttribute(String name) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public Attr getAttributeNode(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attr setAttributeNode(Attr newAttr) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attr removeAttributeNode(Attr oldAttr) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeList getElementsByTagName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAttributeNS(String namespaceURI, String localName) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAttributeNS(String namespaceURI, String qualifiedName, String value)
			throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeAttributeNS(String namespaceURI, String localName) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public Attr getAttributeNodeNS(String namespaceURI, String localName) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attr setAttributeNodeNS(Attr newAttr) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeList getElementsByTagNameNS(String namespaceURI, String localName)
			throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasAttribute(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasAttributeNS(String namespaceURI, String localName) throws DOMException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TypeInfo getSchemaTypeInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setIdAttribute(String name, boolean isId) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setIdAttributeNS(String namespaceURI, String localName, boolean isId)
			throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setIdAttributeNode(Attr idAttr, boolean isId) throws DOMException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getTextContent() throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}
}
