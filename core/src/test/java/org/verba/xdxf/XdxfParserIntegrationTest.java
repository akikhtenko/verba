package org.verba.xdxf;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Ignore;
import org.junit.Test;
import org.verba.xdxf.XdxfParser.XdxfArticleParseException;
import org.verba.xdxf.node.XdxfElement;

public class XdxfParserIntegrationTest {

	@Test
	@Ignore
	public void shouldParseXdxf() throws IOException, XdxfArticleParseException {
		XdxfParser xdxfParser = new XdxfParser();
		InputStream xdxfStream = getClass().getClassLoader().getResourceAsStream("org/verba/xdxf/definition.xdxf");
		try {
			XdxfElement xdxfArticle = xdxfParser.parse(xdxfStream);
			System.out.println(xdxfArticle.asPlainText());
		} finally {
			xdxfStream.close();
		}
	}
}
