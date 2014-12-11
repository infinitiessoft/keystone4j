package com.infinities.keystone4j.identity.controller;

import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.identity.Group;

public interface GroupV3Controller {

	MemberWrapper<Group> createGroup(Group group) throws Exception;

	CollectionWrapper<Group> listGroups() throws Exception;

	CollectionWrapper<Group> listGroupsForUser(String userid) throws Exception;

	MemberWrapper<Group> getGroup(String groupid) throws Exception;

	MemberWrapper<Group> updateGroup(String groupid, Group group) throws Exception;

	void deleteGroup(String groupid) throws Exception;

}
