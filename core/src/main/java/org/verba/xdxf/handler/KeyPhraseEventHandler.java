package org.verba.xdxf.handler;

import org.verba.xdxf.node.KeyPhrase;
import org.verba.xdxf.node.XdxfElement;
import org.xml.sax.Attributes;

public class KeyPhraseEventHandler implements XdxfEventHandler {
	@Override
	public boolean isEventTarget(String elementName) {
		return "k".equalsIgnoreCase(elementName);
	}

	@Override
	public XdxfElement createElement(Attributes atts) {
		return new KeyPhrase();
	}
}
