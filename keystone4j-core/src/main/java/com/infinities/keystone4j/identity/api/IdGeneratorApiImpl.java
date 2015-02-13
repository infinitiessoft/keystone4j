package com.infinities.keystone4j.identity.api;

import com.infinities.keystone4j.identity.IdGenerator;
import com.infinities.keystone4j.identity.IdGeneratorApi;
import com.infinities.keystone4j.model.identity.mapping.IdMapping;

public class IdGeneratorApiImpl implements IdGeneratorApi {

	private final IdGenerator idGenerator;


	public IdGeneratorApiImpl(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	@Override
	public String generatePublicId(IdMapping localEntity) throws Exception {
		return idGenerator.generatePublicId(localEntity);
	}
}
