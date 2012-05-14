package org.verba.xdxf;

import static org.verba.stardict.WordDefinitionPartType.XDXF;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import org.verba.stardict.WordDefinitionPart;
import org.verba.stardict.WordDefinitionPartType;
import org.verba.xdxf.XdxfParser.XdxfArticleParseException;
import org.verba.xdxf.node.XdxfElement;

public class XdxfWordDefinitionPart implements WordDefinitionPart {
	private byte[] rawWordDefinition;
	
	@Override
	public WordDefinitionPartType getType() {
		return XDXF;
	}
	
	public XdxfWordDefinitionPart(byte[] wordDefinitionBuffer) {
		rawWordDefinition = wordDefinitionBuffer;
	}
	
	public String asPlainText() {
		return new String(rawWordDefinition);
	}
	
	public byte[] bytes() {
		return rawWordDefinition;
	}
	
	public XdxfElement asXdxfArticle() {
		InputStream xdxfStream = new ByteArrayInputStream(rawWordDefinition);
		
		XdxfElement xdxfArticle = parseXdxfArticle(xdxfStream);
		
		return xdxfArticle;
	}

	private XdxfElement parseXdxfArticle(InputStream xdxfStream) {
		try {
			return new XdxfParser().parse(xdxfStream);
		} catch (XdxfArticleParseException e) {
			throw new RuntimeException("Unexpected error while parsing xdxf word definition part", e);
		} finally {
			closeQuietly(xdxfStream);
		}
	}

    private void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }
}
