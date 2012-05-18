package org.verba.xdxf.node;

import org.verba.xdxf.XdxfNodeDisplay;

public class ColoredPhrase extends XdxfElement {
	private String colorCode;

	public ColoredPhrase(String aColorCode) {
		colorCode = aColorCode;
	}

	@Override
	public XdxfNodeType getType() {
		return XdxfNodeType.COLORED_PHRASE;
	}

	public String getColorCode() {
		return colorCode;
	}

	@Override
	public void print(XdxfNodeDisplay display) {
		super.print(display);

		display.print(this);
	}
}
