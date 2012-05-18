package org.verba.xdxf.handler;

import org.verba.xdxf.node.ItalicPhrase;
import org.verba.xdxf.node.XdxfElement;
import org.xml.sax.Attributes;

public class ItalicPhraseEventHandler implements XdxfEventHandler {
	@Override
	public boolean isEventTarget(String elementName) {
		return "i".equalsIgnoreCase(elementName);
	}

	@Override
	public XdxfElement createElement(Attributes atts) {
		return new ItalicPhrase();
	}
}
