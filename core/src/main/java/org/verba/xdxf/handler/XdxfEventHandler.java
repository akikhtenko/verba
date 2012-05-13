package org.verba.xdxf.handler;

import org.verba.xdxf.node.XdxfElement;
import org.xml.sax.Attributes;

public interface XdxfEventHandler {
	boolean isEventTarget(String elementName);
	XdxfElement createElement(Attributes atts);
}
