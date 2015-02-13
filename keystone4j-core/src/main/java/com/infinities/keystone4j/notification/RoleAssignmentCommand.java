package com.infinities.keystone4j.notification;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.NonTruncatedCommand;

public interface RoleAssignmentCommand<T> extends NonTruncatedCommand<T> {

	public boolean getInheritedToProjects();

	public KeystoneContext getContext();

	public String getProjectId();

	public String getDomainId();

	public String getUserId();

	public String getGroupId();

}
