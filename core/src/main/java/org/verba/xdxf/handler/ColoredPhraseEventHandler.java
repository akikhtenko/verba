package org.verba.xdxf.handler;

import org.verba.xdxf.node.ColoredPhrase;
import org.verba.xdxf.node.XdxfElement;
import org.xml.sax.Attributes;

public class ColoredPhraseEventHandler implements XdxfEventHandler {
	@Override
	public boolean isEventTarget(String elementName) {
		return "c".equalsIgnoreCase(elementName);
	}

	@Override
	public XdxfElement createElement(Attributes attributes) {
		return new ColoredPhrase(attributes.getValue("c"));
	}
}
