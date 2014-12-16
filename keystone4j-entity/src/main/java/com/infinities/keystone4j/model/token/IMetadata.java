package com.infinities.keystone4j.model.token;

import java.util.List;

public interface IMetadata {

	void setUserId(String userId);

	void setTenantId(String projectId);

	void setRoles(List<String> roleNames);

}
