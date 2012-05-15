package org.verba.xdxf;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.verba.xdxf.handler.DefaultXdxfEventHandler;
import org.verba.xdxf.handler.XdxfEventHandler;
import org.verba.xdxf.node.PlainText;
import org.verba.xdxf.node.RootXdxfElement;
import org.verba.xdxf.node.XdxfElement;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XdxfContentHandler extends DefaultHandler {
	private Set<XdxfEventHandler> handlers = new HashSet<XdxfEventHandler>();
	private StringBuilder plainText = new StringBuilder();
	private Deque<XdxfElement> nodesStack = new LinkedList<XdxfElement>();

	public void registerXdxfElementHandler(XdxfEventHandler xdxfEventHandler) {
		handlers.add(xdxfEventHandler);
	}

	public XdxfElement getXdxfArticle() {
		return nodesStack.removeLast();
	}

	public void startDocument() throws SAXException {
		nodesStack.add(new RootXdxfElement());
	}

	public void startElement(String namespaceURI, String localName, String qName, Attributes attributes)
			throws SAXException {
		flushPlainTextBufferIfNeeded();

		XdxfEventHandler targetEventHandler = pickTargetHandler(qName);

		XdxfElement element = targetEventHandler.createElement(attributes);

		nodesStack.getLast().addChild(element);
		nodesStack.add(element);
	}

	public void characters(char[] text, int start, int length) throws SAXException {
		plainText.append(text, start, length);
	}

	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		flushPlainTextBufferIfNeeded();
		nodesStack.removeLast();
	}

	private XdxfEventHandler pickTargetHandler(String qName) {
		XdxfEventHandler targetEventHandler = null;

		for (XdxfEventHandler eventHandler : handlers) {
			if (eventHandler.isEventTarget(qName)) {
				targetEventHandler = eventHandler;
				break;
			}
		}

		if (targetEventHandler == null) {
			targetEventHandler = new DefaultXdxfEventHandler();
		}

		return targetEventHandler;
	}

	private void flushPlainTextBufferIfNeeded() {
		if (plainText.length() != 0) {
			PlainText plainTextNode = new PlainText(plainText.toString());
			nodesStack.getLast().addChild(plainTextNode);
			plainText.setLength(0);
		}
	}
}
