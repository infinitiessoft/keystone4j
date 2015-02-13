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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.codec.DecoderException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;

public enum Cms {
	Instance;

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
	public String signToken(String text, String signingCertFileName, String signingKeyFile) throws CertificateException,
			NoSuchAlgorithmException, NoSuchProviderException, OperatorCreationException, IOException, CMSException,
			CertStoreException {
		String output = cmsSignData(text, signingCertFileName, signingKeyFile, null);
		return toToken(output);
	}

	private String toToken(String output) {
		output = output.replace('/', '-');
		output = output.replace(BEGIN_CMS, "");
		output = output.replace(END_CMS, "");
		output = output.replace("\n", "");
		return output;
	}

	public String signText(String text, String signingCertFileName, String signingKeyFile) throws CertificateException,
			NoSuchAlgorithmException, NoSuchProviderException, OperatorCreationException, IOException, CMSException,
			CertStoreException {
		return cmsSignData(text, signingCertFileName, signingKeyFile, null);
	}

	private String cmsSignData(String data, String signingCertFileName, String signingKeyFile, String outform)
			throws CertificateException, IOException, NoSuchAlgorithmException, NoSuchProviderException, CMSException,
			OperatorCreationException, CertStoreException {
		if (Strings.isNullOrEmpty(outform)) {
			outform = PKI_ASN1_FORM;
		}

		Security.addProvider(new BouncyCastleProvider());
		CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
		logger.debug("signingCertFile: {}, caFile:{}", new Object[] { signingCertFileName, signingKeyFile });
		X509Certificate signercert = generateCertificate(signingCertFileName);
		// X509Certificate cacert = generateCertificate(caFileName);
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
		String signedContent = new String(DERtoPEM(signed.getContentInfo().getDEREncoded(), "CMS"));
		return signedContent;
	}

	@SuppressWarnings("rawtypes")
	public String verifySignature(byte[] sigbytes, String signingCertFileName, String caFileName) throws CMSException,
			CertificateException, OperatorCreationException, NoSuchAlgorithmException, NoSuchProviderException,
			CertPathBuilderException, InvalidAlgorithmParameterException, IOException {
		logger.debug("signingCertFile: {}, caFile:{}", new Object[] { signingCertFileName, caFileName });
		Security.addProvider(new BouncyCastleProvider());
		X509Certificate signercert = generateCertificate(signingCertFileName);
		X509Certificate cacert = generateCertificate(caFileName);
		Set<X509Certificate> additionalCerts = new HashSet<X509Certificate>();
		additionalCerts.add(cacert);

		sigbytes = Base64.decode(sigbytes);

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

		byte[] stringBytes = BaseEncoding.base64().encode(bytes).getBytes();

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

	private static PrivateKey generatePrivateKey(String signingKeyFile) throws CertificateException, IOException {
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

	public String hashToken(String tokenid, Algorithm mode) throws UnsupportedEncodingException, NoSuchAlgorithmException,
			DecoderException {
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

	public String tokenToCms(String signedText) {
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

		return formatted;
	}

	public String pkizSign(String text, String certfile, String keyfile) throws CertificateException,
			NoSuchAlgorithmException, NoSuchProviderException, OperatorCreationException, CertStoreException, IOException,
			CMSException {
		String signed = cmsSignData(text, certfile, keyfile, PKIZ_CMS_FORM);
		byte[] compressed = CompressionUtils.compress(signed.getBytes());
		String encoded = PKIZ_PREFIX + BaseEncoding.base64Url().encode(compressed);
		return encoded;
	}
}
