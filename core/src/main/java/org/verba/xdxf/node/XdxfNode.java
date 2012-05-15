package org.verba.xdxf.node;

public interface XdxfNode {
	XdxfNodeType getType();

	String asPlainText();
}
