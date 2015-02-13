package com.infinities.keystone4j.identity.driver.mapping;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.identity.IdGeneratorApi;
import com.infinities.keystone4j.identity.MappingDriver;

public class IdMappingDriverFactory implements Factory<MappingDriver> {

	private final IdGeneratorApi idGeneratorApi;


	@Inject
	public IdMappingDriverFactory(IdGeneratorApi idGeneratorApi) {
		this.idGeneratorApi = idGeneratorApi;
	}

	@Override
	public void dispose(MappingDriver arg0) {

	}

	@Override
	public MappingDriver provide() {
		MappingDriver driver = new IdMappingJpaDriver(idGeneratorApi);
		return driver;
	}
}
