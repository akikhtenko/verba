package org.verba.xdxf.handler;

import org.verba.xdxf.node.DefaultXdxfElement;
import org.verba.xdxf.node.XdxfElement;
import org.xml.sax.Attributes;

public class DefaultXdxfEventHandler implements XdxfEventHandler {
	@Override
	public boolean isEventTarget(String elementName) {
		return true;
	}

	@Override
	public XdxfElement createElement(Attributes atts) {
		return new DefaultXdxfElement();
	}
}
