package com.infinities.keystone4j.identity.controller;

import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.model.identity.GroupWrapper;
import com.infinities.keystone4j.model.identity.GroupsWrapper;

public interface GroupV3Controller {

	GroupWrapper createGroup(Group group);

	GroupsWrapper listGroups(String domainid, String name, int page, int perPage);

	GroupsWrapper listGroupsForUser(String userid, String name, int page, int perPage);

	GroupWrapper getGroup(String groupid);

	GroupWrapper updateGroup(String groupid, Group group);

	void deleteGroup(String groupid);

}
