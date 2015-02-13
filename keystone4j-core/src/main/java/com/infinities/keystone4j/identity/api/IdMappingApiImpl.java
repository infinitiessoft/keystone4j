package com.infinities.keystone4j.identity.api;

import com.infinities.keystone4j.identity.IdMappingApi;
import com.infinities.keystone4j.identity.MappingDriver;
import com.infinities.keystone4j.model.identity.mapping.IdMapping;

public class IdMappingApiImpl implements IdMappingApi {

	private final MappingDriver driver;


	public IdMappingApiImpl(MappingDriver driver) {
		this.driver = driver;
	}

	@Override
	public String createIdMapping(IdMapping localEntity, String publicId) throws Exception {
		return driver.createIdMapping(localEntity, publicId);
	}

	@Override
	public String getPublicId(IdMapping localEntity) throws Exception {
		return driver.getPublicId(localEntity);
	}

	@Override
	public IdMapping getIdMapping(String publicId) throws Exception {
		return driver.getIdMapping(publicId);
	}

	@Override
	public void deleteIdMapping(String userid) throws Exception {
		driver.deleteIdMapping(userid);
	}

}
