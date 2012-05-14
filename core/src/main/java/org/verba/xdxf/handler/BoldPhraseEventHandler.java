package org.verba.xdxf.handler;

import org.verba.xdxf.node.BoldPhrase;
import org.verba.xdxf.node.XdxfElement;
import org.xml.sax.Attributes;

public class BoldPhraseEventHandler implements XdxfEventHandler {
	@Override
	public boolean isEventTarget(String elementName) {
		return "b".equalsIgnoreCase(elementName);
	}

	@Override
	public XdxfElement createElement(Attributes atts) {
		return new BoldPhrase();
	}
}
