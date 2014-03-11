package com.infinities.keystone4j.assignment.driver;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

import javax.persistence.NoResultException;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.model.Assignment;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.assignment.model.GroupDomainGrant;
import com.infinities.keystone4j.assignment.model.GroupDomainGrantMetadata;
import com.infinities.keystone4j.assignment.model.GroupProjectGrant;
import com.infinities.keystone4j.assignment.model.GroupProjectGrantMetadata;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.assignment.model.UserDomainGrant;
import com.infinities.keystone4j.assignment.model.UserDomainGrantMetadata;
import com.infinities.keystone4j.assignment.model.UserProjectGrant;
import com.infinities.keystone4j.assignment.model.UserProjectGrantMetadata;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.jpa.impl.DomainDao;
import com.infinities.keystone4j.jpa.impl.GroupDomainGrantDao;
import com.infinities.keystone4j.jpa.impl.GroupDomainGrantMetadataDao;
import com.infinities.keystone4j.jpa.impl.GroupProjectGrantDao;
import com.infinities.keystone4j.jpa.impl.GroupProjectGrantMetadataDao;
import com.infinities.keystone4j.jpa.impl.ProjectDao;
import com.infinities.keystone4j.jpa.impl.RoleDao;
import com.infinities.keystone4j.jpa.impl.UserDao;
import com.infinities.keystone4j.jpa.impl.UserDomainGrantDao;
import com.infinities.keystone4j.jpa.impl.UserDomainGrantMetadataDao;
import com.infinities.keystone4j.jpa.impl.UserProjectGrantDao;
import com.infinities.keystone4j.jpa.impl.UserProjectGrantMetadataDao;

public class AssignmentJpaDriver implements AssignmentDriver {

	private final ProjectDao projectDao;
	private final UserDao userDao;
	private final RoleDao roleDao;
	private final DomainDao domainDao;
	// private final AssignmentDao assignmentDao;
	private final UserProjectGrantDao userProjectGrantDao;
	private final UserDomainGrantDao userDomainGrantDao;
	private final GroupProjectGrantDao groupProjectGrantDao;
	private final GroupDomainGrantDao groupDomainGrantDao;
	private final UserProjectGrantMetadataDao userProjectGrantMetadataDao;
	private final GroupDomainGrantMetadataDao groupDomainGrantMetadataDao;
	private final GroupProjectGrantMetadataDao groupProjectGrantMetadataDao;
	private final UserDomainGrantMetadataDao userDomainGrantMetadataDao;
	private final static String PROJECTS = "projects";
	private final static String ENABLED = "enabled";
	private final static String ROLE_GRANT = "role grant";
	private final static String USER_ALREADY_HAS_ROLE = "User {0} already has role {1} in tenant {2}";
	private final static String CANNOT_REMOVE_ROLE = "Cannot remove role that has not been granted, {0}";


	public AssignmentJpaDriver() {
		super();
		this.projectDao = new ProjectDao();
		this.userDao = new UserDao();
		this.roleDao = new RoleDao();
		this.domainDao = new DomainDao();
		// this.assignmentDao = assignmentDao;
		this.userProjectGrantDao = new UserProjectGrantDao();
		this.userDomainGrantDao = new UserDomainGrantDao();
		this.groupProjectGrantDao = new GroupProjectGrantDao();
		this.groupDomainGrantDao = new GroupDomainGrantDao();
		this.userProjectGrantMetadataDao = new UserProjectGrantMetadataDao();
		this.groupDomainGrantMetadataDao = new GroupDomainGrantMetadataDao();
		this.groupProjectGrantMetadataDao = new GroupProjectGrantMetadataDao();
		this.userDomainGrantMetadataDao = new UserDomainGrantMetadataDao();
	}

	@Override
	public Project getProject(String projectid) {
		Project project = projectDao.findById(projectid);
		if (project != null) {
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
	public List<User> listUsersForProject(String projectid) {
		return userDao.listUsersForProject(projectid);
	}

	@Override
	public List<Project> listProjects(String domainid) {
		if (!Strings.isNullOrEmpty(domainid)) {
			getDomain(domainid);
		}

		List<Project> projects = projectDao.listProject(domainid);
		return projects;
	}

	@Override
	public List<Project> listProjectsForUser(String userid, List<String> groupids) {
		List<UserProjectGrant> userProjects = userProjectGrantDao.listUserProjectGrant(userid);
		Set<Project> projects = Sets.newHashSet();

		for (UserProjectGrant userProjectGrant : userProjects) {
			if (!userProjectGrant.getMetadatas().isEmpty()) {
				projects.add(userProjectGrant.getProject());
			}
		}

		for (String groupid : groupids) {
			List<GroupProjectGrant> grants = groupProjectGrantDao.listGroupProjectGrant(groupid);
			for (GroupProjectGrant grant : grants) {
				if (!grant.getMetadatas().isEmpty()) {
					projects.add(grant.getProject());
				}
			}
		}

		boolean enabled = Config.Instance.getOpt(Config.Type.os_inherit, ENABLED).asBoolean();
		if (!enabled) {
			return Lists.newArrayList(projects);
		}

		Set<Domain> domains = Sets.newHashSet();
		List<UserDomainGrant> userDomainGrants = userDomainGrantDao.listUserDomainGrant(userid);
		domains.addAll(listDomainsWithInheritedGrantsByUser(userDomainGrants));

		for (String groupid : groupids) {
			List<GroupDomainGrant> grants = groupDomainGrantDao.listGroupDomainGrant(groupid);
			domains.addAll(listDomainsWithInheritedGrantsByGroup(grants));
		}

		for (Domain domain : domains) {
			List<Project> refs = projectDao.listProject(domain.getId());
			projects.addAll(refs);
		}

		return Lists.newArrayList(projects);
	}

	@Override
	public void addRoleToUserAndProject(String userid, String projectid, String roleid) {
		User user = new User();
		user.setId(userid);
		Project project = getProject(projectid);
		Role role = getRole(roleid);

		boolean isNew = false;
		UserProjectGrant grant = null;
		try {
			// _get_metadata
			grant = getUserProjectGrant(userid, projectid);
		} catch (Exception e) {
			isNew = true;
			grant = new UserProjectGrant();
			grant.setUser(user);
			grant.setProject(project);
		}

		try {
			// _add_role_to_role_dics
			UserProjectGrantMetadata userProjectGrantMetadata = addRoleToUserProjectGrantMetadata(role, grant, false, false);
			// TODO should we do this?
			userProjectGrantMetadataDao.persist(userProjectGrantMetadata);

		} catch (IllegalArgumentException e) {
			String msg = MessageFormat.format(USER_ALREADY_HAS_ROLE, userid, roleid, projectid);
			throw Exceptions.ConflictException.getInstance(null, ROLE_GRANT, msg); // replace
			// KeyError
		}

		if (isNew) {
			userProjectGrantDao.persist(grant);
		} else {
			userProjectGrantDao.merge(grant);
		}

	}

	@Override
	public void removeRoleFromUserAndProject(String userid, String projectid, String roleid) {
		UserProjectGrant grant = null;
		try {
			// _get_metadata
			grant = getUserProjectGrant(userid, projectid);
		} catch (Exception e) {
			String msg = MessageFormat.format(CANNOT_REMOVE_ROLE, roleid);
			throw Exceptions.RoleNotFoundException.getInstance(msg);
		}

		try {
			// _remove_role_from_role_dics
			UserProjectGrantMetadata metadata = removeRoleFromUserProjectGrantMetadata(roleid, grant, false);
			userProjectGrantMetadataDao.remove(metadata);
		} catch (IllegalArgumentException e) {
			String msg = MessageFormat.format(CANNOT_REMOVE_ROLE, roleid);
			throw Exceptions.RoleNotFoundException.getInstance(msg);
		}

		if (!grant.getMetadatas().isEmpty()) {
			userProjectGrantDao.merge(grant);
		} else {
			userProjectGrantDao.remove(grant);
		}

	}

	@Override
	public List<Assignment> listRoleAssignments() {
		// TODO wired
		// List<Assignment> assignments = assignmentDao.findAll();
		// return assignments;
		List<Assignment> assignments = Lists.newArrayList();
		List<UserDomainGrant> userDomainGrants = userDomainGrantDao.findAll();
		boolean[] bools = new boolean[] { true, false };
		for (boolean b : bools) {
			for (UserDomainGrant grant : userDomainGrants) {
				List<Role> roles = filterUserDomainGrant(grant.getMetadatas(), b);
				for (Role role : roles) {
					Assignment assignment = new Assignment();
					assignment.setRole(role);
					assignment.setUser(grant.getUser());
					assignment.setDomain(grant.getDomain());
					assignment.setInheritedToProjects(b);
					assignments.add(assignment);
				}
			}
		}
		List<UserProjectGrant> userProjectGrants = userProjectGrantDao.findAll();
		for (UserProjectGrant grant : userProjectGrants) {
			List<Role> roles = filterUserProjectGrant(grant.getMetadatas(), false);
			for (Role role : roles) {
				Assignment assignment = new Assignment();
				assignment.setRole(role);
				assignment.setUser(grant.getUser());
				assignment.setProject(grant.getProject());
				assignment.setInheritedToProjects(false);
				assignments.add(assignment);
			}
		}
		List<GroupDomainGrant> groupDomainGrants = groupDomainGrantDao.findAll();
		for (boolean b : bools) {
			for (GroupDomainGrant grant : groupDomainGrants) {
				List<Role> roles = filterGroupDomainGrant(grant.getMetadatas(), b);
				for (Role role : roles) {
					Assignment assignment = new Assignment();
					assignment.setRole(role);
					assignment.setGroup(grant.getGroup());
					assignment.setDomain(grant.getDomain());
					assignment.setInheritedToProjects(b);
					assignments.add(assignment);
				}
			}
		}
		List<GroupProjectGrant> groupProjectGrants = groupProjectGrantDao.findAll();
		for (GroupProjectGrant grant : groupProjectGrants) {
			List<Role> roles = filterGroupProjectGrant(grant.getMetadatas(), false);
			for (Role role : roles) {
				Assignment assignment = new Assignment();
				assignment.setRole(role);
				assignment.setGroup(grant.getGroup());
				assignment.setProject(grant.getProject());
				assignment.setInheritedToProjects(false);
				assignments.add(assignment);
			}
		}

		return assignments;
	}

	@Override
	public Project createProject(Project project) {
		projectDao.persist(project);
		return project;
	}

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

	@Override
	public void deleteProject(String projectid) {
		Project project = getProject(projectid);
		projectDao.remove(project);
	}

	@Override
	public Domain createDomain(Domain domain) {
		domainDao.persist(domain);
		return domain;
	}

	@Override
	public List<Domain> listDomains() {
		return domainDao.findAll();
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

	@Override
	public Role createRole(Role role) {
		roleDao.persist(role);
		return role;
	}

	@Override
	public List<Role> listRoles() {
		return roleDao.findAll();
	}

	@Override
	public Role getRole(String roleid) {
		Role role = roleDao.findById(roleid);
		if (role != null) {
			throw Exceptions.RoleNotFoundException.getInstance(null, roleid);
		}
		return role;
	}

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
		roleDao.remove(role);
	}

	// @Override
	// public void deleteUser() {
	// }
	//
	// @Override
	// public void deleteGroup() {
	// // TODO Auto-generated method stub
	//
	// }

	// _get_metadata
	@Override
	public GroupProjectGrant getGroupProjectGrant(String groupid, String projectid) {
		try {
			GroupProjectGrant grant = groupProjectGrantDao.findByGroupidAndProjectid(groupid, projectid);
			return grant;
		} catch (NoResultException e) {
			throw Exceptions.MetadataNotFoundException.getInstance(null);
		}
	}

	// _get_metadata
	@Override
	public GroupDomainGrant getGroupDomainGrant(String groupid, String domainid) {
		try {
			GroupDomainGrant grant = groupDomainGrantDao.findByGroupidAndDomainid(groupid, domainid);
			return grant;
		} catch (NoResultException e) {
			throw Exceptions.MetadataNotFoundException.getInstance(null);
		}
	}

	// _get_metadata
	@Override
	public UserProjectGrant getUserProjectGrant(String userid, String projectid) {
		try {
			UserProjectGrant grant = userProjectGrantDao.findByUseridAndProjectid(userid, projectid);
			return grant;
		} catch (NoResultException e) {
			throw Exceptions.MetadataNotFoundException.getInstance(null);
		}
	}

	// _get_metadata
	@Override
	public UserDomainGrant getUserDomainGrant(String userid, String domainid) {
		try {
			UserDomainGrant grant = userDomainGrantDao.findByUseridAndDomainid(userid, domainid);
			return grant;
		} catch (NoResultException e) {
			throw Exceptions.MetadataNotFoundException.getInstance(null);
		}
	}

	@Override
	public void deleteGrantByGroupDomain(String roleid, String groupid, String domainid, boolean inherited) {
		getRole(roleid);
		getDomain(domainid);
		GroupDomainGrant grant = null;
		// _get_metadata
		grant = getGroupDomainGrant(groupid, domainid);
		try {
			// _remove_role_from_role_dics
			GroupDomainGrantMetadata metadata = removeRoleFromGroupDomainGrantMetadata(roleid, grant, inherited);
			groupDomainGrantMetadataDao.remove(metadata);
		} catch (IllegalArgumentException e) {
			throw Exceptions.RoleNotFoundException.getInstance(null, roleid);
		}
		groupDomainGrantDao.merge(grant);
	}

	@Override
	public void deleteGrantByGroupProject(String roleid, String groupid, String projectid, boolean inherited) {
		getRole(roleid);
		getProject(projectid);
		GroupProjectGrant grant = null;

		// _get_metadata
		grant = getGroupProjectGrant(groupid, projectid);
		try {
			// _remove_role_from_role_dics
			GroupProjectGrantMetadata metadata = removeRoleFromGroupProjectGrantMetadata(roleid, grant, inherited);
			groupProjectGrantMetadataDao.remove(metadata);
		} catch (IllegalArgumentException e) {
			throw Exceptions.RoleNotFoundException.getInstance(null, roleid);
		}
		groupProjectGrantDao.merge(grant);
	}

	@Override
	public void deleteGrantByUserDomain(String roleid, String userid, String domainid, boolean inherited) {
		getRole(roleid);
		getDomain(domainid);
		UserDomainGrant grant = null;

		// _get_metadata
		grant = getUserDomainGrant(userid, domainid);
		try {
			// _remove_role_from_role_dics
			UserDomainGrantMetadata metadata = removeRoleFromUserDomainGrantMetadata(roleid, grant, inherited);
			userDomainGrantMetadataDao.remove(metadata);
		} catch (IllegalArgumentException e) {
			throw Exceptions.RoleNotFoundException.getInstance(null, roleid);
		}
		userDomainGrantDao.merge(grant);
	}

	@Override
	public void deleteGrantByUserProject(String roleid, String userid, String projectid, boolean inherited) {
		getRole(roleid);
		getProject(projectid);
		UserProjectGrant grant = null;
		// _get_metadata
		grant = getUserProjectGrant(userid, projectid);
		try {
			// _remove_role_from_role_dics
			UserProjectGrantMetadata metadata = removeRoleFromUserProjectGrantMetadata(roleid, grant, inherited);
			userProjectGrantMetadataDao.remove(metadata);
		} catch (IllegalArgumentException e) {
			throw Exceptions.RoleNotFoundException.getInstance(null, roleid);
		}
		userProjectGrantDao.merge(grant);
	}

	@Override
	public Role getGrantByGroupDomain(String roleid, String groupid, String domainid, boolean inherited) {
		Role role = getRole(roleid);
		getDomain(domainid);
		GroupDomainGrant grant = null;
		try {
			// _get_metadata
			grant = getGroupDomainGrant(groupid, domainid);
		} catch (Exception e) {
			grant = new GroupDomainGrant();
		}
		// _roles_from_role_dicts
		List<Role> roles = filterGroupDomainGrant(grant.getMetadatas(), inherited);
		if (!roles.contains(role)) {
			throw Exceptions.RoleNotFoundException.getInstance(null, roleid);
		}
		return role;
	}

	@Override
	public Role getGrantByGroupProject(String roleid, String groupid, String projectid, boolean inherited) {
		Role role = getRole(roleid);
		getProject(projectid);
		GroupProjectGrant grant = null;
		try {
			// _get_metadata
			grant = getGroupProjectGrant(groupid, projectid);
		} catch (Exception e) {
			grant = new GroupProjectGrant();
		}
		// _roles_from_role_dicts
		List<Role> roles = filterGroupProjectGrant(grant.getMetadatas(), inherited);
		if (!roles.contains(role)) {
			throw Exceptions.RoleNotFoundException.getInstance(null, roleid);
		}
		return role;
	}

	@Override
	public Role getGrantByUserDomain(String roleid, String userid, String domainid, boolean inherited) {
		Role role = getRole(roleid);
		getDomain(domainid);
		UserDomainGrant grant = null;
		try {
			// _get_metadata
			grant = getUserDomainGrant(userid, domainid);
		} catch (Exception e) {
			grant = new UserDomainGrant();
		}
		// _roles_from_role_dicts
		List<Role> roles = filterUserDomainGrant(grant.getMetadatas(), inherited);
		if (!roles.contains(role)) {
			throw Exceptions.RoleNotFoundException.getInstance(null, roleid);
		}
		return role;
	}

	@Override
	public Role getGrantByUserProject(String roleid, String userid, String projectid, boolean inherited) {
		Role role = getRole(roleid);
		getProject(projectid);
		UserProjectGrant grant = null;
		try {
			// _get_metadata
			grant = getUserProjectGrant(userid, projectid);
		} catch (Exception e) {
			grant = new UserProjectGrant();
		}
		// _roles_from_role_dicts
		List<Role> roles = filterUserProjectGrant(grant.getMetadatas(), inherited);
		if (!roles.contains(role)) {
			throw Exceptions.RoleNotFoundException.getInstance(null, roleid);
		}
		return role;
	}

	@Override
	public void createGrantByGroupDomain(String roleid, String groupid, String domainid, boolean inherited) {
		Role role = getRole(roleid);
		getDomain(domainid);
		GroupDomainGrant grant = null;
		boolean isNew = false;
		try {
			// _get_metadata
			grant = getGroupDomainGrant(groupid, domainid);
		} catch (Exception e) {
			grant = new GroupDomainGrant();
			isNew = true;
		}
		// _add_role_to_role_dics
		GroupDomainGrantMetadata groupDomainGrantMetadata = addRoleToGroupDomainGrantMetadata(role, grant, inherited, true);
		// TODO should we do this?
		groupDomainGrantMetadataDao.persist(groupDomainGrantMetadata);

		if (isNew) {
			groupDomainGrantDao.persist(grant);
		} else {
			groupDomainGrantDao.merge(grant);
		}
	}

	@Override
	public void createGrantByGroupProject(String roleid, String groupid, String projectid) {
		Role role = getRole(roleid);
		getProject(projectid);
		GroupProjectGrant grant = null;
		boolean isNew = false;
		try {
			// _get_metadata
			grant = getGroupProjectGrant(groupid, projectid);
		} catch (Exception e) {
			grant = new GroupProjectGrant();
			isNew = true;
		}

		// _add_role_to_role_dics
		GroupProjectGrantMetadata groupProjectGrantMetadata = addRoleToGroupProjectGrantMetadata(role, grant, false, true);
		// TODO should we do this?
		groupProjectGrantMetadataDao.persist(groupProjectGrantMetadata);

		if (isNew) {
			groupProjectGrantDao.persist(grant);
		} else {
			groupProjectGrantDao.merge(grant);
		}
	}

	@Override
	public void createGrantByUserDomain(String roleid, String userid, String domainid, boolean inherited) {
		Role role = getRole(roleid);
		getDomain(domainid);
		UserDomainGrant grant = null;
		boolean isNew = false;
		try {
			// _get_metadata
			grant = getUserDomainGrant(userid, domainid);
		} catch (Exception e) {
			grant = new UserDomainGrant();
			isNew = true;
		}

		// _add_role_to_role_dics
		UserDomainGrantMetadata userDomainGrantMetadata = addRoleToUserDomainGrantMetadata(role, grant, inherited, true);
		// TODO should we do this?
		userDomainGrantMetadataDao.persist(userDomainGrantMetadata);

		if (isNew) {
			userDomainGrantDao.persist(grant);
		} else {
			userDomainGrantDao.merge(grant);
		}
	}

	@Override
	public void createGrantByUserProject(String roleid, String userid, String projectid) {
		Role role = getRole(roleid);
		getProject(projectid);
		UserProjectGrant grant = null;
		boolean isNew = false;
		try {
			// _get_metadata
			grant = getUserProjectGrant(userid, projectid);
		} catch (Exception e) {
			grant = new UserProjectGrant();
			isNew = true;
		}

		// _add_role_to_role_dics
		UserProjectGrantMetadata userProjectGrantMetadata = addRoleToUserProjectGrantMetadata(role, grant, false, true);
		// TODO should we do this?
		userProjectGrantMetadataDao.persist(userProjectGrantMetadata);

		if (isNew) {
			userProjectGrantDao.persist(grant);
		} else {
			userProjectGrantDao.merge(grant);
		}
	}

	@Override
	public List<Role> listGrantsByGroupDomain(String groupid, String domainid, boolean inherited) {
		getDomain(domainid);
		GroupDomainGrant grant = null;
		try {
			// _get_metadata
			grant = getGroupDomainGrant(groupid, domainid);
		} catch (Exception e) {
			grant = new GroupDomainGrant();
		}
		// _roles_from_role_dics
		List<Role> roles = filterGroupDomainGrant(grant.getMetadatas(), inherited);
		return roles;
	}

	@Override
	public List<Role> listGrantsByGroupProject(String groupid, String projectid, boolean inherited) {
		getProject(projectid);
		GroupProjectGrant grant = null;
		try {
			// _get_metadata
			grant = getGroupProjectGrant(groupid, projectid);
		} catch (Exception e) {
			grant = new GroupProjectGrant();
		}
		// _roles_from_role_dics
		List<Role> roles = filterGroupProjectGrant(grant.getMetadatas(), inherited);
		return roles;
	}

	@Override
	public List<Role> listGrantsByUserProject(String userid, String projectid, boolean inherited) {
		getProject(projectid);
		UserProjectGrant grant = null;
		try {
			// _get_metadata
			grant = getUserProjectGrant(userid, projectid);
		} catch (Exception e) {
			grant = new UserProjectGrant();
		}
		// _roles_from_role_dics
		List<Role> roles = filterUserProjectGrant(grant.getMetadatas(), inherited);
		return roles;
	}

	@Override
	public List<Role> listGrantsByUserDomain(String userid, String domainid, boolean inherited) {
		getDomain(domainid);
		UserDomainGrant grant = null;
		try {
			// _get_metadata
			grant = getUserDomainGrant(userid, domainid);
		} catch (Exception e) {
			grant = new UserDomainGrant();
		}
		// _roles_from_role_dics
		List<Role> roles = filterUserDomainGrant(grant.getMetadatas(), inherited);
		return roles;
	}

	private Set<Domain> listDomainsWithInheritedGrantsByUser(List<UserDomainGrant> userDomainGrants) {
		Set<Domain> domains = Sets.newHashSet();
		for (UserDomainGrant userDomainGrant : userDomainGrants) {
			for (UserDomainGrantMetadata metadata : userDomainGrant.getMetadatas()) {
				if (!Strings.isNullOrEmpty(metadata.getRole().getInheritedTo())) {
					domains.add(userDomainGrant.getDomain());
				}
			}
		}
		return domains;
	}

	private Set<Domain> listDomainsWithInheritedGrantsByGroup(List<GroupDomainGrant> groupDomainGrants) {
		Set<Domain> domains = Sets.newHashSet();
		for (GroupDomainGrant groupDomainGrant : groupDomainGrants) {
			for (GroupDomainGrantMetadata metadata : groupDomainGrant.getMetadatas()) {
				if (!Strings.isNullOrEmpty(metadata.getRole().getInheritedTo())) {
					domains.add(groupDomainGrant.getDomain());
				}
			}
		}
		return domains;
	}

	// _roles_from_role_dics
	private List<Role> filterGroupDomainGrant(Set<GroupDomainGrantMetadata> metadatas, boolean inheritedToProjects) {
		List<Role> ret = Lists.newArrayList();
		for (GroupDomainGrantMetadata metadata : metadatas) {
			if ((!inheritedToProjects && Strings.isNullOrEmpty(metadata.getRole().getInheritedTo()))
					|| (inheritedToProjects && PROJECTS.equals(metadata.getRole().getInheritedTo()))) {
				ret.add(metadata.getRole());
			}

		}
		return ret;
	}

	// _roles_from_role_dics
	private List<Role> filterGroupProjectGrant(Set<GroupProjectGrantMetadata> metadatas, boolean inheritedToProjects) {
		List<Role> ret = Lists.newArrayList();
		for (GroupProjectGrantMetadata metadata : metadatas) {
			if ((!inheritedToProjects && Strings.isNullOrEmpty(metadata.getRole().getInheritedTo()))
					|| (inheritedToProjects && PROJECTS.equals(metadata.getRole().getInheritedTo()))) {
				ret.add(metadata.getRole());
			}

		}
		return ret;
	}

	// _roles_from_role_dics
	private List<Role> filterUserDomainGrant(Set<UserDomainGrantMetadata> metadatas, boolean inheritedToProjects) {
		List<Role> ret = Lists.newArrayList();
		for (UserDomainGrantMetadata metadata : metadatas) {
			if ((!inheritedToProjects && Strings.isNullOrEmpty(metadata.getRole().getInheritedTo()))
					|| (inheritedToProjects && PROJECTS.equals(metadata.getRole().getInheritedTo()))) {
				ret.add(metadata.getRole());
			}

		}
		return ret;
	}

	// _roles_from_role_dics
	private List<Role> filterUserProjectGrant(Set<UserProjectGrantMetadata> metadatas, boolean inheritedToProjects) {
		List<Role> ret = Lists.newArrayList();
		for (UserProjectGrantMetadata metadata : metadatas) {
			if ((!inheritedToProjects && Strings.isNullOrEmpty(metadata.getRole().getInheritedTo()))
					|| (inheritedToProjects && PROJECTS.equals(metadata.getRole().getInheritedTo()))) {
				ret.add(metadata.getRole());
			}

		}
		return ret;
	}

	// _add_role_to_role_dics
	private GroupDomainGrantMetadata addRoleToGroupDomainGrantMetadata(Role role, GroupDomainGrant grant,
			boolean inheritedToProjects, boolean allowExisting) {
		if (inheritedToProjects) {
			role.setInheritedTo(PROJECTS);
		}
		GroupDomainGrantMetadata groupDomainGrantMetadata = new GroupDomainGrantMetadata();
		groupDomainGrantMetadata.setRole(role);
		groupDomainGrantMetadata.setGrant(grant);
		if (!allowExisting) {
			for (GroupDomainGrantMetadata metadata : grant.getMetadatas()) {
				if (role.getId().equals(metadata.getRole().getId())) {
					throw new IllegalArgumentException();
				}
			}
		}

		grant.getMetadatas().add(groupDomainGrantMetadata);

		return groupDomainGrantMetadata;
	}

	// _add_role_to_role_dics
	private GroupProjectGrantMetadata addRoleToGroupProjectGrantMetadata(Role role, GroupProjectGrant grant,
			boolean inheritedToProjects, boolean allowExisting) {
		if (inheritedToProjects) {
			role.setInheritedTo(PROJECTS);
		}
		GroupProjectGrantMetadata groupProjectGrantMetadata = new GroupProjectGrantMetadata();
		groupProjectGrantMetadata.setRole(role);
		groupProjectGrantMetadata.setGrant(grant);
		if (!allowExisting) {
			for (GroupProjectGrantMetadata metadata : grant.getMetadatas()) {
				if (role.getId().equals(metadata.getRole().getId())) {
					throw new IllegalArgumentException();
				}
			}
		}
		grant.getMetadatas().add(groupProjectGrantMetadata);

		return groupProjectGrantMetadata;
	}

	// _add_role_to_role_dics
	private UserDomainGrantMetadata addRoleToUserDomainGrantMetadata(Role role, UserDomainGrant grant,
			boolean inheritedToProjects, boolean allowExisting) {
		if (inheritedToProjects) {
			role.setInheritedTo(PROJECTS);
		}
		UserDomainGrantMetadata userDomainGrantMetadata = new UserDomainGrantMetadata();
		userDomainGrantMetadata.setRole(role);
		userDomainGrantMetadata.setGrant(grant);
		if (!allowExisting) {
			for (UserDomainGrantMetadata metadata : grant.getMetadatas()) {
				if (role.getId().equals(metadata.getRole().getId())) {
					throw new IllegalArgumentException();
				}
			}
		}
		grant.getMetadatas().add(userDomainGrantMetadata);

		return userDomainGrantMetadata;
	}

	// _add_role_to_role_dics
	private UserProjectGrantMetadata addRoleToUserProjectGrantMetadata(Role role, UserProjectGrant grant,
			boolean inheritedToProjects, boolean allowExisting) {
		if (inheritedToProjects) {
			role.setInheritedTo(PROJECTS);
		}
		UserProjectGrantMetadata userProjectGrantMetadata = new UserProjectGrantMetadata();
		userProjectGrantMetadata.setRole(role);
		userProjectGrantMetadata.setGrant(grant);
		if (!allowExisting) {
			for (UserProjectGrantMetadata metadata : grant.getMetadatas()) {
				if (role.getId().equals(metadata.getRole().getId())) {
					throw new IllegalArgumentException();
				}
			}
		}
		grant.getMetadatas().add(userProjectGrantMetadata);

		return userProjectGrantMetadata;
	}

	// _remove_role_from_role_dics
	private GroupProjectGrantMetadata removeRoleFromGroupProjectGrantMetadata(String roleid, GroupProjectGrant grant,
			boolean inheritedToProjects) {
		for (GroupProjectGrantMetadata ref : grant.getMetadatas()) {
			if (roleid.equals(ref.getRole().getId())) {
				grant.getMetadatas().remove(ref);
				return ref;
			}
		}
		// KeyError
		throw new IllegalArgumentException();
	}

	// _remove_role_from_role_dics
	private GroupDomainGrantMetadata removeRoleFromGroupDomainGrantMetadata(String roleid, GroupDomainGrant grant,
			boolean inheritedToProjects) {
		for (GroupDomainGrantMetadata ref : grant.getMetadatas()) {
			if (roleid.equals(ref.getRole().getId())) {
				grant.getMetadatas().remove(ref);
				return ref;
			}
		}
		// KeyError
		throw new IllegalArgumentException();
	}

	// _remove_role_from_role_dics
	private UserProjectGrantMetadata removeRoleFromUserProjectGrantMetadata(String roleid, UserProjectGrant grant,
			boolean inheritedToProjects) {
		for (UserProjectGrantMetadata ref : grant.getMetadatas()) {
			if (roleid.equals(ref.getRole().getId())) {
				grant.getMetadatas().remove(ref);
				return ref;
			}
		}
		// KeyError
		throw new IllegalArgumentException();
	}

	// _remove_role_from_role_dics
	private UserDomainGrantMetadata removeRoleFromUserDomainGrantMetadata(String roleid, UserDomainGrant grant,
			boolean inheritedToProjects) {
		for (UserDomainGrantMetadata ref : grant.getMetadatas()) {
			if (roleid.equals(ref.getRole().getId())) {
				grant.getMetadatas().remove(ref);
				return ref;
			}
		}
		// KeyError
		throw new IllegalArgumentException();
	}
}
