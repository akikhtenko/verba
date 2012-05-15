package org.verba.xdxf.node;

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
}
