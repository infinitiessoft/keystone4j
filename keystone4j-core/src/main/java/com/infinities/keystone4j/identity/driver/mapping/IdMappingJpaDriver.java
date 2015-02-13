package com.infinities.keystone4j.identity.driver.mapping;

import javax.persistence.NoResultException;

import com.google.common.base.Strings;
import com.infinities.keystone4j.identity.IdGeneratorApi;
import com.infinities.keystone4j.identity.MappingDriver;
import com.infinities.keystone4j.jpa.impl.IdMappingDao;
import com.infinities.keystone4j.model.identity.mapping.IdMapping;

public class IdMappingJpaDriver implements MappingDriver {

	private IdGeneratorApi idGeneratorApi;
	private final IdMappingDao dao = new IdMappingDao();


	public IdMappingJpaDriver(IdGeneratorApi idGeneratorApi) {
		this.idGeneratorApi = idGeneratorApi;
	}

	@Override
	public String getPublicId(IdMapping localEntity) throws Exception {
		try {
			IdMapping ref = dao.find(localEntity.getDomainId(), localEntity.getLocalId(), localEntity.getEntityType());
			return ref.getPublicId();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public IdMapping getIdMapping(String publicId) throws Exception {
		IdMapping ref = dao.findById(publicId);
		return ref;
	}

	@Override
	public String createIdMapping(IdMapping localEntity, String publicId) throws Exception {
		if (Strings.isNullOrEmpty(publicId)) {
			publicId = this.idGeneratorApi.generatePublicId(localEntity);
		}
		localEntity.setPublicId(publicId);
		dao.persist(localEntity);
		return publicId;
	}

	@Override
	public void deleteIdMapping(String publicId) throws Exception {
		try {
			IdMapping ref = dao.findById(publicId);
			if (ref != null) {
				dao.remove(ref);
			}
		} catch (NoResultException e) {
			// ignore
		}
	}

	@Override
	public void setIdGeneratorApi(IdGeneratorApi idGeneratorApi) {
		this.idGeneratorApi = idGeneratorApi;
	}

}
