package com.infinities.keystone4j.identity.api;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.identity.IdGenerator;
import com.infinities.keystone4j.identity.IdGeneratorApi;

public class IdGeneratorApiFactory implements Factory<IdGeneratorApi> {

	private final IdGenerator idGenerator;


	@Inject
	public IdGeneratorApiFactory(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	@Override
	public void dispose(IdGeneratorApi arg0) {

	}

	@Override
	public IdGeneratorApi provide() {
		IdGeneratorApi idGeneratorApi = new IdGeneratorApiImpl(idGenerator);
		return idGeneratorApi;
	}

}
