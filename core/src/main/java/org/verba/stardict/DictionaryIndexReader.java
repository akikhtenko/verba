package org.verba.stardict;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.nio.ByteBuffer;

public class DictionaryIndexReader {
	private PushbackInputStream dictionaryIndexPayloadStream;
	private ByteBuffer targetWordBuffer;
	
	int nextByte = 0;
	
	public DictionaryIndexReader(InputStream aDictionaryIndexPayloadStream) {
		dictionaryIndexPayloadStream = new PushbackInputStream(aDictionaryIndexPayloadStream);
		targetWordBuffer = ByteBuffer.allocate(256);
	}

	public WordCoordinates readWordCoordinates() throws IOException {		
		return new WordCoordinates(readTargetWord(), readWordDefinitionOffset(), readWordDefinitionLength());
	}
	
	public boolean hasNextWordDefinition() throws IOException {
		return !nextByteWillBeEndOfStream();
	}

	private String readTargetWord() throws IOException {
		for (readNextByte(); !isEndOfStreamReached() && !isEndOfTargetWordReached(); readNextByte()) {
			targetWordBuffer.put((byte) nextByte);
		}
		
		ensureNotEndOfStream();
		
		return flushFromTargetWordBuffer();
	}

	private String flushFromTargetWordBuffer() {
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

	private boolean isEndOfStreamReached() throws IOException {
		return isEndOfStreamByte(nextByte);
	}
	
	private boolean isEndOfStreamByte(int byteToCheck) throws IOException {
		if (byteToCheck == -1) {
			return true;
		} else {
			return false;
		}
	}

	private long readWordDefinitionOffset() throws IOException {
		return convertBytesToLong(readNextBytes(4));
	}
	
	private long readWordDefinitionLength() throws IOException {
		return convertBytesToLong(readNextBytes(4));
	}

	private void readNextByte() throws IOException {
		nextByte = dictionaryIndexPayloadStream.read();
	}
	
	private boolean nextByteWillBeEndOfStream() throws IOException {
		int byteAhead = dictionaryIndexPayloadStream.read();
		boolean nextByteEndOfStream = isEndOfStreamByte(byteAhead);
		dictionaryIndexPayloadStream.unread(byteAhead);
		
		return nextByteEndOfStream;
	}
	
	private byte[] readNextBytes(int numOfBytesToRead) throws IOException {
		byte[] byteBuffer = new byte[numOfBytesToRead];
		
		int numOfBytesRead = dictionaryIndexPayloadStream.read(byteBuffer);
		ensureDidNotHitEndOfStream(numOfBytesToRead, numOfBytesRead);
		
		return byteBuffer;
	}
	
	private void ensureNotEndOfStream() throws IOException {
		if (isEndOfStreamReached()) {
			throw new RuntimeException("Unexpected index end reached while reading target word");
		}
	}
	
	private void ensureDidNotHitEndOfStream(int numOfBytesToRead, int numOfBytesRead) {
		if (numOfBytesRead != numOfBytesToRead) {
			throw new RuntimeException("Unexpected index end reached while reading word definition offset");
		}
	}
	
	private long convertBytesToLong(byte[] byteBuffer) {
		long value = 0;
		
		for (int i = 0; i < byteBuffer.length; i++) {
			value = (value << 8) + (byteBuffer[i] & 0xff);
		}
		
		return value;
	}

}
