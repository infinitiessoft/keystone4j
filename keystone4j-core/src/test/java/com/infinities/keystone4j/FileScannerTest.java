package com.infinities.keystone4j;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Table;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.common.Config.Type;

public class FileScannerTest {

	private FileScanner scanner;


	@Before
	public void setUp() throws Exception {
		URL url = FileScannerTest.class.getResource("/keystone.conf");
		scanner = new FileScanner(url);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRead() throws IOException {
		Table<Type, String, String> table = scanner.read();
		assertEquals("ADMIN2", table.get(Config.Type.DEFAULT, "admin_token"));
	}

}
