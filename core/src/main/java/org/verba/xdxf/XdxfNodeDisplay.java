package org.verba.xdxf;

import org.verba.xdxf.node.BoldPhrase;
import org.verba.xdxf.node.ColoredPhrase;
import org.verba.xdxf.node.ItalicPhrase;
import org.verba.xdxf.node.KeyPhrase;
import org.verba.xdxf.node.PhraseReference;
import org.verba.xdxf.node.PlainText;

public interface XdxfNodeDisplay {

	void print(PlainText plainText);

	void print(KeyPhrase keyPhrase);

	void print(ColoredPhrase coloredPhrase);

	void print(BoldPhrase boldPhrase);

	void print(ItalicPhrase italicPhrase);

	void print(PhraseReference phraseReference);

}
