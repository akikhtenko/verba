package org.verba.stardict;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.verba.util.InputStreamReader;

public class DictionaryIndexReader {
	private InputStreamReader streamReader;
	private ByteBuffer targetWordBuffer;
	
	int nextByte = 0;
	
	public DictionaryIndexReader(InputStream aDictionaryIndexPayloadStream) {
		streamReader = new InputStreamReader(aDictionaryIndexPayloadStream);
		targetWordBuffer = ByteBuffer.allocate(256);
	}
	
	public boolean hasNextWordDefinition() throws IOException {
		return !streamReader.isNextByteEndOfStream();
	}

	public WordDefinitionCoordinates readWordCoordinates() throws IOException {		
		return new WordDefinitionCoordinates(readTargetWord(), readWordDefinitionOffset(), readWordDefinitionLength());
	}
	
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
		if (nextByte == '\0') {
			return true;
		} else {
			return false;
		}
	}

	private long readWordDefinitionOffset() throws IOException {
		return convertBytesToLong(streamReader.readNextBytes(4));
	}
	
	private int readWordDefinitionLength() throws IOException {
		return (int) convertBytesToLong(streamReader.readNextBytes(4));
	}

	private void readNextByte() throws IOException {
		nextByte = streamReader.readNextByte();
	}

	private long convertBytesToLong(byte[] byteBuffer) {
		long value = 0;
		
		for (int i = 0; i < byteBuffer.length; i++) {
			value = (value << 8) + (byteBuffer[i] & 0xff);
		}
		
		return value;
	}

}
