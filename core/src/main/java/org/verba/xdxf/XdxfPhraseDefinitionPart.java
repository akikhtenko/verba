package org.verba.xdxf;

import static org.verba.stardict.PhraseDefinitionPartType.XDXF;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.verba.stardict.PhraseDefinitionPart;
import org.verba.stardict.PhraseDefinitionPartType;
import org.verba.xdxf.XdxfParser.XdxfArticleParseException;
import org.verba.xdxf.node.XdxfElement;

public class XdxfPhraseDefinitionPart implements PhraseDefinitionPart {
	private byte[] rawPhraseDefinition;

	public XdxfPhraseDefinitionPart(byte[] phraseDefinitionBuffer) {
		rawPhraseDefinition = phraseDefinitionBuffer.clone();
	}

	@Override
	public PhraseDefinitionPartType getType() {
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
			IOUtils.closeQuietly(contentAsStream);
		}
	}

	private ByteArrayInputStream getContentAsInputStream() {
		return new ByteArrayInputStream(rawPhraseDefinition);
	}

	protected XdxfParser createXdxfParser() {
		return new XdxfParser();
	}
}
