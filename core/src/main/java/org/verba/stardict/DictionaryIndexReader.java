package org.verba.stardict;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class DictionaryIndexReader {
	private InputStream dictionaryIndexPayloadStream;
	private ByteBuffer targetWordBuffer;
	
	public DictionaryIndexReader(InputStream aDictionaryIndexPayloadStream) {
		dictionaryIndexPayloadStream = aDictionaryIndexPayloadStream;
		targetWordBuffer = ByteBuffer.allocate(256);
	}

	public WordCoordinates readWordCoordinates() throws IOException {
		int nextByte = 0;
		
		while ((nextByte = dictionaryIndexPayloadStream.read()) != -1 && nextByte != '\0') {
			targetWordBuffer.put((byte) nextByte);
		}
		
		readExtras();
		
		String targetWord = new String(targetWordBuffer.array(), 0, targetWordBuffer.position());
		targetWordBuffer.rewind();
		
		return new WordCoordinates(targetWord);
	}

	private void readExtras() throws IOException {
		byte[] byteBuffer = new byte[4];
		// bytes for word offset
		dictionaryIndexPayloadStream.read(byteBuffer);
		// bytes for word length
		dictionaryIndexPayloadStream.read(byteBuffer);
	}
	
	private long convertBytesToLong(byte[] byteBuffer) {
		long value = 0;
		
		for (int i = 0; i < byteBuffer.length; i++) {
			value = (value << 8) + (byteBuffer[i] & 0xff);
		}
		
		return value;
	}

}
