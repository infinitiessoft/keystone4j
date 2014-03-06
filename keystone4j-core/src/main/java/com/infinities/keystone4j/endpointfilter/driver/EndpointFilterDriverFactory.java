package com.infinities.keystone4j.endpointfilter.driver;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.endpointfilter.EndpointFilterDriver;

public class EndpointFilterDriverFactory implements Factory<EndpointFilterDriver> {

	public EndpointFilterDriverFactory() {
	}

	@Override
	public void dispose(EndpointFilterDriver arg0) {

	}

	@Override
	public EndpointFilterDriver provide() {
		String driver = Config.Instance.getOpt(Config.Type.endpoint_filter, "driver").asText();
		try {
			Class<?> c = Class.forName(driver);
			return (EndpointFilterDriver) c.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
