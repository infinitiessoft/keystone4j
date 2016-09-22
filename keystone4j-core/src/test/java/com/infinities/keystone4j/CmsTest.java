/*******************************************************************************
 * # Copyright 2015 InfinitiesSoft Solutions Inc.
 * #
 * # Licensed under the Apache License, Version 2.0 (the "License"); you may
 * # not use this file except in compliance with the License. You may obtain
 * # a copy of the License at
 * #
 * #      http://www.apache.org/licenses/LICENSE-2.0
 * #
 * # Unless required by applicable law or agreed to in writing, software
 * # distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * # WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * # License for the specific language governing permissions and limitations
 * # under the License.
 *******************************************************************************/
package com.infinities.keystone4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.utils.Cms;

public class CmsTest {

	// private final String text = "{" + "\"auth\":{" + "\"identity\":{" +
	// "\"methods\":[" + "\"password\"" + "],"
	// + "\"password\":{" + "\"user\":{" +
	// "\"id\":\"0f3328f8-a7e7-41b4-830d-be8fdd5186c7\","
	// + "\"password\":\"admin\"" + "}" + "}" + "}" + "}" + "}";
	private final static Logger logger = LoggerFactory.getLogger(CmsTest.class);
	private String certFile;
	private String keyFile;
	private String caFile;


	@Before
	public void setUp() throws Exception {
		certFile = Config.getOpt(Config.Type.signing, "certfile").asText();
		keyFile = Config.getOpt(Config.Type.signing, "keyfile").asText();
		caFile = Config.getOpt(Config.Type.signing, "ca_certs").asText();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSignToken() throws Exception {
		String t = "1234567890";
		logger.debug("text size:{}", t.length());
		String output = Cms.signToken(t, certFile, keyFile);
		logger.debug("byte size: {}, sign: {}", new Object[] { output.getBytes().length, output });

		String input = Cms.verifySignature(output.getBytes(), certFile, caFile);
		logger.debug("decrypt: {}", input);
		assertEquals(t, input);
	}

	@Test
	public void testSignToken2() throws Exception {
		String t = "MIINFgYJKoZIhvcNAQcCoIINBzCCDQMCAQExCzAJBgUrDgMCGgUAMIIHbwYJKoZIhvcNAQcBoIIHYASCB1x7CiAgInRva2VuIiA6IHsKICAgICJjYXRhbG9nIiA6IFsgewogICAgICAiaWQiIDogIjNmODEzYjk5NDMxZTQ2YzBhZjg3NzM5M2Y2YWQ5MWQ3IiwKICAgICAgImxpbmtzIiA6IHsKICAgICAgICAic2VsZiIgOiBudWxsCiAgICAgIH0sCiAgICAgICJlbmRwb2ludHMiIDogWyB7CiAgICAgICAgImlkIiA6ICI5Nzc1ZjE3Y2JjODE0ZjhkODA5N2E1MjQ2ODBiYjMzYyIsCiAgICAgICAgImRlc2NyaXB0aW9uIiA6ICJrZXlzdG9uZSBlbmRwb2ludCIsCiAgICAgICAgImxpbmtzIiA6IHsKICAgICAgICAgICJzZWxmIiA6IG51bGwKICAgICAgICB9LAogICAgICAgICJ1cmwiIDogImh0dHA6Ly9sb2NhbGhvc3Q6MzUzNTcvdjMuMC8iLAogICAgICAgICJuYW1lIiA6ICJrZXlzdG9uZV9lbmRwb2ludCIsCiAgICAgICAgImVuYWJsZWQiIDogdHJ1ZSwKICAgICAgICAicmVnaW9uIiA6ICJSZWdpb25PbmUiLAogICAgICAgICJpbnRlcmZhY2UiIDogImFkbWluIiwKICAgICAgICAic2VydmljZV9pZCIgOiAiM2Y4MTNiOTk0MzFlNDZjMGFmODc3MzkzZjZhZDkxZDciCiAgICAgIH0gXSwKICAgICAgInR5cGUiIDogImlkZW50aXR5IiwKICAgICAgIm5hbWUiIDogImtleXN0b25lIiwKICAgICAgImVuYWJsZWQiIDogdHJ1ZQogICAgfSBdLAogICAgImV4dHJhcyIgOiB7IH0sCiAgICAibWV0aG9kcyIgOiBbICJwYXNzd29yZCIgXSwKICAgICJwcm9qZWN0IiA6IHsKICAgICAgImlkIiA6ICI4OGU1NTBhMTM1YmI0ZTZkYTY4ZTc5ZTViN2M0YjJmMiIsCiAgICAgICJkZXNjcmlwdGlvbiIgOiAiIiwKICAgICAgImxpbmtzIiA6IHsKICAgICAgICAic2VsZiIgOiBudWxsCiAgICAgIH0sCiAgICAgICJuYW1lIiA6ICJhZG1pbiIsCiAgICAgICJlbmFibGVkIiA6IHRydWUsCiAgICAgICJkb21haW5faWQiIDogImRlZmF1bHQiCiAgICB9LAogICAgInJvbGVzIiA6IFsgewogICAgICAiaWQiIDogImQ5MDM5MzZlN2JiZDQxODNiOGNkMzU4MTZkMmNmODhiIiwKICAgICAgImRlc2NyaXB0aW9uIiA6ICIiLAogICAgICAibGlua3MiIDogewogICAgICAgICJzZWxmIiA6IG51bGwKICAgICAgfSwKICAgICAgIm5hbWUiIDogImFkbWluIgogICAgfSwgewogICAgICAiaWQiIDogIjlmZTJmZjllZTQzODRiMTg5NGE5MDg3OGQzZTkyYmFiIiwKICAgICAgImRlc2NyaXB0aW9uIiA6ICJEZWZhdWx0IHJvbGUgZm9yIHByb2plY3QgbWVtYmVyc2hpcCIsCiAgICAgICJsaW5rcyIgOiB7CiAgICAgICAgInNlbGYiIDogbnVsbAogICAgICB9LAogICAgICAibmFtZSIgOiAiX21lbWJlcl8iCiAgICB9IF0sCiAgICAidXNlciIgOiB7CiAgICAgICJpZCIgOiAiZTc5MTJjMjIyNWU4NGFjNTkwNWQ4Y2YwYjUwNDBhNmQiLAogICAgICAibGlua3MiIDogewogICAgICAgICJzZWxmIiA6IG51bGwKICAgICAgfSwKICAgICAgImRvbWFpbiIgOiB7CiAgICAgICAgImlkIiA6ICJkZWZhdWx0IiwKICAgICAgICAiZGVzY3JpcHRpb24iIDogIk93bnMgdXNlcnMgYW5kIHRlbmFudHMgKGkuZS4gcHJvamVjdHMpIGF2YWlsYWJsZSBvbiBJZGVudGl0eSBBUEkgdjIuIiwKICAgICAgICAibGlua3MiIDogewogICAgICAgICAgInNlbGYiIDogbnVsbAogICAgICAgIH0sCiAgICAgICAgIm5hbWUiIDogIkRlZmF1bHQiLAogICAgICAgICJlbmFibGVkIiA6IHRydWUKICAgICAgfSwKICAgICAgIm5hbWUiIDogImFkbWluIiwKICAgICAgImVuYWJsZWQiIDogdHJ1ZSwKICAgICAgImRvbWFpbl9pZCIgOiAiZGVmYXVsdCIKICAgIH0sCiAgICAiYXVkaXRJZHMiIDogWyAiTXpJd05XRmhPVE10WldVd1lTMDBZelU1TFRreE5qTXROR0U1WWpZMk5qUXdORCIgXSwKICAgICJleHBpcmVzX2F0IiA6IDE0NzQ2NDU1NzIzMjAsCiAgICAiaXNzdWVkX2F0IiA6IDE0NzQ1NTkxNzIzMjAKICB9Cn2gggNqMIIDZjCCAk6gAwIBAgIBATANBgkqhkiG9w0BAQUFADBXMQswCQYDVQQGEwJVUzEOMAwGA1UECAwFVW5zZXQxDjAMBgNVBAcMBVVuc2V0MQ4wDAYDVQQKDAVVbnNldDEYMBYGA1UEAwwPd3d3LmV4YW1wbGUuY29tMB4XDTE0MDUzMDAyMTEwMFoXDTI0MDUyNzAyMTEwMFowRzELMAkGA1UEBhMCVVMxDjAMBgNVBAgMBVVuc2V0MQ4wDAYDVQQKDAVVbnNldDEYMBYGA1UEAwwPd3d3LmV4YW1wbGUuY29tMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxXZmeMG3z9KENHFOeEnGGmEJXo9HPgNRaizizyH4FIIZ-GxX3AmhD1orTSqSlw9lpwVG5_elzciW9qI0YU0iVa3U3uXqknOOd0F5kgnp7dt72uW1cuF6_ilnskEiUsL_llEJjlBU3GI59erjftzVYKXWjgSGVK2Muu6aQ7yd5NAkqbml1mk3TIATGKYoEmToqNGeDkN1TZchCn6UDdqt9PfcjZ9e6CHUAj9nsYqdkCr1HnOMJ5YZ85scFIX7TBLqoLArrnJib_ecF7FjaOU9IKUoIhz5ZlHE4u-AzfJmJiO-uZ72BuUUK9w_oC46s5hQ9O3fjx2qP7AZsdWxohedcQIDAQABo00wSzAJBgNVHRMEAjAAMB0GA1UdDgQWBBTsOyXDHjy93twiKto3EuJNghqbejAfBgNVHSMEGDAWgBRapzXpipYC9VGh6s3ofZCbQ0ZVrTANBgkqhkiG9w0BAQUFAAOCAQEASsHg_z_-WFs-emzSnvGO7ThcCEwUW2P2-oAdnMaajD3mqmN1BE5xhwF8ueEO-tnanLyc7Y8D-QvC7gDHeH_pzaXHRicQVG-RLu_la0RPObuNW24GyIXDZmfIEomewRVnTkXSAPWGRSmjyc-LEdhNSgP5Vj6uDrP-ZXBoxYLsdyHX3430a48FHVxQFiRc8Vi69JrUjjaW2n44LnbVIx_5iOfcT0u56bYKzuLzMdIXbRDgUTE4QI0YxkN679no_xqn1mLfTmTlTz_HMYqz8PyT96CRXVCmlg3VGQ2IvDP47t3A_WlRGVvZLo37TxU444j90K8Q3D82n0jO3YKNCIXbCDGCAg4wggIKAgEBMFwwVzELMAkGA1UEBhMCVVMxDjAMBgNVBAgMBVVuc2V0MQ4wDAYDVQQHDAVVbnNldDEOMAwGA1UECgwFVW5zZXQxGDAWBgNVBAMMD3d3dy5leGFtcGxlLmNvbQIBATAJBgUrDgMCGgUAoIGIMBgGCSqGSIb3DQEJAzELBgkqhkiG9w0BBwEwHAYJKoZIhvcNAQkFMQ8XDTE2MDkyMjE1NDYxMlowIwYJKoZIhvcNAQkEMRYEFM2ky8Hi-HrGQGo8_E6El82_SotZMCkGCSqGSIb3DQEJNDEcMBowCQYFKw4DAhoFAKENBgkqhkiG9w0BAQEFADANBgkqhkiG9w0BAQEFAASCAQBKtuz54AfOXIoEhjOayV8ddFNOFsZTrmePA30XZhW5GwTIYBeorFE4SUcu4Jv4iZ2yVhDPSqH3ricIRGBCeFDweo0LxpQRHjfTh3ig9FZ_J77mqeaZ42MXpLEJA4q_0K8zZy1js_yONltqq01NrBa3v5o7X712-QSvmXNZ97TXBug_znGGxzR9B1TihV6mEhANiDln9-dOgq3m3vMuv9RzEqidrONMwv_0AYEhLcoARePB_4zwJ7wUrwZhFNZAkdVp5-TEVoYG_UBAIlN59Sd3XGroE9a4fdH-9kzsuW1r-vRwnX9M3_hVGS83ZUlhSDsmo0MUKeRVWOyusKMMhdLt";
		logger.debug("text size:{}", t.length());
		String output = Cms.signToken(t, certFile, keyFile);
		logger.debug("byte size: {}, sign: {}", new Object[] { output.getBytes().length, output });

		String input = Cms.verifySignature(output.getBytes(), certFile, caFile);
		logger.debug("decrypt: {}", input);
		assertEquals(t, input);

		Cms.cmsVerify(output, certFile, caFile, Cms.PKI_ASN1_FORM);
	}

	@Test
	public void testSignAndHashToken() throws Exception {
		String output = Cms.signToken("12345", certFile, keyFile);
		System.err.println(output);
		assertTrue(output.startsWith("MII"));
		String hashed = Cms.hashToken(output, null);
		logger.debug("hash: {}", hashed);
	}

	@Test
	public void testHashToken() throws Exception {
		String output = "MIIGDgYJKoZIhvcNAQcCoIIF-zCCBfsCAQExCzAJBgUrDgMCGgUAMIGUBgkqhkiG9w0BBwGggYYEgYN7ImF1dGgiOnsiaWRlbnRpdHkiOnsibWV0aG9kcyI6WyJwYXNzd29yZCJdLCJwYXNzd29yZCI6eyJ1c2VyIjp7ImlkIjoiMGYzMzI4ZjgtYTdlNy00MWI0LTgzMGQtYmU4ZmRkNTE4NmM3IiwicGFzc3dvcmQiOiJhZG1pbiJ9fX19faCCA2owggNmMIICTqADAgECAgEBMA0GCSqGSIb3DQEBBQUAMFcxCzAJBgNVBAYTAlVTMQ4wDAYDVQQIDAVVbnNldDEOMAwGA1UEBwwFVW5zZXQxDjAMBgNVBAoMBVVuc2V0MRgwFgYDVQQDDA93d3cuZXhhbXBsZS5jb20wHhcNMTQwNTMwMDIxMTAwWhcNMjQwNTI3MDIxMTAwWjBHMQswCQYDVQQGEwJVUzEOMAwGA1UECAwFVW5zZXQxDjAMBgNVBAoMBVVuc2V0MRgwFgYDVQQDDA93d3cuZXhhbXBsZS5jb20wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDFdmZ4wbfP0oQ0cU54ScYaYQlej0c+A1FqLOLPIfgUghn4bFfcCaEPWitNKpKXD2WnBUbn96XNyJb2ojRhTSJVrdTe5eqSc453QXmSCent23va5bVy4Xr+KWeyQSJSwv+WUQmOUFTcYjn16uN+3NVgpdaOBIZUrYy67ppDvJ3k0CSpuaXWaTdMgBMYpigSZOio0Z4OQ3VNlyEKfpQN2q3099yNn17oIdQCP2exip2QKvUec4wnlhnzmxwUhftMEuqgsCuucmJv95wXsWNo5T0gpSgiHPlmUcTi74DN8mYmI765nvYG5RQr3D+gLjqzmFD07d+PHao-sBmx1bGiF51xAgMBAAGjTTBLMAkGA1UdEwQCMAAwHQYDVR0OBBYEFOw7JcMePL3e3CIq2jcS4k2CGpt6MB8GA1UdIwQYMBaAFFqnNemKlgL1UaHqzeh9kJtDRlWtMA0GCSqGSIb3DQEBBQUAA4IBAQBKweD-P-5YWz56bNKe8Y7tOFwITBRbY-b6gB2cxpqMPeaqY3UETnGHAXy54Q762dqcvJztjwP5C8LuAMd4f+nNpcdGJxBUb5Eu7+VrRE85u41bbgbIhcNmZ8gSiZ7BFWdORdIA9YZFKaPJz4sR2E1KA-lWPq4Os-5lcGjFgux3IdffjfRrjwUdXFAWJFzxWLr0mtSONpbafjgudtUjH-mI59xPS7nptgrO4vMx0hdtEOBRMThAjRjGQ3rv2ej-GqfWYt9OZOVPP8cxirPw-JP3oJFdUKaWDdUZDYi8M-ju3cD9aVEZW9kujftPFTjjiP3QrxDcPzafSM7dgo0IhdsIMYIB4jCCAd4CAQEwXDBXMQswCQYDVQQGEwJVUzEOMAwGA1UECAwFVW5zZXQxDjAMBgNVBAcMBVVuc2V0MQ4wDAYDVQQKDAVVbnNldDEYMBYGA1UEAwwPd3d3LmV4YW1wbGUuY29tAgEBMAkGBSsOAwIaBQCgXTAYBgkqhkiG9w0BCQMxCwYJKoZIhvcNAQcBMBwGCSqGSIb3DQEJBTEPFw0xNTA1MDcwNzIxMzBaMCMGCSqGSIb3DQEJBDEWBBRwGuUlZ9O0cRA6HtmYxpgStsh9dTANBgkqhkiG9w0BAQEFAASCAQAf3WIopRYN1DXHNWyKGixVB-OotZVWcMKhHYxQF21eKA2A48-MU7-uwDUkLdCUfSiysxsWfkPvB10K8V2ZfvHcGatCV615iJvOG7GSsPGRSy3p3sKaB26c1eRTRDDVgjvCCpY7gIK2xZ9S5EC4ms9VAtkVFxASRvdzumaN4u5M1nv3szAM1sEF1-1dbi94Z6riNY-RpvTOv6mvTkdsMbhOnO3Vyz2TdlrJx5toD7aNUrFFpEMQkqJ4WQTMNaQCTLLjlCgH5q5koKbQM1rJUV2a52ZKGiCpOyTTnEQuGB5si3h3YXSsn5FBck-bPkGBWS2kDjeK5-ktLjcmmFkt6tLl";
		String hashed = Cms.hashToken(output, null);
		assertEquals("088E315EFE1519CB9BA31F1A1440B888", hashed);
		logger.debug("hash test: {}", hashed);
		// logger.debug(Cms.hashToken("708bb4f9-9d3c-46af-b18c-7033dc012f11"));
	}

}
