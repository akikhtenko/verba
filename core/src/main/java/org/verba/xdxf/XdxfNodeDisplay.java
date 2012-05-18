package org.verba.xdxf;

import org.verba.xdxf.node.ColoredPhrase;
import org.verba.xdxf.node.KeyPhrase;

public interface XdxfNodeDisplay {

	void print(String plainText);

	void print(KeyPhrase keyPhrase);

	void print(ColoredPhrase coloredPhrase);

}
