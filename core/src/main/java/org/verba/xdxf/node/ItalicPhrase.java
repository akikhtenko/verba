package org.verba.xdxf.node;

import org.verba.xdxf.XdxfNodeDisplay;

public class ItalicPhrase extends XdxfElement {

	@Override
	public XdxfNodeType getType() {
		return XdxfNodeType.ITALIC_PHRASE;
	}

	@Override
	public void print(XdxfNodeDisplay display) {
		super.print(display);

		display.print(this);
	}
}
