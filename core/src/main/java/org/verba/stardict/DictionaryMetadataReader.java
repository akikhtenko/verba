package org.verba.stardict;

import static java.lang.Integer.parseInt;
import static org.apache.commons.io.Charsets.UTF_8;
import static org.verba.stardict.IndexOffsetSize.fromString;
import static org.verba.stardict.PhraseDefinitionElementType.XDXF;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

public class DictionaryMetadataReader {
	private static final String PROPERTY_BOOKNAME = "bookname";
	private static final String PROPERTY_WORDCOUNT = "wordcount";
	private static final String PROPERTY_IDXOFFSETBITS = "idxoffsetbits";
	private static final String PROPERTY_VERSION = "version";
	private static final String PROPERTY_DESCRIPTION = "description";
	private static final String PROPERTY_DATE = "date";
	private static final String PROPERTY_SAMETYPESEQUENCE = "sametypesequence";
	private static final String PARAMETER_VALUE_DELIMITER = "=";
	private InputStream dictionaryMetadataSource;
	private Properties dictionaryProperties;
	private DictionaryMetadata dictionaryMetadata;

	public DictionaryMetadataReader(InputStream dictionaryMetadataSource) {
		this.dictionaryMetadataSource = dictionaryMetadataSource;
		dictionaryProperties = new Properties();
		dictionaryMetadata = new DictionaryMetadata();
	}

	public DictionaryMetadata read() throws IOException {
		parseDictionaryMetadataSource();
		populateDictionaryMetadata();
		return dictionaryMetadata;
	}

	private void populateDictionaryMetadata() {
		populateName();
		populateDescription();
		populateDate();
		populateVersion();
		populateWordCount();
		populateIndexOffsetSize();
		populatePhraseDefinitionPartFormat();
	}

	private void populatePhraseDefinitionPartFormat() {
		String sameTypeSequence = dictionaryProperties.getProperty(PROPERTY_SAMETYPESEQUENCE);
		if (isNotEmpty(sameTypeSequence)) {
			dictionaryMetadata.setPhraseDefinitionPartFormat(parsePhraseDefinitionFormat(sameTypeSequence));
		}
	}

	private PhraseDefinitionPartFormat parsePhraseDefinitionFormat(String sameTypeSequence) {
		PhraseDefinitionPartFormat phraseDefinitionPartFormat = new PhraseDefinitionPartFormat();
		for (int i = 0; i < sameTypeSequence.length(); i++) {
			switch (sameTypeSequence.charAt(i)) {
			case 'x':
				phraseDefinitionPartFormat.add(XDXF);
			default:
				break;
			}
		}
		return phraseDefinitionPartFormat;
	}

	private void populateIndexOffsetSize() {
		String idxOffsetBits = dictionaryProperties.getProperty(PROPERTY_IDXOFFSETBITS);
		if (isNotEmpty(idxOffsetBits)) {
			dictionaryMetadata.setIndexOffsetSize(fromString(idxOffsetBits));
		}
	}

	private void populateWordCount() {
		String wordCount = dictionaryProperties.getProperty(PROPERTY_WORDCOUNT);
		if (isNotEmpty(wordCount)) {
			dictionaryMetadata.setWordCount(parseInt(wordCount));
		}
	}

	private void populateVersion() {
		if (dictionaryProperties.contains(PROPERTY_VERSION)) {
			dictionaryMetadata.setVersion(dictionaryProperties.getProperty(PROPERTY_VERSION));
		}
	}

	private void populateDate() {
		if (dictionaryProperties.contains(PROPERTY_DATE)) {
			dictionaryMetadata.setDate(dictionaryProperties.getProperty(PROPERTY_DATE));
		}
	}

	private void populateDescription() {
		if (dictionaryProperties.contains(PROPERTY_DESCRIPTION)) {
			dictionaryMetadata.setDescription(dictionaryProperties.getProperty(PROPERTY_DESCRIPTION));
		}
	}

	private void populateName() {
		if (dictionaryProperties.contains(PROPERTY_BOOKNAME)) {
			dictionaryMetadata.setName(dictionaryProperties.getProperty(PROPERTY_BOOKNAME));
		}
	}

	private void parseDictionaryMetadataSource() throws IOException {
		LineIterator it = IOUtils.lineIterator(dictionaryMetadataSource, UTF_8);
		while (it.hasNext()) {
			String line = it.nextLine().trim();
			processNextLine(line);
		}
	}

	private void processNextLine(String line) {
		String[] parameterValuePair = line.split(PARAMETER_VALUE_DELIMITER);
		if (isValidParameterValuePair(parameterValuePair)) {
			dictionaryProperties.setProperty(parameterValuePair[0], parameterValuePair[1]);
		}
	}

	private boolean isValidParameterValuePair(String[] parameterValuePair) {
		return parameterValuePair.length > 1;
	}

	private boolean isNotEmpty(String string) {
		return string != null && string.length() > 0;
	}
}
