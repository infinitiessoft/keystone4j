package com.infinities.keystone4j.common;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.infinities.keystone4j.option.Option;

public class ConfigTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetOpt() {
		Option adminToken = Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token");
		assertEquals("ADMIN2", adminToken.getText());
	}

}
