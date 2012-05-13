package org.verba.xdxf.node;

import static org.verba.xdxf.node.XdxfNodeType.PLAIN_TEXT;


public class PlainText implements XdxfNode {
	private String plainText;
	
	public PlainText(String aPlainText) {
		plainText = aPlainText;
	}

	@Override
	public XdxfNodeType getType() {
		return PLAIN_TEXT;
	}

	@Override
	public String asPlainText() {
		return plainText;
	}
}
