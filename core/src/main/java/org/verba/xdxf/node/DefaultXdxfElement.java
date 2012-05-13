package org.verba.xdxf.node;

import java.util.LinkedList;
import java.util.List;


public class DefaultXdxfElement implements XdxfElement {
	private List<XdxfNode> children = new LinkedList<XdxfNode>();

	@Override
	public XdxfNodeType getType() {
		return XdxfNodeType.UNKNOWN;
	}

	@Override
	public void addChild(XdxfNode xdxfNode) {
		children.add(xdxfNode);
	}

	@Override
	public String asPlainText() {
		StringBuffer childrenText = new StringBuffer();
		for (XdxfNode child: children) {
			childrenText.append(child.asPlainText());
		}
		
		return childrenText.toString();
	}
}
