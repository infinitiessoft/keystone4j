package com.infinities.keystone4j.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cms {

	// private final static String CERT_FILE = "certfile";
	// private final static String KEY_FILE = "keyfile";
	private final static Logger logger = LoggerFactory.getLogger(Cms.class);
	private final static String ALGORITHM = "RSA/ECB/PKCS1Padding";
	// private Certificate cert;
	// private final Signature rsaSigner;
	private RSAPublicKey publicKey;
	private RSAPrivateKey privateKey;


	public Cms(String certFile, String keyFile) {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		readPrivateKey(keyFile);
		readPublicKey(certFile);
	}

	private void readPrivateKey(String keyFile) {
		// String keyFile = Config.Instance.getOpt(Config.Type.signing,
		// "certfile").asText();
		// String keyFile = Config.Instance.getOpt(Config.Type.signing,
		// KEY_FILE).asText();
		URL url = URLUtils.Instance.getURL(keyFile);
		// URL url = Cms.class.getResource(keyFile);
		// File file = new File(url);
		FileReader fileReader;
		try {
			fileReader = new FileReader(url.getPath());
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		PEMReader pr = new PEMReader(bufferedReader);
		try {
			Object obj = pr.readObject();
			if (obj instanceof KeyPair) {
				KeyPair keyPair = (KeyPair) obj;
				privateKey = (RSAPrivateKey) keyPair.getPrivate();
				System.out.println("this is a keypair");
			} else if (obj instanceof PrivateKey) {
				privateKey = (RSAPrivateKey) obj;
				System.out.println("this is a private key");
			} else {
				throw new RuntimeException("invalid key format");
			}
		} catch (Exception e) {
			throw new RuntimeException("setup cerfile or keyfile fail", e);
		} finally {
			if (pr != null) {
				try {
					pr.close();
				} catch (IOException e) {
					// ignore
				}
			}

			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					// ignore
				}
			}

			if (fileReader != null) {
				try {
					fileReader.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}

	private void readPublicKey(String keyFile) {
		// String keyFile = Config.Instance.getOpt(Config.Type.signing,
		// CERT_FILE).asText();
		URL url = URLUtils.Instance.getURL(keyFile);
		// URL url = Cms.class.getResource(keyFile);
		// File file = new File(url);
		FileReader fileReader;
		try {
			fileReader = new FileReader(url.getPath());
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		PEMReader pr = new PEMReader(bufferedReader);
		try {
			Object obj = pr.readObject();
			if (obj instanceof PKCS10CertificationRequest) {
				PKCS10CertificationRequest request = (PKCS10CertificationRequest) obj;
				publicKey = (RSAPublicKey) request.getPublicKey();
				System.out.println("this is a PKCS10Certification");
			} else {
				throw new RuntimeException("invalid cer format");
			}
		} catch (Exception e) {
			throw new RuntimeException("setup cerfile or keyfile fail", e);
		} finally {
			if (pr != null) {
				try {
					pr.close();
				} catch (IOException e) {
					// ignore
				}
			}

			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					// ignore
				}
			}

			if (fileReader != null) {
				try {
					fileReader.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}

	// certFile public_key & keyFile private_key
	public String signToken(String text) throws SignatureException, UnsupportedEncodingException, InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		String output = signText(text);
		return toToken(output);
	}

	private String toToken(String output) {
		return output;
	}

	private String signText(String text) throws SignatureException, UnsupportedEncodingException, InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		logger.debug("key format: {}, {}", new Object[] { privateKey.getFormat(), privateKey.getAlgorithm() });
		logger.debug("text before sign: {}", text);
		return encrypt(text);
		// rsaSigner.update(text.getBytes("UTF8"));
		// byte[] signaturesBytes = rsaSigner.sign();
		// String hex = Hex.encodeHexString(signaturesBytes);
		// logger.debug("text after sign and hex: {}", hex);
		// return hex;
	}

	public String toCms(String signedText) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {
		return decrypt(signedText);
	}

	public String hashToken(String tokenid) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		logger.debug("text before hash: {}", tokenid);
		byte[] input = tokenid.getBytes();//
		// Hex.decodeHex(tokenid.toCharArray());//
		// Base64.decode(tokenid.getBytes("UTF8"));
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] output = md.digest(input);
		String hex = new String(Hex.encode(output));
		logger.debug("text after hash and hex: {}", hex);
		return hex;
		// return tokenid;
	}

	private String encrypt(String text) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] input = text.getBytes();
		byte[] output = blockCipher(cipher, input, Cipher.ENCRYPT_MODE);
		String toReturn = new String(Base64.encode(output));
		return toReturn;
	}

	private String decrypt(String text) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] input = Base64.decode(text);
		byte[] output = blockCipher(cipher, input, Cipher.DECRYPT_MODE);
		return new String(output);
	}

	public byte[] blockCipher(Cipher cipher, byte[] bytes, int mode) throws IllegalBlockSizeException, BadPaddingException {
		byte[] scrambled = new byte[0];
		byte[] toReturn = new byte[0];

		int length = (mode == Cipher.ENCRYPT_MODE) ? 100 : privateKey.getModulus().bitLength() / 8;

		byte[] buffer = new byte[length];
		for (int i = 0; i < bytes.length; i++) {
			if ((i > 0) && (i % length == 0)) {
				scrambled = cipher.doFinal(buffer);
				toReturn = append(toReturn, scrambled);
				int newlength = length;
				if (i + length > bytes.length) {
					newlength = bytes.length - i;
				}
				buffer = new byte[newlength];
			}
			buffer[i % length] = bytes[i];
		}
		scrambled = cipher.doFinal(buffer);
		toReturn = append(toReturn, scrambled);
		return toReturn;
	}

	private byte[] append(byte[] prefix, byte[] suffix) {

		byte[] toReturn = new byte[prefix.length + suffix.length];
		for (int i = 0; i < prefix.length; i++) {
			toReturn[i] = prefix[i];
		}
		for (int i = 0; i < suffix.length; i++) {
			toReturn[i + prefix.length] = suffix[i];
		}
		return toReturn;
	}
}
