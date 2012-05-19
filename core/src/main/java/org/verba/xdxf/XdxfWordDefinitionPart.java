package org.verba.xdxf;

import static org.verba.stardict.WordDefinitionPartType.XDXF;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.verba.stardict.WordDefinitionPart;
import org.verba.stardict.WordDefinitionPartType;
import org.verba.xdxf.XdxfParser.XdxfArticleParseException;
import org.verba.xdxf.node.XdxfElement;

public class XdxfWordDefinitionPart implements WordDefinitionPart {
	private byte[] rawWordDefinition;

	public XdxfWordDefinitionPart(byte[] wordDefinitionBuffer) {
		rawWordDefinition = wordDefinitionBuffer.clone();
	}

	@Override
	public WordDefinitionPartType getType() {
		return XDXF;
	}

	public byte[] bytes() {
		return rawWordDefinition;
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
		return new ByteArrayInputStream(rawWordDefinition);
	}

	protected XdxfParser createXdxfParser() {
		return new XdxfParser();
	}
}
