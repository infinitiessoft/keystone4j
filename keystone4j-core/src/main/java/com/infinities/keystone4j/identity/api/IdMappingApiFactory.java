package com.infinities.keystone4j.identity.api;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.identity.IdMappingApi;
import com.infinities.keystone4j.identity.MappingDriver;

public class IdMappingApiFactory implements Factory<IdMappingApi> {

	private final MappingDriver driver;


	@Inject
	public IdMappingApiFactory(MappingDriver driver) {
		this.driver = driver;
	}

	@Override
	public void dispose(IdMappingApi arg0) {

	}

	@Override
	public IdMappingApi provide() {
		IdMappingApi idMappingApi = new IdMappingApiImpl(driver);
		return idMappingApi;
	}

}
