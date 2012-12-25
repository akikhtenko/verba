package org.verba.xdxf;

import static org.apache.commons.io.IOUtils.closeQuietly;
import static org.verba.stardict.PhraseDefinitionElementType.XDXF;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.verba.stardict.PhraseDefinitionElement;
import org.verba.stardict.PhraseDefinitionElementType;
import org.verba.xdxf.XdxfParser.XdxfArticleParseException;
import org.verba.xdxf.node.XdxfElement;

public class XdxfPhraseDefinitionElement implements PhraseDefinitionElement {
	private byte[] rawPhraseDefinition;

	public XdxfPhraseDefinitionElement(byte[] phraseDefinitionBuffer) {
		rawPhraseDefinition = phraseDefinitionBuffer.clone();
	}

	@Override
	public PhraseDefinitionElementType getType() {
		return XDXF;
	}

	public byte[] bytes() {
		return rawPhraseDefinition;
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
		return new ByteArrayInputStream(rawPhraseDefinition);
	}

	protected XdxfParser createXdxfParser() {
		return new XdxfParser();
	}
}
