package org.verba.xdxf.handler;

import org.verba.xdxf.node.PhraseReference;
import org.verba.xdxf.node.XdxfElement;
import org.xml.sax.Attributes;

public class PhraseReferenceEventHandler implements XdxfEventHandler {
	@Override
	public boolean isEventTarget(String elementName) {
		return "kref".equalsIgnoreCase(elementName);
	}

	@Override
	public XdxfElement createElement(Attributes atts) {
		return new PhraseReference();
	}
}
