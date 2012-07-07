package org.verba.stardict;

public enum IndexOffsetSize {
	BITS_64, BITS_32;

	public static IndexOffsetSize fromString(String internal) {
		return internal.equals("64") ? BITS_64 : BITS_32;
	}
}