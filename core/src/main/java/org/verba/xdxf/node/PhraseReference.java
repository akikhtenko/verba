package org.verba.xdxf.node;

import org.verba.xdxf.XdxfNodeDisplay;

public class PhraseReference extends XdxfElement {

	@Override
	public XdxfNodeType getType() {
		return XdxfNodeType.PHRASE_REFERENCE;
	}

	@Override
	public void print(XdxfNodeDisplay display) {
		super.print(display);

		display.print(this);
	}
}
