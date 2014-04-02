package com.infinities.keystone4j;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.utils.Cms;

public class CmsTest {

	private final String text = "{" + "\"auth\":{" + "\"identity\":{" + "\"methods\":[" + "\"password\"" + "],"
			+ "\"password\":{" + "\"user\":{" + "\"id\":\"0f3328f8-a7e7-41b4-830d-be8fdd5186c7\","
			+ "\"password\":\"admin\"" + "}" + "}" + "}" + "}" + "}";
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
		logger.debug("sign: {}", output);
	}

	@Test
	public void testSignAndHashToken() throws Exception {
		String output = Cms.Instance.signToken(text);
		String hashed = Cms.Instance.hashToken(output);
		logger.debug("hash: {}", hashed);
	}

	@Test
	public void testHashToken() throws Exception {
		String output = "7a816864a3c61a0d58faeed765d549a7910970d7ef3469862d2275535d916b5ff73da44e50e0a4262ffb10e92e846fc8af5fab61e536e8766bc3a3e0ecef1648ef1816008607257cd54c05273561c3831f591c3f234904e394592a9dc84a6790bfc903a6718b6a36ae19571f1386b3372dcc4a1473ebe89ec094b576ba157c6bd79a9f4f7b354de9f1c4dcd69ebb6f94a7bcb3a1e37f56f70cd046dc506fc73899c39ee221e6977878c6bb11fd491404ff68421a5ca8fbecd10d1dd65bdd51a6d21a895b7b79de229397420932a9902b99547674f6bde6e5334795dade1d68e0a9339c57bf6823b25f1aaf9dd1cc890b5cf26b4749497cf2806004b508919980";
		String hashed = Cms.Instance.hashToken(output);
		assertEquals("136f8b5f87dbc542b1921b9e1b1fdc91", hashed);
		logger.debug("hash test: {}", hashed);
		// logger.debug(Cms.Instance.hashToken("708bb4f9-9d3c-46af-b18c-7033dc012f11"));
	}

}
