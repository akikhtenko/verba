package org.verba.stardict;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.verba.util.InputStreamReader;

public class DictionaryIndexReader implements Closeable {
	private static final int BITS_PER_BYTE = 8;
	private static final int MAX_WORD_LENGTH = 256;
	private InputStreamReader streamReader;
	private ByteBuffer targetWordBuffer;

	int nextByte = 0;

	public DictionaryIndexReader(InputStream aDictionaryIndexPayloadStream) {
		streamReader = new InputStreamReader(aDictionaryIndexPayloadStream);
		targetWordBuffer = ByteBuffer.allocate(MAX_WORD_LENGTH);
	}

	public boolean hasNextPhraseDefinitionCoordinates() throws IOException {
		return !streamReader.isNextByteEndOfStream();
	}

	public PhraseDefinitionCoordinates readPhraseDefinitionCoordinates() throws IOException {
		return new PhraseDefinitionCoordinates(readTargetWord(), readPhraseDefinitionOffset(), readPhraseDefinitionLength());
	}

	@Override
	public void close() throws IOException {
		streamReader.close();
	}

	private String readTargetWord() throws IOException {
		for (readNextByte(); !isEndOfTargetWordReached(); readNextByte()) {
			appendByteToTheTargetWord();
		}

		return getTargetWord();
	}

	private void appendByteToTheTargetWord() {
		targetWordBuffer.put((byte) nextByte);
	}

	private String getTargetWord() {
		String targetWord = new String(targetWordBuffer.array(), 0, targetWordBuffer.position());
		targetWordBuffer.rewind();

		return targetWord;
	}

	private boolean isEndOfTargetWordReached() {
		return nextByte == '\0';
	}

	private long readPhraseDefinitionOffset() throws IOException {
		return convertBytesToLong(streamReader.readNextBytes(4));
	}

	private int readPhraseDefinitionLength() throws IOException {
		return (int) convertBytesToLong(streamReader.readNextBytes(4));
	}

	private void readNextByte() throws IOException {
		nextByte = streamReader.readNextByte();
	}

	private long convertBytesToLong(byte[] byteBuffer) {
		long value = 0;

		for (int i = 0; i < byteBuffer.length; i++) {
			value = (value << BITS_PER_BYTE) + (byteBuffer[i] & 0xff);
		}

		return value;
	}

}
