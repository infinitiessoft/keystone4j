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
package com.infinities.keystone4j.assignment.driver;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.infinities.keystone4j.ListFunction;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.driver.function.ListDomainsFunction;
import com.infinities.keystone4j.assignment.driver.function.ListProjectsFunction;
import com.infinities.keystone4j.assignment.driver.function.ListRolesFunction;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.common.Config.Type;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.common.TruncatedFunction;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.jpa.impl.DomainDao;
import com.infinities.keystone4j.jpa.impl.ProjectDao;
import com.infinities.keystone4j.jpa.impl.RoleAssignmentDao;
import com.infinities.keystone4j.jpa.impl.RoleDao;
import com.infinities.keystone4j.model.assignment.Assignment;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.Metadata;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.assignment.RoleAssignment;
import com.infinities.keystone4j.model.assignment.RoleAssignment.AssignmentType;

public class AssignmentJpaDriver implements AssignmentDriver {

	private final Logger logger = LoggerFactory.getLogger(AssignmentJpaDriver.class);
	private final ProjectDao projectDao;
	private final RoleDao roleDao;
	private final DomainDao domainDao;
	private final RoleAssignmentDao roleAssignmentDao;
	// private final AssignmentDao assignmentDao;
	// private final UserProjectGrantDao userProjectGrantDao;
	// private final UserDomainGrantDao userDomainGrantDao;
	// private final GroupProjectGrantDao groupProjectGrantDao;
	// private final GroupDomainGrantDao groupDomainGrantDao;
	private final static String ROLE_GRANT = "role grant";
	private final static String USER_ALREADY_HAS_ROLE = "User {0} already has role {1} in tenant {2}";
	private final static String CANNOT_REMOVE_ROLE = "Cannot remove role that has not been granted, {0}";


	public AssignmentJpaDriver() {
		super();
		this.projectDao = new ProjectDao();
		this.roleDao = new RoleDao();
		this.domainDao = new DomainDao();
		this.roleAssignmentDao = new RoleAssignmentDao();
		// this.userProjectGrantDao = new UserProjectGrantDao();
		// this.userDomainGrantDao = new UserDomainGrantDao();
		// this.groupProjectGrantDao = new GroupProjectGrantDao();
		// this.groupDomainGrantDao = new GroupDomainGrantDao();
	}

	@Override
	public Project getProject(String projectid) {
		Project project = projectDao.findById(projectid);
		if (project == null) {
			throw Exceptions.ProjectNotFoundException.getInstance(null, projectid);
		}
		return project;
	}

	@Override
	public Project getProjectByName(String projectName, String domainid) {
		try {
			Project project = projectDao.findByName(projectName, domainid);
			return project;
		} catch (NoResultException e) {
			throw Exceptions.ProjectNotFoundException.getInstance(null, projectName);
		}
	}

	@Override
	public List<String> listUserIdsForProject(String projectid) {
		this.getProject(projectid);
		return roleAssignmentDao.listUserIdsForProject(projectid);
	}

	@Override
	public void createGrant(String roleid, String userid, String groupid, String domainid, String projectid,
			boolean inheritedToProjects) {
		getRole(roleid);
		if (!Strings.isNullOrEmpty(domainid)) {
			getDomain(domainid);
		}
		if (!Strings.isNullOrEmpty(projectid)) {
			getProject(projectid);
		}

		AssignmentType type = calculateType(userid, groupid, projectid, domainid);
		try {
			RoleAssignment roleAssignment = new RoleAssignment();
			roleAssignment.setType(type);
			String actorId = Strings.isNullOrEmpty(userid) ? groupid : userid;
			roleAssignment.setActorId(actorId);
			String targetId = Strings.isNullOrEmpty(projectid) ? domainid : projectid;
			roleAssignment.setTargetId(targetId);
			roleAssignment.setRoleId(roleid);
			roleAssignment.setInherited(inheritedToProjects);
			roleAssignment.setId(UUID.randomUUID().toString());
			roleAssignmentDao.persist(roleAssignment);
		} catch (EntityExistsException e) {
			logger.warn("role assignment exist", e);
		}
	}

	private AssignmentType calculateType(String userid, String groupid, String projectid, String domainid) {
		if (!Strings.isNullOrEmpty(userid) && !Strings.isNullOrEmpty(projectid)) {
			return AssignmentType.USER_PROJECT;
		} else if (!Strings.isNullOrEmpty(userid) && !Strings.isNullOrEmpty(domainid)) {
			return AssignmentType.USER_DOMAIN;
		} else if (!Strings.isNullOrEmpty(groupid) && !Strings.isNullOrEmpty(projectid)) {
			return AssignmentType.GROUP_PROJECT;
		} else if (!Strings.isNullOrEmpty(groupid) && !Strings.isNullOrEmpty(domainid)) {
			return AssignmentType.GROUP_DOMAIN;
		} else {
			String messageData = Joiner.on(", ").join(userid, groupid, projectid, domainid);
			throw new RuntimeException(messageData);
		}
	}

	@Override
	public List<Role> listGrants(String userid, String groupid, String domainid, String projectid,
			boolean inheritedToProjects) {
		if (!Strings.isNullOrEmpty(domainid)) {
			getDomain(domainid);
		}
		if (!Strings.isNullOrEmpty(projectid)) {
			getProject(projectid);
		}

		String actorId = Strings.isNullOrEmpty(userid) ? groupid : userid;
		String targetId = Strings.isNullOrEmpty(projectid) ? domainid : projectid;
		List<Role> roles = roleDao.listGrants(actorId, targetId, inheritedToProjects);
		return roles;
	}

	@Override
	public Role getGrant(String roleid, String userid, String groupid, String domainid, String projectid,
			boolean inheritedToProjects) {
		Role roleRef = getRole(roleid);
		if (!Strings.isNullOrEmpty(domainid)) {
			getDomain(domainid);
		}
		if (!Strings.isNullOrEmpty(projectid)) {
			getProject(projectid);
		}
		try {
			String actorId = Strings.isNullOrEmpty(userid) ? groupid : userid;
			String targetId = Strings.isNullOrEmpty(projectid) ? domainid : projectid;
			roleAssignmentDao.getGrant(roleid, targetId, actorId, inheritedToProjects);
		} catch (NoResultException e) {
			throw Exceptions.RoleNotFoundException.getInstance(null, roleid);
		}
		return roleRef;
	}

	@Override
	public void deleteGrant(String roleid, String userid, String groupid, String domainid, String projectid,
			boolean inheritedToProjects) {
		getRole(roleid);
		if (!Strings.isNullOrEmpty(domainid)) {
			getDomain(domainid);
		}
		if (!Strings.isNullOrEmpty(projectid)) {
			getProject(projectid);
		}

		String actorId = Strings.isNullOrEmpty(userid) ? groupid : userid;
		String targetId = Strings.isNullOrEmpty(projectid) ? domainid : projectid;
		RoleAssignment roleAssignment = roleAssignmentDao.getGrant(roleid, targetId, actorId, inheritedToProjects);

		try {
			roleAssignmentDao.remove(roleAssignment);
		} catch (IllegalArgumentException e) {
			throw Exceptions.RoleNotFoundException.getInstance(null, roleid);
		}

	}

	@Override
	public List<Project> listProjects(Hints hints) throws Exception {
		ListFunction<Project> function = new TruncatedFunction<Project>(new ListProjectsFunction());
		return function.execute(hints);
	}

	@Override
	public List<Project> listProjectsInDomain(String domainid) throws Exception {
		return projectDao.listProjectsInDomain(domainid);
	}

	@Override
	public List<Project> listProjectsForUser(String userid, List<String> groupids, Hints hints) {
		List<String> actorList = new ArrayList<String>();
		actorList.add(userid);
		if (groupids != null) {
			actorList.addAll(groupids);
		}

		List<RoleAssignment> assignments = roleAssignmentDao.listRoleAssignmentsForActors(actorList);
		Set<String> projectIds = new HashSet<String>();
		for (RoleAssignment assignment : assignments) {
			if (assignment.getType() == AssignmentType.USER_PROJECT || assignment.getType() == AssignmentType.GROUP_PROJECT
					&& !assignment.getInherited()) {
				projectIds.add(assignment.getTargetId());
			}
		}

		if (!Config.getOpt(Type.os_inherit, "enabled").asBoolean()) {
			return projectIdsToDicts(projectIds);
		}

		Set<String> domainIds = new HashSet<String>();
		for (RoleAssignment assignment : assignments) {
			if (assignment.getType() == AssignmentType.USER_DOMAIN || assignment.getType() == AssignmentType.GROUP_DOMAIN
					&& !assignment.getInherited()) {
				domainIds.add(assignment.getTargetId());
			}
		}

		if (!domainIds.isEmpty()) {
			List<Project> projects = projectDao.listProjectsInDomains(domainIds);
			for (Project projectRef : projects) {
				projectIds.add(projectRef.getId());
			}
		}
		return projectIdsToDicts(projectIds);
	}

	@Override
	public List<Project> listProjectsForGroups(List<String> groupids) {
		List<RoleAssignment> assignments = roleAssignmentDao.listRoleAssignmentsForActors(groupids);
		Set<String> projectIds = new HashSet<String>();
		for (RoleAssignment assignment : assignments) {
			if (assignment.getType() == AssignmentType.GROUP_PROJECT) {
				projectIds.add(assignment.getTargetId());
			}
		}

		if (!Config.getOpt(Type.os_inherit, "enabled").asBoolean()) {
			return projectIdsToDicts(projectIds);
		}

		Set<String> domainIds = new HashSet<String>();
		for (RoleAssignment assignment : assignments) {
			if (assignment.getType() == AssignmentType.GROUP_DOMAIN && assignment.getInherited()) {
				domainIds.add(assignment.getTargetId());
			}
		}

		if (!domainIds.isEmpty()) {
			List<Project> projects = projectDao.listProjectsInDomains(domainIds);
			for (Project projectRef : projects) {
				projectIds.add(projectRef.getId());
			}
		}
		return projectIdsToDicts(projectIds);
	}

	private List<Project> projectIdsToDicts(Set<String> ids) {
		if (ids.isEmpty()) {
			return new ArrayList<Project>();
		} else {
			return projectDao.listProjectsInProjectIds(ids);
		}
	}

	@Override
	public List<Project> listProjectsInSubtree(String projectid) throws Exception {
		Project project = getProject(projectid);
		List<String> projectIds = new ArrayList<String>();
		projectIds.add(project.getId());
		List<Project> children = getChildren(projectIds);

		List<Project> subtree = new ArrayList<Project>();
		Set<String> examined = new HashSet<String>();
		examined.add(project.getId());

		while (!children.isEmpty()) {
			Set<String> childrenIds = new HashSet<String>();
			for (Project ref : children) {
				if (examined.contains(ref.getId())) {
					String msg = String.format("Circular reference or a repeated entry found in projects hierarchy - %s",
							ref.getId());
					logger.error(msg);
					return new ArrayList<Project>();
				}
				childrenIds.add(ref.getId());
			}
			examined.addAll(childrenIds);
			subtree.addAll(children);
			children = getChildren(new ArrayList<String>(childrenIds));
		}

		return subtree;
	}

	private List<Project> getChildren(List<String> projectIds) {
		return projectDao.getChildren(projectIds);
	}

	@Override
	public List<Project> listProjectParents(String projectid) throws Exception {
		Project project = getProject(projectid);
		List<Project> parents = new ArrayList<Project>();
		Set<String> examined = new HashSet<String>();

		while (!Strings.isNullOrEmpty(project.getParentId())) {
			if (examined.contains(project.getId())) {
				String msg = String.format("Circular reference or a repeated entry found in projects hierarchy - %s",
						project.getId());
				logger.error(msg);
				return new ArrayList<Project>();
			}
			examined.add(project.getId());
			Project parentProject = getProject(project.getParentId());
			parents.add(parentProject);
			project = parentProject;
		}

		return parents;
	}

	public boolean isLeadProject(String projectid) {
		List<String> ids = new ArrayList<String>();
		ids.add(projectid);
		List<Project> projectRefs = getChildren(ids);
		return projectRefs.isEmpty();
	}

	// projectid=null, domainid=null
	public List<Role> getRolesForGroup(List<String> groupids, String projectid, String domainid) throws Exception {

		if (!Strings.isNullOrEmpty(projectid)) {
			return getRolesForGroupsOnProject(groupids, projectid);
		} else if (!Strings.isNullOrEmpty(domainid)) {
			return getRolesForGroupsOnDomain(groupids, domainid);
		} else {
			throw new IllegalArgumentException("Must specify either domain or project");
		}
	}

	private List<Role> getRolesForGroupsOnDomain(List<String> groupids, String domainid) {
		return roleDao.getRolesForGroupsOnDomain(groupids, domainid);
	}

	private List<Role> getRolesForGroupsOnProject(List<String> groupids, String projectid) throws Exception {
		Project project = getProject(projectid);
		List<String> roleids = getGroupProjectRoles(groupids, projectid, project.getDomainId());
		return roleIdsToDicts(roleids);
	}

	private List<Role> roleIdsToDicts(List<String> roleids) {
		if (roleids.isEmpty()) {
			return new ArrayList<Role>();
		} else {
			return roleDao.listRolesInIds(roleids);
		}
	}

	@Override
	public List<String> getGroupProjectRoles(List<String> groups, String projectid, String projectDomainId) throws Exception {
		if (groups.isEmpty()) {
			return new ArrayList<String>();
		}
		return roleAssignmentDao.getGroupProjectRoles(groups, projectid, projectDomainId, this);
	}

	@Override
	public void addRoleToUserAndProject(String userid, String projectid, String roleid) {
		RoleAssignment roleAssignment = new RoleAssignment();
		roleAssignment.setType(AssignmentType.USER_PROJECT);
		roleAssignment.setActorId(userid);
		roleAssignment.setTargetId(projectid);
		roleAssignment.setRoleId(roleid);
		roleAssignment.setInherited(false);

		try {
			roleAssignmentDao.persist(roleAssignment);
		} catch (EntityExistsException e) {
			String msg = MessageFormat.format(USER_ALREADY_HAS_ROLE, userid, roleid, projectid);
			logger.error(msg, e);
			throw Exceptions.ConflictException.getInstance(null, ROLE_GRANT, msg); // replace
			// KeyError
		}

	}

	@Override
	public void removeRoleFromUserAndProject(String userid, String projectid, String roleid) {
		try {
			RoleAssignment roleAssignment = roleAssignmentDao.getGrant(roleid, projectid, userid, false);
			roleAssignmentDao.remove(roleAssignment);
		} catch (NoResultException e) {
			String msg = MessageFormat.format(CANNOT_REMOVE_ROLE, roleid);
			throw Exceptions.RoleNotFoundException.getInstance(msg);
		}
	}

	@Override
	public List<Assignment> listRoleAssignments() {
		List<RoleAssignment> roleAssignments = roleAssignmentDao.findAll();

		List<Assignment> assignments = Lists.newArrayList();
		for (RoleAssignment roleAssignment : roleAssignments) {
			assignments.add(denormalizeRole(roleAssignment));
		}
		return assignments;
	}

	private Assignment denormalizeRole(RoleAssignment ref) {
		Assignment assignment = new Assignment();
		if (ref.getType() == AssignmentType.USER_PROJECT) {
			assignment.setUserId(ref.getActorId());
			assignment.setProjectId(ref.getTargetId());
		} else if (ref.getType() == AssignmentType.USER_DOMAIN) {
			assignment.setUserId(ref.getActorId());
			assignment.setDomainId(ref.getTargetId());
		} else if (ref.getType() == AssignmentType.GROUP_PROJECT) {
			assignment.setGroupId(ref.getActorId());
			assignment.setProjectId(ref.getTargetId());
		} else if (ref.getType() == AssignmentType.GROUP_DOMAIN) {
			assignment.setGroupId(ref.getActorId());
			assignment.setDomainId(ref.getTargetId());
		} else {
			String msg = String.format("Unexpected assignment type encountered, %s", ref.getType());
			throw new RuntimeException(msg);
		}

		assignment.setRoleId(ref.getRoleId());
		if (ref.getInherited()) {
			assignment.setInheritedToProjects("projects");
		}

		return assignment;
	}

	// TODO ignore @sql.handle_conflicts(conflict_type='project')
	@Override
	public Project createProject(String projectid, Project project) {
		projectDao.persist(project);
		return project;
	}

	// TODO ignore @sql.handle_conflicts(conflict_type='project')
	@Override
	public Project updateProject(String projectid, Project project) {
		Project oldProject = getProject(projectid);
		if (project.isDescriptionUpdated()) {
			oldProject.setDescription(project.getDescription());
		}
		if (project.isDomainUpdated()) {
			if (!Strings.isNullOrEmpty(project.getDomain().getId())) {
				oldProject.setDomain(project.getDomain());
			}
		}
		if (project.isEnabledUpdate()) {
			oldProject.setEnabled(project.getEnabled());
		}
		if (project.isExtraUpdated()) {
			oldProject.setExtra(project.getExtra());
		}
		if (project.isNameUpdate()) {
			oldProject.setName(project.getName());
		}
		return projectDao.merge(oldProject);
	}

	// TODO ignore @sql.handle_conflicts(conflict_type='project')
	@Override
	public Project deleteProject(String projectid) {
		Project project = getProject(projectid);
		projectDao.remove(project);
		return null;
	}

	@Override
	public Domain createDomain(String domainid, Domain domain) {
		domainDao.persist(domain);
		return domain;
	}

	@Override
	public List<Domain> listDomains(Hints hints) throws Exception {
		ListFunction<Domain> function = new TruncatedFunction<Domain>(new ListDomainsFunction());
		return function.execute(hints);
	}

	@Override
	public List<Domain> listDomainsForUser(String userid, List<String> groupids, Hints hints) {
		return domainDao.listDomainsForUser(userid, groupids);
	}

	@Override
	public List<Domain> listDomainsForGroups(List<String> groupids) {
		return domainDao.listDomainsForGroups(groupids);
	}

	@Override
	public Domain getDomain(String domainid) {
		Domain domain = domainDao.findById(domainid);
		if (domain == null) {
			throw Exceptions.DomainNotFoundException.getInstance(null, domainid);
		}
		return domain;
	}

	@Override
	public Domain getDomainByName(String domainName) {
		try {
			Domain domain = domainDao.findByName(domainName);
			return domain;
		} catch (NoResultException e) {
			throw Exceptions.DomainNotFoundException.getInstance(null, domainName);
		}
	}

	// TODO ignore @sql.handle_conflicts(conflict_type='domain')
	@Override
	public Domain updateDomain(String domainid, Domain domain) {
		Domain oldDomain = getDomain(domainid);
		if (domain.isDescriptionUpdated()) {
			oldDomain.setDescription(domain.getDescription());
		}
		if (domain.isEnabledUpdated()) {
			oldDomain.setEnabled(domain.getEnabled());
		}
		if (domain.isExtraUpdated()) {
			oldDomain.setExtra(domain.getExtra());
		}
		if (domain.isNameUpdated()) {
			oldDomain.setName(domain.getName());
		}
		return domainDao.merge(oldDomain);
	}

	@Override
	public void deleteDomain(String domainid) {
		Domain domain = getDomain(domainid);
		domainDao.remove(domain);
	}

	// TODO ignore @sql.handle_conflicts(conflict_type='role')
	@Override
	public Role createRole(String roleid, Role role) {
		roleDao.persist(role);
		return role;
	}

	@Override
	public List<Role> listRoles(Hints hints) throws Exception {
		ListFunction<Role> function = new TruncatedFunction<Role>(new ListRolesFunction());
		return function.execute(hints);
	}

	@Override
	public Role getRole(String roleid) {
		Role role = roleDao.findById(roleid);
		if (role == null) {
			throw Exceptions.RoleNotFoundException.getInstance(null, roleid);
		}
		return role;
	}

	// TODO ignore @sql.handle_conflicts(conflict_type='role')
	@Override
	public Role updateRole(String roleid, Role role) {
		Role oldRole = getRole(roleid);
		if (role.isDescriptionUpdated()) {
			oldRole.setDescription(role.getDescription());
		}
		if (role.isExtraUpdated()) {
			oldRole.setExtra(role.getExtra());
		}
		if (role.isNameUpdated()) {
			oldRole.setName(role.getName());
		}
		return roleDao.merge(oldRole);
	}

	@Override
	public void deleteRole(String roleid) {
		Role role = getRole(roleid);
		roleAssignmentDao.removeByRoleId(roleid);
		roleDao.remove(role);
	}

	@Override
	public void deleteUser(String userid) {
		roleAssignmentDao.removeByActorId(userid);
	}

	@Override
	public void deleteGroup(String groupid) {
		roleAssignmentDao.removeByActorId(groupid);
	}

	// _get_metadata
	// @Override
	// public GroupProjectGrant getGroupProjectGrant(String groupid, String
	// projectid, String roleid) {
	// try {
	// GroupProjectGrant grant = groupProjectGrantDao.getGrant(groupid,
	// projectid, roleid);
	// return grant;
	// } catch (NoResultException e) {
	// throw Exceptions.MetadataNotFoundException.getInstance(null);
	// }
	// }

	// _get_metadata
	// @Override
	// public GroupDomainGrant getGroupDomainGrant(String groupid, String
	// domainid, String roleid) {
	// try {
	// GroupDomainGrant grant = groupDomainGrantDao.getGrant(groupid, domainid,
	// roleid);
	// return grant;
	// } catch (NoResultException e) {
	// throw Exceptions.MetadataNotFoundException.getInstance(null);
	// }
	// }

	// @Override
	// public List<GroupDomainGrant> getGroupDomainGrants(String groupid, String
	// domainid) {
	// try {
	// List<GroupDomainGrant> grants =
	// groupDomainGrantDao.findByGroupidAndDomainid(groupid, domainid);
	// return grants;
	// } catch (NoResultException e) {
	// throw Exceptions.MetadataNotFoundException.getInstance(null);
	// }
	// }

	// @Override
	// public List<UserDomainGrant> getUserDomainGrants(String userid, String
	// domainid) {
	// try {
	// List<UserDomainGrant> grants =
	// userDomainGrantDao.findByUseridAndDomainid(userid, domainid);
	// return grants;
	// } catch (NoResultException e) {
	// throw Exceptions.MetadataNotFoundException.getInstance(null);
	// }
	// }

	// @Override
	// public List<GroupProjectGrant> getGroupProjectGrants(String groupid,
	// String projectid) {
	// try {
	// List<GroupProjectGrant> grants =
	// groupProjectGrantDao.findByGroupidAndProjectid(groupid, projectid);
	// return grants;
	// } catch (NoResultException e) {
	// throw Exceptions.MetadataNotFoundException.getInstance(null);
	// }
	// }

	// @Override
	// public List<UserProjectGrant> getUserProjectGrants(String userid, String
	// projectid) {
	// try {
	// List<UserProjectGrant> grants =
	// userProjectGrantDao.findByUseridAndProjectid(userid, projectid);
	// return grants;
	// } catch (NoResultException e) {
	// throw Exceptions.MetadataNotFoundException.getInstance(null);
	// }
	// }

	// _get_metadata
	// @Override
	// public UserProjectGrant getUserProjectGrant(String userid, String
	// projectid, String roleid) {
	// try {
	// UserProjectGrant grant = userProjectGrantDao.getGrant(userid, projectid,
	// roleid);
	// return grant;
	// } catch (NoResultException e) {
	// throw Exceptions.MetadataNotFoundException.getInstance(null);
	// }
	// }

	// _get_metadata
	// @Override
	// public UserDomainGrant getUserDomainGrant(String userid, String domainid,
	// String roleid) {
	// try {
	// UserDomainGrant grant = userDomainGrantDao.getGrant(userid, domainid,
	// roleid);
	// return grant;
	// } catch (NoResultException e) {
	// throw Exceptions.MetadataNotFoundException.getInstance(null);
	// }
	// }

	@Override
	public Integer getListLimit() {
		return Config.getOpt(Config.Type.assignment, "list_limit").asInteger();
	}

	@Override
	public boolean isLeafProject(String tenantid) {
		List<String> projectids = new ArrayList<String>();
		projectids.add(tenantid);
		List<Project> projectRefs = getChildren(projectids);
		return projectRefs.isEmpty();
	}

	@Override
	public Metadata getMetadata(String userid, String tenantid, String domainid, String groupid) {
		RoleAssignment.AssignmentType type = calcAssignmentType(userid, tenantid);
		String actorId = Strings.isNullOrEmpty(userid) ? groupid : userid;
		String targetId = Strings.isNullOrEmpty(tenantid) ? domainid : tenantid;
		List<RoleAssignment> refs = roleAssignmentDao.findAll(type, actorId, targetId);
		if (refs == null || refs.isEmpty()) {
			throw Exceptions.MetadataNotFoundException.getInstance();
		}

		Metadata metadataRef = new Metadata();
		for (RoleAssignment assignment : refs) {
			Metadata.Role roleRef = new Metadata.Role();
			roleRef.setId(assignment.getRoleId());
			if (assignment.getInherited()) {
				roleRef.setIheritedTo("projects");
			}
			metadataRef.getRoles().add(roleRef);
		}

		return metadataRef;

	}

	private AssignmentType calcAssignmentType(String userid, String tenantid) {
		if (!Strings.isNullOrEmpty(userid)) {
			if (!Strings.isNullOrEmpty(tenantid)) {
				return AssignmentType.USER_PROJECT;
			} else {
				return AssignmentType.USER_DOMAIN;
			}
		} else {
			if (!Strings.isNullOrEmpty(tenantid)) {
				return AssignmentType.GROUP_PROJECT;
			} else {
				return AssignmentType.GROUP_DOMAIN;
			}
		}
	}

	@Override
	public List<Project> listUserProjects(String userid, Hints hints) {
		throw Exceptions.NotImplementedException.getInstance();
	}

	@Override
	public List<String> rolesFromRoleDicts(List<com.infinities.keystone4j.model.assignment.Metadata.Role> dictList,
			boolean inherited) {
		List<String> roleList = new ArrayList<String>();

		for (com.infinities.keystone4j.model.assignment.Metadata.Role role : dictList) {
			if ((Strings.isNullOrEmpty(role.getIheritedTo()) && !inherited) || "projects".equals(role.getIheritedTo())
					&& inherited) {
				roleList.add(role.getId());
			}
		}
		return roleList;
	}

}
