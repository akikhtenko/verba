package org.verba.xdxf.handler;

import org.verba.xdxf.node.KeyPhrase;
import org.verba.xdxf.node.XdxfElement;
import org.xml.sax.Attributes;

public class KeyPhraseEventHandler extends XdxfEventHandler {
	@Override
	protected String getMatchingTag() {
		return "k";
	}

	@Override
	public XdxfElement createElement(Attributes atts) {
		return new KeyPhrase();
	}
}
