package org.verba.xdxf.handler;

import org.verba.xdxf.node.ItalicPhrase;
import org.verba.xdxf.node.XdxfElement;
import org.xml.sax.Attributes;

public class ItalicPhraseEventHandler extends XdxfEventHandler {
	@Override
	protected String getMatchingTag() {
		return "i";
	}

	@Override
	public XdxfElement createElement(Attributes atts) {
		return new ItalicPhrase();
	}
}
