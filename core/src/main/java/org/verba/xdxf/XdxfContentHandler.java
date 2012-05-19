package org.verba.xdxf;

import java.util.Deque;
import java.util.LinkedList;

import org.verba.xdxf.handler.XdxfEventHandler;
import org.verba.xdxf.node.PlainText;
import org.verba.xdxf.node.RootXdxfElement;
import org.verba.xdxf.node.XdxfElement;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XdxfContentHandler extends DefaultHandler {
	private XdxfEventHandler handlersChain;
	private StringBuilder plainText = new StringBuilder();
	private Deque<XdxfElement> nodesStack = new LinkedList<XdxfElement>();

	public XdxfContentHandler(XdxfEventHandler aHandlersChain) {
		handlersChain = aHandlersChain;
	}

	public XdxfElement getXdxfArticle() {
		return nodesStack.removeLast();
	}

	@Override
	public void startDocument() throws SAXException {
		nodesStack.add(new RootXdxfElement());
	}

	@Override
	public void startElement(String nsUri, String localName, String qName, Attributes attributes) throws SAXException {
		flushPlainTextBufferIfNeeded();

		XdxfElement element = handlersChain.buildXdxfElement(qName, attributes);

		nodesStack.getLast().addChild(element);
		nodesStack.add(element);
	}

	@Override
	public void characters(char[] text, int start, int length) throws SAXException {
		plainText.append(text, start, length);
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		flushPlainTextBufferIfNeeded();
		nodesStack.removeLast();
	}

	private void flushPlainTextBufferIfNeeded() {
		if (plainText.length() != 0) {
			PlainText plainTextNode = new PlainText(plainText.toString());
			nodesStack.getLast().addChild(plainTextNode);
			plainText.setLength(0);
		}
	}
}
