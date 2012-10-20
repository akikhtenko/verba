package org.verba.stardict;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.verba.stardict.IndexOffsetSize.BITS_32;
import static org.verba.stardict.IndexOffsetSize.BITS_64;
import static org.verba.stardict.PhraseDefinitionElementType.XDXF;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.verba.stardict.metadata.DictionaryMetadata;
import org.verba.stardict.metadata.DictionaryMetadataReader;

public class DictionaryMetadataReaderTest {
	@Test
	public void shouldParseDictionaryMetadata() throws IOException {
		InputStream dictionaryMdSource = getClass().getClassLoader().getResourceAsStream(
				"org/verba/stardict/dictionary.ifo");
		DictionaryMetadataReader dictionaryMetadataReader = new DictionaryMetadataReader(dictionaryMdSource);
		try {
			DictionaryMetadata dictionaryMetadata = dictionaryMetadataReader.read();
			assertThat(dictionaryMetadata.getName(), is("Dictionary X"));
			assertThat(dictionaryMetadata.getDescription(), is("Dictionary X. The best dictionary in the world"));
			assertThat(dictionaryMetadata.getVersion(), is("2.4.2"));
			assertThat(dictionaryMetadata.getDate(), is("2008.07.21"));
			assertThat(dictionaryMetadata.getWordCount(), is(1234));
			assertThat(dictionaryMetadata.getIndexOffsetSize(), is(BITS_64));
			assertThat(dictionaryMetadata.getPhraseDefinitionPartFormat().elementTypes().next(), is(XDXF));
		} catch (IOException e) {
			dictionaryMdSource.close();
		}
	}

	@Test
	public void shouldApplyMetadataDefaults() throws IOException {
		InputStream dictionaryMdSource = new ByteArrayInputStream("bookname=X\nwordcount=1".getBytes());
		DictionaryMetadataReader dictionaryMetadataReader = new DictionaryMetadataReader(dictionaryMdSource);
		DictionaryMetadata dictionaryMetadata = dictionaryMetadataReader.read();
		assertThat(dictionaryMetadata.getIndexOffsetSize(), is(BITS_32));
	}

	@Test(expected = RuntimeException.class)
	public void shouldFailIfNameIsMissing() throws IOException {
		InputStream dictionaryMdSource = new ByteArrayInputStream("wordcount=1".getBytes());
		DictionaryMetadataReader dictionaryMetadataReader = new DictionaryMetadataReader(dictionaryMdSource);
		dictionaryMetadataReader.read();
	}

	@Test(expected = RuntimeException.class)
	public void shouldFailIfWordCountIsMissing() throws IOException {
		InputStream dictionaryMdSource = new ByteArrayInputStream("bookname=X".getBytes());
		DictionaryMetadataReader dictionaryMetadataReader = new DictionaryMetadataReader(dictionaryMdSource);
		dictionaryMetadataReader.read();
	}
}
