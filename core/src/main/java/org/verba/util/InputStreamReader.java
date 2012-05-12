package org.verba.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

public class InputStreamReader {
	private static final int END_OF_STREAM_BYTE = -1;
	private PushbackInputStream stream;
	
	public InputStreamReader(InputStream inputStream) {
		stream  = new PushbackInputStream(new BufferedInputStream(inputStream));
	}
	
	public int readNextByte() throws IOException {
		int nextByte = stream.read();
		
		ensureDidNotHitEndOfStream(nextByte);
		
		return nextByte;
	}
	
	public boolean isNextByteEndOfStream() throws IOException {
		int byteAhead = stream.read();
		boolean nextByteEndOfStream = isEndOfStreamByte(byteAhead);
		stream.unread(byteAhead);
		
		return nextByteEndOfStream;
	}
	
	public byte[] readNextBytes(int numOfBytesToRead) throws IOException {
		byte[] byteBuffer = new byte[numOfBytesToRead];
		
		int numOfBytesRead = stream.read(byteBuffer);
		ensureDidNotHitEndOfStream(numOfBytesToRead, numOfBytesRead);
		
		return byteBuffer;
	}
	

	public byte[] readBytesAtOffset(long bytesOffset, int numOfBytesToRead) throws IOException {
		stream.skip(bytesOffset);

		return readNextBytes(numOfBytesToRead);
	}
	
	public void close() throws IOException {
		stream.close();
	}
	
	private void ensureDidNotHitEndOfStream(int byteToCheck) throws IOException {
		if (isEndOfStreamByte(byteToCheck)) {
			throw new RuntimeException("Unexpected end of stream reached");
		}
	}
	
	private void ensureDidNotHitEndOfStream(int numOfBytesToRead, int numOfBytesRead) {
		if (numOfBytesRead != numOfBytesToRead) {
			throw new RuntimeException("Unexpected end of stream reached");
		}
	}
	
	private boolean isEndOfStreamByte(int byteToCheck) throws IOException {
		if (byteToCheck == END_OF_STREAM_BYTE) {
			return true;
		} else {
			return false;
		}
	}
}
