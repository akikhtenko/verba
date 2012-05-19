package org.verba.xdxf.handler;

import org.verba.xdxf.node.BoldPhrase;
import org.verba.xdxf.node.XdxfElement;
import org.xml.sax.Attributes;

public class BoldPhraseEventHandler extends XdxfEventHandler {
	@Override
	protected String getMatchingTag() {
		return "b";
	}

	@Override
	public XdxfElement createElement(Attributes atts) {
		return new BoldPhrase();
	}
}
