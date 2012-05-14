package org.verba.xdxf;

import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.verba.xdxf.handler.BoldPhraseEventHandler;
import org.verba.xdxf.handler.KeyPhraseEventHandler;
import org.verba.xdxf.node.XdxfElement;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class XdxfParser {

	public XdxfElement parse(InputStream xdxfStream) throws XdxfArticleParseException {
		InputSource inputSource = new InputSource(xdxfStream);
		
		XdxfContentHandler xdxfContentHandler = prepareContentHandler();
		
		try {
	        SAXParser saxParser = createSaxParser();
	        XMLReader xmlReader = saxParser.getXMLReader();
	        xmlReader.setContentHandler(xdxfContentHandler);
	        
			xmlReader.parse(inputSource);
		} catch (Exception e) {
			throw new XdxfArticleParseException(e);
		}
		
		return xdxfContentHandler.getXdxfArticle();
	}

	protected XdxfContentHandler prepareContentHandler() {
		XdxfContentHandler xdxfContentHandler = new XdxfContentHandler();
		
		xdxfContentHandler.registerXdxfElementHandler(new KeyPhraseEventHandler());
		xdxfContentHandler.registerXdxfElementHandler(new BoldPhraseEventHandler());
		
		return xdxfContentHandler;
	}

	protected SAXParser createSaxParser() throws ParserConfigurationException, SAXException {
		SAXParserFactory saxParserFactory =  SAXParserFactory.newInstance();
		return saxParserFactory.newSAXParser();
	}

	public static class XdxfArticleParseException extends Exception {
		private static final long serialVersionUID = 5234438862619499913L;

		public XdxfArticleParseException(Throwable e) {
			super(e);
		}
	}
}