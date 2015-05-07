/*******************************************************************************
 * # Copyright 2015 InfinitiesSoft Solutions Inc.
 * #
 * # Licensed under the Apache License, Version 2.0 (the "License"); you may
 * # not use this file except in compliance with the License. You may obtain
 * # a copy of the License at
 * #
 * #      http://www.apache.org/licenses/LICENSE-2.0
 * #
 * # Unless required by applicable law or agreed to in writing, software
 * # distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * # WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * # License for the specific language governing permissions and limitations
 * # under the License.
 *******************************************************************************/
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
