package org.verba.xdxf.node;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class XdxfElement implements XdxfNode {
	private List<XdxfNode> children = new LinkedList<XdxfNode>();

	public XdxfNodeType getType() {
		return XdxfNodeType.UNKNOWN;
	}

	public void addChild(XdxfNode xdxfNode) {
		children.add(xdxfNode);
	}

	public String asPlainText() {
		StringBuffer childrenText = new StringBuffer();
		for (XdxfNode child: children) {
			childrenText.append(child.asPlainText());
		}
		
		return childrenText.toString();
	}

	public Iterator<XdxfNode> iterator() {
		return children.iterator();
	}
}
