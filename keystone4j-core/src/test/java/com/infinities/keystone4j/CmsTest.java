package com.infinities.keystone4j;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.utils.Cms;

public class CmsTest {

	private final String text = "test_string";
	private final static Logger logger = LoggerFactory.getLogger(CmsTest.class);


	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSignToken() throws Exception {
		String output = Cms.Instance.signToken(text);
		logger.debug(output);
	}

}
