package com.infinities.keystone4j.identity.id_generators;

import java.security.MessageDigest;

import com.google.common.base.Strings;
import com.infinities.keystone4j.identity.IdGenerator;
import com.infinities.keystone4j.model.identity.mapping.IdMapping;

public class Sha256IdGenerator implements IdGenerator {

	@Override
	public String generatePublicId(IdMapping localEntity) throws Exception {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		if (!Strings.isNullOrEmpty(localEntity.getDomainId())) {
			md.update(localEntity.getDomainId().getBytes());
		}
		if (!Strings.isNullOrEmpty(localEntity.getLocalId())) {
			md.update(localEntity.getLocalId().getBytes());
		}
		if (!Strings.isNullOrEmpty(localEntity.getPublicId())) {
			md.update(localEntity.getPublicId().getBytes());
		}
		if (localEntity.getEntityType() != null) {
			md.update(localEntity.getEntityType().name().getBytes());
		}
		byte byteData[] = md.digest();
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			String hex = Integer.toHexString(0xff & byteData[i]);
			if (hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}
		// return new String(Hex.encode(byteData));
		return hexString.toString();
	}

}
