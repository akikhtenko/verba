package org.verba.xdxf.node;

import org.verba.xdxf.XdxfNodeDisplay;

public interface XdxfNode {
	XdxfNodeType getType();
	String asPlainText();
	int getContentLength();
	void print(XdxfNodeDisplay display);
}
