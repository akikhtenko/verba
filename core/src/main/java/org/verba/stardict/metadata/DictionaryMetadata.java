package org.verba.stardict.metadata;

import static org.verba.stardict.IndexOffsetSize.BITS_32;

import org.verba.stardict.IndexOffsetSize;
import org.verba.stardict.PhraseDefinitionPartFormat;

public class DictionaryMetadata {
	private String name;
	private String description;
	private String version;
	private int wordCount;
	private IndexOffsetSize indexOffsetSize = BITS_32;
	private String date;
	private PhraseDefinitionPartFormat phraseDefinitionPartFormat;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public int getWordCount() {
		return wordCount;
	}
	public void setWordCount(int wordCount) {
		this.wordCount = wordCount;
	}
	public IndexOffsetSize getIndexOffsetSize() {
		return indexOffsetSize;
	}
	public void setIndexOffsetSize(IndexOffsetSize indexOffsetSize) {
		this.indexOffsetSize = indexOffsetSize;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public PhraseDefinitionPartFormat getPhraseDefinitionPartFormat() {
		return phraseDefinitionPartFormat;
	}
	public void setPhraseDefinitionPartFormat(PhraseDefinitionPartFormat phraseDefinitionPartFormat) {
		this.phraseDefinitionPartFormat = phraseDefinitionPartFormat;
	}
}
