package com.infinities.keystone4j.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.openssl.PEMReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.common.Config;

public enum Cms {
	Instance;

	// private final static String CERT_FILE = "certfile";
	private final static String KEY_FILE = "keyfile";
	private final static Logger logger = LoggerFactory.getLogger(Cms.class);

	// private Certificate cert;
	private final Signature rsaSigner;


	private Cms() {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

		String keyFile = Config.Instance.getOpt(Config.Type.signing, KEY_FILE).asText();
		URL url = new KeystoneUtils().getURL(keyFile);
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
			PrivateKey key = null;
			if (obj instanceof KeyPair) {
				KeyPair keyPair = (KeyPair) obj;
				key = keyPair.getPrivate();
			} else if (obj instanceof PrivateKey) {
				key = (PrivateKey) obj;
			} else {
				throw new RuntimeException("invalid pem format");
			}
			rsaSigner = Signature.getInstance("SHA1withRSA");
			rsaSigner.initSign(key);
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
	public String signToken(String text) throws SignatureException, UnsupportedEncodingException {
		String output = signText(text);
		return toToken(output);
	}

	private String toToken(String output) {
		return output;
	}

	private String signText(String text) throws SignatureException, UnsupportedEncodingException {
		logger.debug("text before sign: {}", text);
		rsaSigner.update(text.getBytes("UTF8"));
		byte[] signaturesBytes = rsaSigner.sign();
		String hex = Hex.encodeHexString(signaturesBytes);
		logger.debug("text after sign and hex: {}", hex);
		return hex;
	}

	public String hashToken(String tokenid) throws UnsupportedEncodingException, NoSuchAlgorithmException, DecoderException {
		logger.debug("text before hash: {}", tokenid);
		byte[] input = tokenid.getBytes();//
		// Hex.decodeHex(tokenid.toCharArray());//
		// Base64.decode(tokenid.getBytes("UTF8"));
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] output = md.digest(input);
		String hex = Hex.encodeHexString(output);
		logger.debug("text after hash and hex: {}", hex);
		return hex;
		// return tokenid;
	}
}
