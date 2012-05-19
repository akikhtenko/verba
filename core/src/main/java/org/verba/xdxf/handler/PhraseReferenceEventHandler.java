package org.verba.xdxf.handler;

import org.verba.xdxf.node.PhraseReference;
import org.verba.xdxf.node.XdxfElement;
import org.xml.sax.Attributes;

public class PhraseReferenceEventHandler extends XdxfEventHandler {
	@Override
	protected String getMatchingTag() {
		return "kref";
	}

	@Override
	public XdxfElement createElement(Attributes atts) {
		return new PhraseReference();
	}
}
