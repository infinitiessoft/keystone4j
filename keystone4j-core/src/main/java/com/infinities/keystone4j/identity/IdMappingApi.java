package com.infinities.keystone4j.identity;

import com.infinities.keystone4j.model.identity.mapping.IdMapping;

public interface IdMappingApi {

	String createIdMapping(IdMapping localEntity, String publicId) throws Exception;

	String getPublicId(IdMapping localEntity) throws Exception;

	IdMapping getIdMapping(String publicId) throws Exception;

	void deleteIdMapping(String userid) throws Exception;
}
