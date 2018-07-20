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

package net.oauth.signature.pem;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class PEMReaderTest {

	private PEMReader reader;


	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPEMReaderString() throws IOException, URISyntaxException {
		URL url = Thread.currentThread().getContextClassLoader().getResource("cacert.pem");
		reader = new PEMReader(url.getPath());
		String readerText = reader.getText();

		String caPem = Files.asCharSource(new File(url.getPath()), Charsets.UTF_8).read();
		assertEquals(caPem, readerText);
	}
}
