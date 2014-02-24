package com.infinities.keystone4j.catalog.driver;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.common.Config;

public class CatalogDriverFactory implements Factory<CatalogDriver> {

	public CatalogDriverFactory() {
	}

	@Override
	public void dispose(CatalogDriver arg0) {

	}

	@Override
	public CatalogDriver provide() {
		String driver = Config.Instance.getOpt(Config.Type.catalog, "driver").getText();
		try {
			Class<?> c = Class.forName(driver);
			return (CatalogDriver) c.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
