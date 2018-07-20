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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertPathBuilderException;
import java.security.cert.CertStoreException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.zip.DataFormatException;

import org.bouncycastle.cms.CMSException;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.util.encoders.Base64;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.infinities.keystone4j.ssl.CertificateVerificationException;
import com.infinities.keystone4j.utils.Cms.Algorithm;

public class CmsTest {

	private String signed;
	private String token, token2;
	private String signingCertFileName;
	private String signingCaFileName;


	@Before
	public void setUp() throws Exception {
		URL tokenUrl = Thread.currentThread().getContextClassLoader().getResource("token.txt");
		token = Resources.toString(tokenUrl, Charsets.UTF_8);

		URL tokenUrl2 = Thread.currentThread().getContextClassLoader().getResource("token2.txt");
		token2 = Resources.toString(tokenUrl2, Charsets.UTF_8);

		URL revokedUrl = Thread.currentThread().getContextClassLoader().getResource("revokedwrapper2.txt");
		RevokedToken revokedWrapper = JsonUtils.readJson(new File(revokedUrl.getPath()), RevokedToken.class);
		signed = revokedWrapper.getSigned();
		signingCertFileName = Thread.currentThread().getContextClassLoader().getResource("signing_cert.pem").getPath();
		signingCaFileName = Thread.currentThread().getContextClassLoader().getResource("cacert.pem").getPath();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCmsVerifyStringStringString() throws CertificateException, OperatorCreationException,
			NoSuchAlgorithmException, NoSuchProviderException, CertPathBuilderException, InvalidAlgorithmParameterException,
			CMSException, IOException, CertificateVerificationException {
		String verify = Cms.cmsVerify(signed, this.signingCertFileName, this.signingCaFileName);
		assertEquals("{\"revoked\": []}", verify);
	}

	@Test
	public void testCmsVerifyStringStringStringString() throws CertificateException, OperatorCreationException,
			NoSuchAlgorithmException, NoSuchProviderException, CertPathBuilderException, InvalidAlgorithmParameterException,
			CMSException, IOException, CertificateVerificationException {
		String inform = Cms.PKI_ASN1_FORM;
		String verify = Cms.cmsVerify(signed, this.signingCertFileName, this.signingCaFileName, inform);
		assertEquals("{\"revoked\": []}", verify);
	}

	@Test
	public void testCmsVerifyStringStringStringString2() throws CertificateException, OperatorCreationException,
			NoSuchAlgorithmException, NoSuchProviderException, CertPathBuilderException, InvalidAlgorithmParameterException,
			CMSException, IOException, CertificateVerificationException {
		String inform = Cms.PKI_ASN1_FORM;
		token = Cms.tokenToCms(this.token);
		String verify = Cms.cmsVerify(token, this.signingCertFileName, this.signingCaFileName, inform);
		URL novaUrl = Thread.currentThread().getContextClassLoader().getResource("nova.txt");
		String expect = Resources.toString(novaUrl, Charsets.UTF_8);
		assertEquals(expect, verify.trim());
	}

	@Test
	public void testCmsVerifyStringStringStringString3() throws CertificateException, OperatorCreationException,
			NoSuchAlgorithmException, NoSuchProviderException, CertPathBuilderException, InvalidAlgorithmParameterException,
			CMSException, IOException, CertificateVerificationException {
		String inform = Cms.PKI_ASN1_FORM;
		token2 = Cms.tokenToCms(this.token2);
		String verify = Cms.cmsVerify(token2, this.signingCertFileName, this.signingCaFileName, inform);
		URL novaUrl = Thread.currentThread().getContextClassLoader().getResource("nova2.txt");
		String expect = Resources.toString(novaUrl, Charsets.UTF_8);
		assertEquals(expect, verify.trim());
	}

	@Test
	public void testCmsVerifyStringStringStringString4() throws CertificateException, OperatorCreationException,
			NoSuchAlgorithmException, NoSuchProviderException, CertPathBuilderException, InvalidAlgorithmParameterException,
			CMSException, IOException, CertificateVerificationException {
		URL revokedUrl = Thread.currentThread().getContextClassLoader().getResource("revokedwrapper3.txt");
		RevokedToken revokedWrapper = JsonUtils.readJson(new File(revokedUrl.getPath()), RevokedToken.class);
		String signed = revokedWrapper.getSigned();
		String inform = Cms.PKI_ASN1_FORM;
		String verify = Cms.cmsVerify(signed, this.signingCertFileName, this.signingCaFileName, inform);
		assertEquals("{\"revoked\": []}", verify);
	}

	// @Test
	// public void testCmsVerifyStringStringStringString3() throws
	// CertificateException, OperatorCreationException,
	// NoSuchAlgorithmException, NoSuchProviderException,
	// CertPathBuilderException, InvalidAlgorithmParameterException,
	// CMSException, IOException, CertificateVerificationException {
	// String key = PropertiesHolder.CONFIG_FOLDER + File.separator + "ssl" +
	// File.separator + "private" + File.separator
	// + "signing_key.pem";
	// URL novaUrl =
	// Thread.currentThread().getContextClassLoader().getResource("nova.txt");
	// String expect = Resources.toString(novaUrl, Charsets.UTF_8);
	// Cms.sign(expect, signingCertFileName, key);
	// }

	@Test
	public void testIsPkiz() {
		String tokenid1 = "PKIZ12345";
		String tokenid2 = "12345";
		assertTrue(Cms.isPkiz(tokenid1));
		assertFalse(Cms.isPkiz(tokenid2));
	}

	@Test
	public void testIsAsn1Token() {
		String tokenid1 = "MII12345";
		String tokenid2 = "12345";
		assertTrue(Cms.isAsn1Token(tokenid1));
		assertFalse(Cms.isAsn1Token(tokenid2));
	}

	@Test
	public void testPkizUncompress() throws DataFormatException, UnsupportedEncodingException {
		String tokenid = "12345678910";
		byte[] compress = CompressionUtils.compress(tokenid.getBytes());
		String encoded = "PKIZ" + new String(Base64.encode(compress), "UTF-8");
		// String pkiz = "PKIZ" + new String(compress);
		String ret = Cms.pkizUncompress(encoded);
		assertEquals(tokenid, ret);
	}

	@Test
	public void testTokenToCms() {
		String tokenid = "12345";
		String tokenid1 = "-----BEGIN CMS-----\n" + tokenid + "\n-----END CMS-----\n";
		assertEquals(tokenid1, Cms.tokenToCms(tokenid));
	}

	@Test
	public void testCmsHashToken() {
		String tokenid = "PKIZ12345";
		assertTrue(Cms.cmsHashToken(tokenid, Algorithm.md5).matches("[a-fA-F0-9]{32}"));
		String tokenid2 = "12345";
		assertEquals(tokenid2, Cms.cmsHashToken(tokenid2, Algorithm.md5));
		String tokenid3 = "MII12345";
		assertTrue(Cms.cmsHashToken(tokenid3, Algorithm.md5).matches("[a-fA-F0-9]{32}"));
		assertTrue(Cms.cmsHashToken(tokenid, Algorithm.sha1).matches("[a-fA-F0-9]{40}"));
		assertEquals(tokenid2, Cms.cmsHashToken(tokenid2, Algorithm.sha1));
		assertTrue(Cms.cmsHashToken(tokenid3, Algorithm.sha1).matches("[a-fA-F0-9]{40}"));

		assertTrue(Cms.cmsHashToken(tokenid, Algorithm.sha256).matches("[a-fA-F0-9]{64}"));
		assertEquals(tokenid2, Cms.cmsHashToken(tokenid2, Algorithm.sha256));
		assertTrue(Cms.cmsHashToken(tokenid, Algorithm.sha256).matches("[a-fA-F0-9]{64}"));
		assertTrue(Cms.cmsHashToken(tokenid, Algorithm.sha512).matches("[a-fA-F0-9]{128}"));
		assertEquals(tokenid2, Cms.cmsHashToken(tokenid2, Algorithm.sha512));
		assertTrue(Cms.cmsHashToken(tokenid, Algorithm.sha512).matches("[a-fA-F0-9]{128}"));
	}

	@Test
	public void testVerify() throws CertificateException, NoSuchAlgorithmException, NoSuchProviderException,
			OperatorCreationException, CertStoreException, InvalidKeySpecException, IOException, CMSException,
			CertPathBuilderException, InvalidAlgorithmParameterException, CertificateVerificationException {
		String certfile = "conf" + File.separator + "ssl" + File.separator + "certs" + File.separator + "signing_cert.pem";
		String keyfile = "conf" + File.separator + "ssl" + File.separator + "private" + File.separator + "signing_key.pem";
		String data = "test";
		String signedText = Cms.signText(data, certfile, keyfile);
		System.err.println("signed:" + signedText);
		String inform = Cms.PKI_ASN1_FORM;
		String signingCertFileName = "conf" + File.separator + "signing_cert.pem";
		String signingCaFileName = "conf" + File.separator + "cacert.pem";

		String result = Cms.cmsVerify(signedText, signingCertFileName, signingCaFileName, inform);

		String formatted = signedText.replace("-----BEGIN CMS-----", "").replace("-----END CMS-----", "").trim();

		result = Cms.cmsVerify(formatted, signingCertFileName, signingCaFileName);
		System.err.println(result);
	}


	public static class RevokedToken implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String signed;


		public String getSigned() {
			return signed;
		}

		public void setSigned(String signed) {
			this.signed = signed;
		}

	}

}
