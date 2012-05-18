package org.verba.xdxf.node;

import org.verba.xdxf.XdxfNodeDisplay;

public class KeyPhrase extends XdxfElement {

	@Override
	public XdxfNodeType getType() {
		return XdxfNodeType.KEY_PHRASE;
	}

	@Override
	public void print(XdxfNodeDisplay display) {
		super.print(display);

		display.print(this);
	}
}
