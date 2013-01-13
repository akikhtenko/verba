package org.verba.xdxf;

import static org.apache.commons.io.IOUtils.closeQuietly;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.verba.stardict.PhraseDefinitionElement;
import org.verba.stardict.PhraseDefinitionElementDisplay;
import org.verba.xdxf.XdxfParser.XdxfArticleParseException;
import org.verba.xdxf.node.XdxfElement;

public class XdxfPhraseDefinitionElement implements PhraseDefinitionElement {
	private byte[] rawPhraseDefinitionElement;

	public XdxfPhraseDefinitionElement(byte[] phraseDefinitionBuffer) {
		rawPhraseDefinitionElement = phraseDefinitionBuffer.clone();
	}

	public byte[] bytes() {
		return rawPhraseDefinitionElement;
	}

	public XdxfElement asXdxfArticle() {
		InputStream contentAsStream = getContentAsInputStream();
		try {
			return createXdxfParser().parse(contentAsStream);
		} catch (XdxfArticleParseException e) {
			throw new RuntimeException("Unexpected error while parsing xdxf word definition part", e);
		} finally {
			closeQuietly(contentAsStream);
		}
	}

	private ByteArrayInputStream getContentAsInputStream() {
		return new ByteArrayInputStream(rawPhraseDefinitionElement);
	}

	protected XdxfParser createXdxfParser() {
		return new XdxfParser();
	}

	@Override
	public void print(PhraseDefinitionElementDisplay phraseDefinitionDisplay) {
		phraseDefinitionDisplay.print(this);
	}
}
