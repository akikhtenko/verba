package org.verba.util;

import java.util.Iterator;

public class ByteArray {
	private byte[] bytes;

	public ByteArray(byte[] bytes) {
		this.bytes = bytes.clone();
	}

	public ByteArray(final byte[] bytes, final int start, final int length) {
		this.bytes = new byte[length];
		System.arraycopy(bytes, start, this.bytes, 0, length);
	}

	public byte[] asBytes() {
		return bytes;
	}

	public Iterable<ByteArray> split(final byte[] delim) {
		return new Iterable<ByteArray>() {
			@Override
			public Iterator<ByteArray> iterator() {
				return new Iterator<ByteArray>() {
					private int byteArrayIndex = 0;

					@Override
					public boolean hasNext() {
						return byteArrayIndex < bytes.length;
					}

					@Override
					public ByteArray next() {
						final int start = byteArrayIndex;
						int delimiterIndex = 0;
						while (byteArrayIndex < bytes.length) {
							if (bytes[byteArrayIndex] == delim[delimiterIndex]) {
								byteArrayIndex++;
								delimiterIndex++;
								if (delimiterIndex == delim.length) {
									return new ByteArray(bytes, start, (byteArrayIndex - delimiterIndex - start));
								}
							} else if (delimiterIndex > 0) {
								byteArrayIndex -= delimiterIndex;
								byteArrayIndex++;
								delimiterIndex = 0;
							} else {
								byteArrayIndex++;
							}
						}
						return handleEndOfArray(start);
					}

					private ByteArray handleEndOfArray(final int start) {
						return start > 0 ? new ByteArray(bytes, start, byteArrayIndex - start) : ByteArray.this;
					}

					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}
}
