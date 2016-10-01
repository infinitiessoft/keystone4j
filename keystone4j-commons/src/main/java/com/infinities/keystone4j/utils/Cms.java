/*******************************************************************************
 * Copyright 2015 InfinitiesSoft Solutions Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package com.infinities.keystone4j.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertPathBuilderException;
import java.security.cert.CertStoreException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.DataFormatException;

import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import com.infinities.keystone4j.ssl.Base64Verifier;
import com.infinities.keystone4j.ssl.CertificateVerificationException;
import com.infinities.keystone4j.ssl.X509CertificateParser;

public class Cms {

	public enum Algorithm {
		md5, sha1, sha256, sha512;
	}


	private final static Logger logger = LoggerFactory.getLogger(Cms.class);
	// private final static String ALGORITHM = "RSA/ECB/PKCS1Padding";
	public final static String PKI_ASN1_PREFIX = "MII";
	public final static String PKIZ_PREFIX = "PKIZ";
	public final static String PKIZ_CMS_FORM = "DER";
	public final static String PKI_ASN1_FORM = "PEM";
	private final static String BEGIN_CMS = "-----BEGIN CMS-----";
	private final static String END_CMS = "-----END CMS-----";
	// private Certificate cert;
	// private final Signature rsaSigner;
	private final static ConcurrentHashMap<String, X509Certificate> certMap = new ConcurrentHashMap<String, X509Certificate>();
	private final static ConcurrentHashMap<String, PrivateKey> keyMap = new ConcurrentHashMap<String, PrivateKey>();


	private Cms() {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		// readPrivateKey();
		// readPublicKey();
	}

	// certFile public_key & keyFile private_key
	public static String signToken(String text, String signingCertFileName, String signingKeyFile)
			throws CertificateException, NoSuchAlgorithmException, NoSuchProviderException, OperatorCreationException,
			IOException, CMSException, CertStoreException, InvalidKeySpecException {
		String output = cmsSignData(text, signingCertFileName, signingKeyFile, null);
		// return output;
		return cmsToToken(output);
	}

	private static String cmsToToken(String output) {
		output = output.replace('/', '-');
		output = output.replace(BEGIN_CMS, "");
		output = output.replace(END_CMS, "");
		output = output.replace("\n", "");
		return output;
	}

	public static String signText(String text, String signingCertFileName, String signingKeyFile)
			throws CertificateException, NoSuchAlgorithmException, NoSuchProviderException, OperatorCreationException,
			IOException, CMSException, CertStoreException, InvalidKeySpecException {
		return cmsSignData(text, signingCertFileName, signingKeyFile, null);
	}

	@SuppressWarnings("rawtypes")
	private static String cmsSignData(String data, String signingCertFileName, String signingKeyFile, String outform)
			throws CertificateException, IOException, NoSuchAlgorithmException, NoSuchProviderException, CMSException,
			OperatorCreationException, CertStoreException, InvalidKeySpecException {
		if (Strings.isNullOrEmpty(outform)) {
			outform = PKI_ASN1_FORM;
		}

		Security.addProvider(new BouncyCastleProvider());
		CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
		logger.debug("signingCertFile: {}, caFile:{}", new Object[] { signingCertFileName, signingKeyFile });
		X509Certificate signercert = generateCertificate(signingCertFileName);
		PrivateKey key = generatePrivateKey(signingKeyFile);
		ContentSigner sha1Signer = new JcaContentSignerBuilder("SHA1withRSA").setProvider("BC").build(key);
		gen.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(new JcaDigestCalculatorProviderBuilder().setProvider(
				"BC").build()).build(sha1Signer, signercert));
		List<X509Certificate> certList = new ArrayList<X509Certificate>();
		certList.add(signercert);
		Store certs = new JcaCertStore(certList);
		gen.addCertificates(certs);
		CMSProcessableByteArray b = new CMSProcessableByteArray(data.getBytes());
		CMSSignedData signed = gen.generate(b, true);
		// return
		// BaseEncoding.base64Url().encode(signed.toASN1Structure().getEncoded("DER"));

		return new String(Base64.encode(signed.toASN1Structure().getEncoded("DER")), "UTF-8");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static String verifySignature(byte[] sigbytes, String signingCertFileName, String caFileName)
			throws CMSException, CertificateException, OperatorCreationException, NoSuchAlgorithmException,
			NoSuchProviderException, CertPathBuilderException, InvalidAlgorithmParameterException, IOException,
			CertificateVerificationException {
		logger.debug("signingCertFile: {}, caFile:{}", new Object[] { signingCertFileName, caFileName });
		Security.addProvider(new BouncyCastleProvider());
		X509Certificate signercert = generateCertificate(signingCertFileName);
		X509Certificate cacert = generateCertificate(caFileName);
		Set<X509Certificate> additionalCerts = new HashSet<X509Certificate>();
		additionalCerts.add(cacert);

		// CertificateVerifier.verifyCertificate(signercert, additionalCerts,
		// false); // .validateKeyChain(signercert,
		// certs);
		if (Base64Verifier.isBase64(sigbytes)) {
			try {
				String s = new String(sigbytes, "UTF-8");
				// s = s.replace('-', '+'); // 62nd char of encoding
				// s = s.replace('_', '/'); // 63rd char of encoding
				sigbytes = Base64.decode(s);
				// sigbytes = BaseEncoding.base64Url().decode(new
				// String(sigbytes, "UTF-8"));
				logger.debug("Signature file is BASE64 encoded");
			} catch (Exception ioe) {
				logger.warn("Problem decoding from b64", ioe);
			}
		}

		// --- Use Bouncy Castle provider to verify included-content CSM/PKCS#7
		// signature ---
		try {
			logger.debug("sigbytes size: {}", sigbytes.length);
			CMSSignedData s = new CMSSignedData(sigbytes);
			Store store = s.getCertificates();
			SignerInformationStore signers = s.getSignerInfos();
			Collection c = signers.getSigners();
			Iterator it = c.iterator();
			int verified = 0;

			while (it.hasNext()) {
				X509Certificate cert = null;
				SignerInformation signer = (SignerInformation) it.next();
				Collection certCollection = store.getMatches(signer.getSID());
				if (certCollection.isEmpty() && signercert == null)
					continue;
				else if (signercert != null) // use a signer cert file for
												// verification, if it was
												// provided
					cert = signercert;
				else { // use the certificates included in the signature for
						// verification
					Iterator certIt = certCollection.iterator();
					cert = (X509Certificate) certIt.next();
				}

				if (signer.verify(new JcaSimpleSignerInfoVerifierBuilder().setProvider("BC").build(cert)))
					verified++;
			}

			if (verified == 0) {
				logger.warn(" No signers' signatures could be verified !");
			} else if (signercert != null)
				logger.info("Verified a signature using signer certificate file  {}", signingCertFileName);
			else
				logger.info("Verified a signature using a certificate in the signature data");

			CMSProcessableByteArray cpb = (CMSProcessableByteArray) s.getSignedContent();
			byte[] rawcontent = (byte[]) cpb.getContent();

			return new String(rawcontent);
		} catch (Exception ex) {
			logger.error("Couldn't verify included-content CMS signature", ex);
			throw new RuntimeException("Couldn't verify included-content CMS signature", ex);
		}
	}

	public static byte[] DERtoPEM(byte[] bytes, String headfoot) {
		ByteArrayOutputStream pemStream = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(pemStream);

		byte[] stringBytes = Base64.encode(bytes);

		String encoded = new String(stringBytes);

		if (headfoot != null) {
			writer.print("-----BEGIN " + headfoot + "-----\n");
		}

		// write 64 chars per line till done
		int i = 0;
		while ((i + 1) * 64 < encoded.length()) {
			writer.print(encoded.substring(i * 64, (i + 1) * 64));
			writer.print("\n");
			i++;
		}
		if (encoded.length() % 64 != 0) {
			writer.print(encoded.substring(i * 64)); // write remainder
			writer.print("\n");
		}
		if (headfoot != null) {
			writer.print("-----END " + headfoot + "-----\n");
		}
		writer.flush();
		return pemStream.toByteArray();
	}

	private static X509Certificate generateCertificate(String certFilePath) throws CertificateException, IOException {
		X509Certificate cert = certMap.get(certFilePath);
		if (cert != null) {
			return cert;
		} else {
			synchronized (certMap) {
				cert = certMap.get(certFilePath);
				if (cert != null) {
					return cert;
				}
				File f = new File(certFilePath);
				logger.debug("cert file {} exist? {}", new Object[] { certFilePath, f.exists() });
				cert = X509CertificateParser.parse(f);
				certMap.put(certFilePath, cert);
				return cert;
			}
		}
	}

	private static PrivateKey generatePrivateKey(String signingKeyFile) throws CertificateException, IOException,
			InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException {
		PrivateKey key = keyMap.get(signingKeyFile);
		if (key != null) {
			return key;
		} else {
			synchronized (keyMap) {
				key = keyMap.get(signingKeyFile);
				if (key != null) {
					return key;
				}
				key = X509CertificateParser.parseKey(new File(signingKeyFile));
				keyMap.put(signingKeyFile, key);
				return key;
			}
		}
	}

	public static boolean isPkiz(String userToken) {
		return userToken.startsWith(PKIZ_PREFIX);
	}

	public static boolean isAsn1Token(String userToken) {
		return userToken.startsWith(PKI_ASN1_PREFIX);
	}

	public static String hashToken(String tokenid, Algorithm mode) throws UnsupportedEncodingException,
			NoSuchAlgorithmException {
		if (mode == null) {
			mode = Algorithm.md5;
		}

		if (Strings.isNullOrEmpty(tokenid)) {
			throw new NullPointerException("invalid tokenid");
		}

		if (isAsn1Token(tokenid) || isPkiz(tokenid)) {
			HashFunction hf = Hashing.md5();
			if (mode == Algorithm.sha1) {
				hf = Hashing.sha1();
			} else if (mode == Algorithm.sha256) {
				hf = Hashing.sha256();
			} else if (mode == Algorithm.sha512) {
				hf = Hashing.sha512();
			}
			HashCode hc = hf.newHasher().putString(tokenid).hash();
			return toHex(hc.asBytes());

		} else {
			return tokenid;
		}
	}

	public static String toHex(byte[] digest) {
		StringBuilder sb = new StringBuilder();
		for (byte b : digest) {
			sb.append(String.format("%1$02X", b));
		}

		return sb.toString();
	}

	public static String tokenToCms(String signedText) {
		logger.debug("token: {}", signedText);
		String copyOfText = signedText.replace('-', '/');
		String formatted = "-----BEGIN CMS-----\n";
		int lineLength = 64;
		while (copyOfText.length() > 0) {
			if (copyOfText.length() > lineLength) {
				formatted += copyOfText.substring(0, lineLength);
				copyOfText = copyOfText.substring(lineLength);
			} else {
				formatted += copyOfText;
				copyOfText = "";
			}
			formatted += "\n";
		}
		formatted += "-----END CMS-----\n";
		logger.debug("Cms: {}", formatted);
		return formatted;
	}

	public static String pkizSign(String text, String certfile, String keyfile) throws CertificateException,
			NoSuchAlgorithmException, NoSuchProviderException, OperatorCreationException, CertStoreException, IOException,
			CMSException, InvalidKeySpecException {
		String signed = cmsSignData(text, certfile, keyfile, PKIZ_CMS_FORM);
		byte[] compressed = CompressionUtils.compress(signed.getBytes());
		String encoded = PKIZ_PREFIX + BaseEncoding.base64Url().encode(compressed);
		return encoded;
	}

	public static String cmsVerify(String formatted, String signingCertFileName, String caFileName)
			throws CertificateException, OperatorCreationException, NoSuchAlgorithmException, NoSuchProviderException,
			CertPathBuilderException, InvalidAlgorithmParameterException, CMSException, IOException,
			CertificateVerificationException {
		return cmsVerify(formatted, signingCertFileName, caFileName, PKI_ASN1_FORM);
	}

	public static String cmsVerify(String formatted, String signingCertFileName, String caFileName, String inform)
			throws CertificateException, OperatorCreationException, NoSuchAlgorithmException, NoSuchProviderException,
			CertPathBuilderException, InvalidAlgorithmParameterException, CMSException, IOException,
			CertificateVerificationException {
		logger.debug("data verify: {}", formatted);
		formatted = formatted.replace(BEGIN_CMS, "").replace(END_CMS, "").trim();
		logger.debug("after formatted data: {}", formatted);
		byte[] data = encodingForForm(formatted, inform);

		return verifySignature(data, signingCertFileName, caFileName);
	}

	private static byte[] encodingForForm(String formatted, String inform) {
		if (PKI_ASN1_FORM.equals(inform)) {
			return formatted.getBytes(Charsets.UTF_8);
		} else if (PKIZ_CMS_FORM.equals(inform)) {
			return Hex.encode(formatted.getBytes());
		}
		return null;
	}

	public static String pkizUncompress(String signedText) throws DataFormatException {
		String text = signedText.substring(PKIZ_PREFIX.length());
		text = text.replace('-', '+'); // 62nd char of encoding
		text = text.replace('_', '/'); // 63rd char of encoding
		byte[] unencoded = Base64.decode(text);
		byte[] uncompressedByte = CompressionUtils.decompress(unencoded);
		return new String(uncompressedByte);
	}

	public static String cmsHashToken(String tokenid, Algorithm mode) {
		if (mode == null) {
			mode = Algorithm.md5;
		}

		if (Strings.isNullOrEmpty(tokenid)) {
			throw new NullPointerException("invalid tokenid");
		}

		if (isAsn1Token(tokenid) || isPkiz(tokenid)) {
			HashFunction hf = Hashing.md5();
			if (mode == Algorithm.sha1) {
				hf = Hashing.sha1();
			} else if (mode == Algorithm.sha256) {
				hf = Hashing.sha256();
			} else if (mode == Algorithm.sha512) {
				hf = Hashing.sha512();
			}
			HashCode hc = hf.newHasher().putString(tokenid).hash();
			return toHex(hc.asBytes());

		} else {
			return tokenid;
		}
	}

}
