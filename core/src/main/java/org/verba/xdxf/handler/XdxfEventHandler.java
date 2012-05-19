package org.verba.xdxf.handler;

import org.verba.xdxf.node.XdxfElement;
import org.xml.sax.Attributes;

public abstract class XdxfEventHandler {
	private XdxfEventHandler nextHandler;

	public XdxfElement buildXdxfElement(String elementName, Attributes attributes) {
		XdxfElement builtXdxfElement = null;
		if (getMatchingTag().equalsIgnoreCase(elementName)) {
			builtXdxfElement = createElement(attributes);
		} else if (nextHandler != null) {
			builtXdxfElement = nextHandler.buildXdxfElement(elementName, attributes);
		} else {
			builtXdxfElement = new XdxfElement();
		}

		return builtXdxfElement;
	}

	public XdxfEventHandler chainNextHandler(XdxfEventHandler theNextHandler) {
		nextHandler = theNextHandler;
		return theNextHandler;
	}

	protected abstract XdxfElement createElement(Attributes attributes);

	protected abstract String getMatchingTag();
}
