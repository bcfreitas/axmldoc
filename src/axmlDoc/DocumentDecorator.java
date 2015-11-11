package axmlDoc;

import org.w3c.dom.Document;

public abstract class DocumentDecorator implements Document{
	protected Document docDecorado;
	public DocumentDecorator(Document doc){
		this.docDecorado = doc;
	}
}
