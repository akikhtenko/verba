package org.verba.stardict.metadata;

import static java.lang.Integer.parseInt;
import static org.apache.commons.io.Charsets.UTF_8;
import static org.apache.commons.io.IOUtils.lineIterator;
import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.verba.stardict.IndexOffsetSize.fromString;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.LineIterator;
import org.verba.stardict.PhraseDefinitionFormat;
import org.verba.stardict.metadata.elementtype.PhraseDefinitionElementTypeFactory;

public class DictionaryMetadataReader {
	private static final String ERROR_MISSING_PROPERTY = "Mandatory property [%s] is missing";
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
	private PhraseDefinitionElementTypeFactory phraseDefinitionElementTypeFactory;

	public DictionaryMetadataReader(InputStream dictionaryMetadataSource) {
		this.dictionaryMetadataSource = dictionaryMetadataSource;
		dictionaryProperties = new Properties();
		dictionaryMetadata = new DictionaryMetadata();
		phraseDefinitionElementTypeFactory = new PhraseDefinitionElementTypeFactory();
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
		populatePhraseDefinitionFormat();
	}

	private void populatePhraseDefinitionFormat() {
		String sameTypeSequence = dictionaryProperties.getProperty(PROPERTY_SAMETYPESEQUENCE);
		if (isNotEmpty(sameTypeSequence)) {
			dictionaryMetadata.setPhraseDefinitionFormat(parsePhraseDefinitionFormat(sameTypeSequence));
		}
	}

	private PhraseDefinitionFormat parsePhraseDefinitionFormat(String sameTypeSequence) {
		PhraseDefinitionFormat phraseDefinitionFormat = new PhraseDefinitionFormat();
		for (int i = 0; i < sameTypeSequence.length(); i++) {
			phraseDefinitionFormat.add(phraseDefinitionElementTypeFactory.createFromChar(sameTypeSequence.charAt(i)));
		}
		return phraseDefinitionFormat;
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
		} else {
			throw new RuntimeException(String.format(ERROR_MISSING_PROPERTY, PROPERTY_WORDCOUNT));
		}
	}

	private void populateVersion() {
		String version = dictionaryProperties.getProperty(PROPERTY_VERSION);
		if (isNotEmpty(version)) {
			dictionaryMetadata.setVersion(version);
		}
	}

	private void populateDate() {
		String date = dictionaryProperties.getProperty(PROPERTY_DATE);
		if (isNotEmpty(date)) {
			dictionaryMetadata.setDate(date);
		}
	}

	private void populateDescription() {
		String description = dictionaryProperties.getProperty(PROPERTY_DESCRIPTION);
		if (isNotEmpty(description)) {
			dictionaryMetadata.setDescription(description);
		}
	}

	private void populateName() {
		String bookName = dictionaryProperties.getProperty(PROPERTY_BOOKNAME);
		if (isNotEmpty(bookName)) {
			dictionaryMetadata.setName(bookName);
		} else {
			throw new RuntimeException(String.format(ERROR_MISSING_PROPERTY, PROPERTY_BOOKNAME));
		}
	}

	private void parseDictionaryMetadataSource() throws IOException {
		LineIterator it = lineIterator(dictionaryMetadataSource, UTF_8);
		while (it.hasNext()) {
			String line = it.nextLine();
			processNextLine(line);
		}
	}

	private void processNextLine(String line) {
		String[] parameterValuePair = line.split(PARAMETER_VALUE_DELIMITER);
		if (isValidParameterValuePair(parameterValuePair)) {
			dictionaryProperties.setProperty(parameterValuePair[0].trim(), parameterValuePair[1].trim());
		}
	}

	private boolean isValidParameterValuePair(String[] parameterValuePair) {
		return parameterValuePair.length > 1;
	}
}
