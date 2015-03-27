package com.infinities.keystone4j.identity;

import com.infinities.keystone4j.model.identity.mapping.IdMapping;

public interface IdGenerator {

	String generatePublicId(IdMapping localEntity) throws Exception;

}