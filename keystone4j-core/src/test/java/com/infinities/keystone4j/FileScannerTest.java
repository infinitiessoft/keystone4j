package com.infinities.keystone4j;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Table;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.common.Config.Type;
import com.infinities.keystone4j.utils.FileScanner;

public class FileScannerTest {

	private FileScanner scanner;


	@Before
	public void setUp() throws Exception {
		// URL url = getClass().getResource(KeystoneApplication.CONF_DIR +
		// "keystone.conf");
		File file = new File(KeystoneApplication.CONF_DIR + "keystone.conf");
		// URL url = FileScannerTest.class.getResource("/keystone.conf");
		scanner = new FileScanner(file.toURI().toURL());
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
