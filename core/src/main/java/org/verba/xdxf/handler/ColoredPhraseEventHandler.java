package org.verba.xdxf.handler;

import org.verba.xdxf.node.ColoredPhrase;
import org.verba.xdxf.node.XdxfElement;
import org.xml.sax.Attributes;

public class ColoredPhraseEventHandler extends XdxfEventHandler {
	@Override
	protected String getMatchingTag() {
		return "c";
	}

	@Override
	public XdxfElement createElement(Attributes attributes) {
		return new ColoredPhrase(attributes.getValue("c"));
	}
}
