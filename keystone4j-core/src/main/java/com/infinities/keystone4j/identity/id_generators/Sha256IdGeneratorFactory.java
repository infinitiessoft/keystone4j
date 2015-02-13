package com.infinities.keystone4j.identity.id_generators;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.identity.IdGenerator;

public class Sha256IdGeneratorFactory implements Factory<IdGenerator> {

	@Inject
	public Sha256IdGeneratorFactory() {

	}

	@Override
	public void dispose(IdGenerator arg0) {

	}

	@Override
	public IdGenerator provide() {
		IdGenerator idGenerator = new Sha256IdGenerator();
		return idGenerator;
	}

}
