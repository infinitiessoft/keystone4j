package com.infinities.keystone4j.ssl;

public class Base64Verifier {

	private Base64Verifier() {

	}


	/**
	 * This array is a lookup table that translates Unicode characters drawn
	 * from the "Base64 Alphabet" (as specified in Table 1 of RFC 2045) into
	 * their 6-bit positive integer equivalents. Characters that are not in the
	 * Base64 alphabet but fall within the bounds of the array are translated to
	 * -1.
	 * 
	 * Note: '+' and '-' both decode to 62. '/' and '_' both decode to 63. This
	 * means decoder seamlessly handles both URL_SAFE and STANDARD base64. (The
	 * encoder, on the other hand, needs to know ahead of time what to emit).
	 * 
	 * Thanks to "commons" project in ws.apache.org for this code.
	 * http://svn.apache.org/repos/asf/webservices/commons/trunk/modules/util/
	 */
	private static final byte[] DECODE_TABLE = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, 62, -1,
			63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
			12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33,
			34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51 };

	protected static final byte PAD_DEFAULT = '=';


	/**
	 * Returns whether or not the <code>octet</code> is in the base 64 alphabet.
	 * 
	 * @param octet
	 *            The value to test
	 * @return <code>true</code> if the value is defined in the the base 64
	 *         alphabet, <code>false</code> otherwise.
	 * @since 1.4
	 */
	public static boolean isBase64(byte octet) {
		return octet == PAD_DEFAULT || (octet >= 0 && octet < DECODE_TABLE.length && DECODE_TABLE[octet] != -1);
	}

	public static boolean isBase64(String text) {
		return text.matches("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$");
	}

	/**
	 * Tests a given byte array to see if it contains only valid characters
	 * within the Base64 alphabet. Currently the method treats whitespace as
	 * valid.
	 * 
	 * @param arrayOctet
	 *            byte array to test
	 * @return <code>true</code> if all bytes are valid characters in the Base64
	 *         alphabet or if the byte array is empty; <code>false</code>,
	 *         otherwise
	 * @since 1.5
	 */
	public static boolean isBase64(byte[] arrayOctet) {
		for (int i = 0; i < arrayOctet.length; i++) {
			if (!isBase64(arrayOctet[i]) && !isWhiteSpace(arrayOctet[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if a byte value is whitespace or not. Whitespace is taken to mean:
	 * space, tab, CR, LF
	 * 
	 * @param byteToCheck
	 *            the byte to check
	 * @return true if byte is whitespace, false otherwise
	 */
	protected static boolean isWhiteSpace(final byte byteToCheck) {
		switch (byteToCheck) {
		case ' ':
		case '\n':
		case '\r':
		case '\t':
			return true;
		default:
			return false;
		}
	}

}
