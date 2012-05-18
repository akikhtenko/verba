package org.verba.xdxf.node;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.verba.xdxf.XdxfNodeDisplay;

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
		for (XdxfNode child : children) {
			childrenText.append(child.asPlainText());
		}

		return childrenText.toString();
	}

	public int getContentLength() {
		int childrenLength = 0;
		for (XdxfNode child : children) {
			childrenLength += child.getContentLength();
		}

		return childrenLength;
	}

	public Iterator<XdxfNode> iterator() {
		return children.iterator();
	}

	@Override
	public void print(XdxfNodeDisplay display) {
		for (XdxfNode child : children) {
			child.print(display);
		}
	}
}
