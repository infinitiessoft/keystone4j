package com.infinities.keystone4j.assignment.driver;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.model.Assignment;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.assignment.model.GroupDomainGrant;
import com.infinities.keystone4j.assignment.model.GroupProjectGrant;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.assignment.model.UserDomainGrant;
import com.infinities.keystone4j.assignment.model.UserProjectGrant;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.model.Group;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.jpa.impl.DomainDao;
import com.infinities.keystone4j.jpa.impl.GroupDomainGrantDao;
import com.infinities.keystone4j.jpa.impl.GroupProjectGrantDao;
import com.infinities.keystone4j.jpa.impl.ProjectDao;
import com.infinities.keystone4j.jpa.impl.RoleDao;
import com.infinities.keystone4j.jpa.impl.UserDao;
import com.infinities.keystone4j.jpa.impl.UserDomainGrantDao;
import com.infinities.keystone4j.jpa.impl.UserProjectGrantDao;

public class AssignmentJpaDriver implements AssignmentDriver {

	private final Logger logger = LoggerFactory.getLogger(AssignmentJpaDriver.class);
	private final ProjectDao projectDao;
	private final UserDao userDao;
	private final RoleDao roleDao;
	private final DomainDao domainDao;
	// private final AssignmentDao assignmentDao;
	private final UserProjectGrantDao userProjectGrantDao;
	private final UserDomainGrantDao userDomainGrantDao;
	private final GroupProjectGrantDao groupProjectGrantDao;
	private final GroupDomainGrantDao groupDomainGrantDao;
	// private final UserProjectGrantMetadataDao userProjectGrantMetadataDao;
	// private final GroupDomainGrantMetadataDao groupDomainGrantMetadataDao;
	// private final GroupProjectGrantMetadataDao groupProjectGrantMetadataDao;
	// private final UserDomainGrantMetadataDao userDomainGrantMetadataDao;
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
		// this.userProjectGrantMetadataDao = new UserProjectGrantMetadataDao();
		// this.groupDomainGrantMetadataDao = new GroupDomainGrantMetadataDao();
		// this.groupProjectGrantMetadataDao = new
		// GroupProjectGrantMetadataDao();
		// this.userDomainGrantMetadataDao = new UserDomainGrantMetadataDao();
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
			// if (!userProjectGrant.getMetadatas().isEmpty()) {
			projects.add(userProjectGrant.getProject());
			// }
		}

		for (String groupid : groupids) {
			List<GroupProjectGrant> grants = groupProjectGrantDao.listGroupProjectGrant(groupid);
			for (GroupProjectGrant grant : grants) {
				// if (!grant.getMetadatas().isEmpty()) {
				projects.add(grant.getProject());
				// }
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
			grant = getUserProjectGrant(userid, projectid, roleid);
		} catch (Exception e) {
			isNew = true;
			grant = new UserProjectGrant();
			grant.setUser(user);
			grant.setProject(project);
			grant.setRole(role);
		}

		try {
			// _add_role_to_role_dics
			addRoleToUserProjectGrantMetadata(role, grant, false, false);
			// TODO should we do this?
			// userProjectGrantDao.persist(grant);
		} catch (IllegalArgumentException e) {
			String msg = MessageFormat.format(USER_ALREADY_HAS_ROLE, userid, roleid, projectid);
			logger.error(msg, e);
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
			grant = getUserProjectGrant(userid, projectid, roleid);
			userProjectGrantDao.remove(grant);
		} catch (Exception e) {
			String msg = MessageFormat.format(CANNOT_REMOVE_ROLE, roleid);
			throw Exceptions.RoleNotFoundException.getInstance(msg);
		}

		// try {
		// // _remove_role_from_role_dics
		// UserProjectGrantMetadata metadata =
		// removeRoleFromUserProjectGrantMetadata(roleid, grant, false);
		// userProjectGrantMetadataDao.remove(metadata);
		// } catch (IllegalArgumentException e) {
		// String msg = MessageFormat.format(CANNOT_REMOVE_ROLE, roleid);
		// throw Exceptions.RoleNotFoundException.getInstance(msg);
		// }

		// if (!grant.getMetadatas().isEmpty()) {
		// userProjectGrantDao.merge(grant);
		// } else {

		// }

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
			List<UserDomainGrant> grants = filterUserDomainGrant(userDomainGrants, b);
			for (UserDomainGrant grant : grants) {

				// for (Role role : roles) {
				Assignment assignment = new Assignment();
				assignment.setRole(grant.getRole());
				assignment.setUser(grant.getUser());
				assignment.setDomain(grant.getDomain());
				assignment.setInheritedToProjects(b);
				assignments.add(assignment);
				// }
			}
		}

		List<UserProjectGrant> userProjectGrants = userProjectGrantDao.findAll();
		userProjectGrants = filterUserProjectGrant(userProjectGrants, false);
		for (UserProjectGrant grant : userProjectGrants) {
			// List<Role> roles = filterUserProjectGrant(grant.getMetadatas(),
			// false);
			// for (Role role : roles) {
			Assignment assignment = new Assignment();
			assignment.setRole(grant.getRole());
			assignment.setUser(grant.getUser());
			assignment.setProject(grant.getProject());
			assignment.setInheritedToProjects(false);
			assignments.add(assignment);
			// }
		}
		List<GroupDomainGrant> groupDomainGrants = groupDomainGrantDao.findAll();
		for (boolean b : bools) {
			List<GroupDomainGrant> grants = filterGroupDomainGrant(groupDomainGrants, b);
			for (GroupDomainGrant grant : grants) {

				// for (Role role : roles) {
				Assignment assignment = new Assignment();
				assignment.setRole(grant.getRole());
				assignment.setGroup(grant.getGroup());
				assignment.setDomain(grant.getDomain());
				assignment.setInheritedToProjects(b);
				assignments.add(assignment);
				// }
			}
		}
		List<GroupProjectGrant> groupProjectGrants = groupProjectGrantDao.findAll();
		groupProjectGrants = filterGroupProjectGrant(groupProjectGrants, false);
		for (GroupProjectGrant grant : groupProjectGrants) {

			// for (Role role : roles) {
			Assignment assignment = new Assignment();
			assignment.setRole(grant.getRole());
			assignment.setGroup(grant.getGroup());
			assignment.setProject(grant.getProject());
			assignment.setInheritedToProjects(false);
			assignments.add(assignment);
			// }
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
		if (role == null) {
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
	public GroupProjectGrant getGroupProjectGrant(String groupid, String projectid, String roleid) {
		try {
			GroupProjectGrant grant = groupProjectGrantDao.getGrant(groupid, projectid, roleid);
			return grant;
		} catch (NoResultException e) {
			throw Exceptions.MetadataNotFoundException.getInstance(null);
		}
	}

	// _get_metadata
	@Override
	public GroupDomainGrant getGroupDomainGrant(String groupid, String domainid, String roleid) {
		try {
			GroupDomainGrant grant = groupDomainGrantDao.getGrant(groupid, domainid, roleid);
			return grant;
		} catch (NoResultException e) {
			throw Exceptions.MetadataNotFoundException.getInstance(null);
		}
	}

	@Override
	public List<GroupDomainGrant> getGroupDomainGrants(String groupid, String domainid) {
		try {
			List<GroupDomainGrant> grants = groupDomainGrantDao.findByGroupidAndDomainid(groupid, domainid);
			return grants;
		} catch (NoResultException e) {
			throw Exceptions.MetadataNotFoundException.getInstance(null);
		}
	}

	@Override
	public List<UserDomainGrant> getUserDomainGrants(String userid, String domainid) {
		try {
			List<UserDomainGrant> grants = userDomainGrantDao.findByUseridAndDomainid(userid, domainid);
			return grants;
		} catch (NoResultException e) {
			throw Exceptions.MetadataNotFoundException.getInstance(null);
		}
	}

	@Override
	public List<GroupProjectGrant> getGroupProjectGrants(String groupid, String projectid) {
		try {
			List<GroupProjectGrant> grants = groupProjectGrantDao.findByGroupidAndProjectid(groupid, projectid);
			return grants;
		} catch (NoResultException e) {
			throw Exceptions.MetadataNotFoundException.getInstance(null);
		}
	}

	@Override
	public List<UserProjectGrant> getUserProjectGrants(String userid, String projectid) {
		try {
			List<UserProjectGrant> grants = userProjectGrantDao.findByUseridAndProjectid(userid, projectid);
			return grants;
		} catch (NoResultException e) {
			throw Exceptions.MetadataNotFoundException.getInstance(null);
		}
	}

	// _get_metadata
	@Override
	public UserProjectGrant getUserProjectGrant(String userid, String projectid, String roleid) {
		try {
			UserProjectGrant grant = userProjectGrantDao.getGrant(userid, projectid, roleid);
			return grant;
		} catch (NoResultException e) {
			throw Exceptions.MetadataNotFoundException.getInstance(null);
		}
	}

	// _get_metadata
	@Override
	public UserDomainGrant getUserDomainGrant(String userid, String domainid, String roleid) {
		try {
			UserDomainGrant grant = userDomainGrantDao.getGrant(userid, domainid, roleid);
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
		grant = getGroupDomainGrant(groupid, domainid, roleid);
		try {
			// _remove_role_from_role_dics
			// GroupDomainGrantMetadata metadata =
			// removeRoleFromGroupDomainGrantMetadata(roleid, grant, inherited);
			// groupDomainGrantMetadataDao.remove(metadata);
			groupDomainGrantDao.remove(grant);
		} catch (IllegalArgumentException e) {
			throw Exceptions.RoleNotFoundException.getInstance(null, roleid);
		}

	}

	@Override
	public void deleteGrantByGroupProject(String roleid, String groupid, String projectid, boolean inherited) {
		getRole(roleid);
		getProject(projectid);
		GroupProjectGrant grant = null;

		// _get_metadata
		grant = getGroupProjectGrant(groupid, projectid, roleid);
		try {
			// _remove_role_from_role_dics
			// GroupProjectGrantMetadata metadata =
			// removeRoleFromGroupProjectGrantMetadata(roleid, grant,
			// inherited);
			// groupProjectGrantMetadataDao.remove(metadata);
			groupProjectGrantDao.remove(grant);
		} catch (IllegalArgumentException e) {
			throw Exceptions.RoleNotFoundException.getInstance(null, roleid);
		}

	}

	@Override
	public void deleteGrantByUserDomain(String roleid, String userid, String domainid, boolean inherited) {
		getRole(roleid);
		getDomain(domainid);
		UserDomainGrant grant = null;

		// _get_metadata
		grant = getUserDomainGrant(userid, domainid, roleid);
		try {
			// _remove_role_from_role_dics
			// UserDomainGrantMetadata metadata =
			// removeRoleFromUserDomainGrantMetadata(roleid, grant, inherited);
			// userDomainGrantMetadataDao.remove(metadata);
			userDomainGrantDao.remove(grant);
		} catch (IllegalArgumentException e) {
			throw Exceptions.RoleNotFoundException.getInstance(null, roleid);
		}

	}

	@Override
	public void deleteGrantByUserProject(String roleid, String userid, String projectid, boolean inherited) {
		getRole(roleid);
		getProject(projectid);
		UserProjectGrant grant = null;
		// _get_metadata
		grant = getUserProjectGrant(userid, projectid, roleid);
		try {
			// _remove_role_from_role_dics
			// UserProjectGrantMetadata metadata =
			// removeRoleFromUserProjectGrantMetadata(roleid, grant, inherited);
			// userProjectGrantMetadataDao.remove(metadata);
			userProjectGrantDao.remove(grant);
		} catch (IllegalArgumentException e) {
			throw Exceptions.RoleNotFoundException.getInstance(null, roleid);
		}

	}

	@Override
	public Role getGrantByGroupDomain(String roleid, String groupid, String domainid, boolean inherited) {
		Role role = getRole(roleid);
		getDomain(domainid);
		GroupDomainGrant grant = null;
		try {
			// _get_metadata
			grant = getGroupDomainGrant(groupid, domainid, roleid);
		} catch (Exception e) {
			grant = new GroupDomainGrant();
		}
		// _roles_from_role_dicts
		List<GroupDomainGrant> grants = Lists.newArrayList();
		grants.add(grant);
		grants = filterGroupDomainGrant(grants, inherited);
		List<String> roleids = Lists.newArrayList();
		for (GroupDomainGrant g : grants) {
			roleids.add(g.getRole().getId());
		}
		if (!roleids.contains(role.getId())) {
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
			grant = getGroupProjectGrant(groupid, projectid, roleid);
		} catch (Exception e) {
			grant = new GroupProjectGrant();
		}
		// _roles_from_role_dicts
		List<GroupProjectGrant> grants = Lists.newArrayList();
		grants.add(grant);
		grants = filterGroupProjectGrant(grants, inherited);
		List<String> roleids = Lists.newArrayList();
		for (GroupProjectGrant g : grants) {
			roleids.add(g.getRole().getId());
		}
		if (!roleids.contains(role.getId())) {
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
			grant = getUserDomainGrant(userid, domainid, roleid);
		} catch (Exception e) {
			grant = new UserDomainGrant();
		}
		// _roles_from_role_dicts
		List<UserDomainGrant> grants = Lists.newArrayList();
		grants.add(grant);
		grants = filterUserDomainGrant(grants, inherited);
		List<String> roleids = Lists.newArrayList();
		for (UserDomainGrant g : grants) {
			roleids.add(g.getRole().getId());
		}
		if (!roleids.contains(role.getId())) {
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
			grant = getUserProjectGrant(userid, projectid, roleid);
		} catch (Exception e) {
			grant = new UserProjectGrant();
		}
		// _roles_from_role_dicts
		List<UserProjectGrant> grants = Lists.newArrayList();
		grants.add(grant);
		grants = filterUserProjectGrant(grants, inherited);
		List<String> roleids = Lists.newArrayList();
		for (UserProjectGrant g : grants) {
			roleids.add(g.getRole().getId());
		}
		if (!roleids.contains(role.getId())) {
			throw Exceptions.RoleNotFoundException.getInstance(null, roleid);
		}
		return role;
	}

	@Override
	public void createGrantByGroupDomain(String roleid, String groupid, String domainid, boolean inherited) {
		Role role = getRole(roleid);
		Domain domain = getDomain(domainid);
		GroupDomainGrant grant = null;
		boolean isNew = false;
		try {
			// _get_metadata
			grant = getGroupDomainGrant(groupid, domainid, roleid);
		} catch (Exception e) {
			grant = new GroupDomainGrant();
			grant.setDomain(domain);
			Group group = new Group();
			group.setId(groupid);
			grant.setGroup(group);
			grant.setRole(role);
			isNew = true;
		}
		// _add_role_to_role_dics
		addRoleToGroupDomainGrantMetadata(role, grant, inherited, true);

		if (isNew) {
			groupDomainGrantDao.persist(grant);
		} else {
			groupDomainGrantDao.merge(grant);
		}

		// TODO should we do this?
		// groupDomainGrantMetadataDao.persist(groupDomainGrantMetadata);
	}

	@Override
	public void createGrantByGroupProject(String roleid, String groupid, String projectid) {
		Role role = getRole(roleid);
		Project project = getProject(projectid);
		GroupProjectGrant grant = null;
		boolean isNew = false;
		try {
			// _get_metadata
			grant = getGroupProjectGrant(groupid, projectid, roleid);
		} catch (Exception e) {
			grant = new GroupProjectGrant();
			grant.setProject(project);
			Group group = new Group();
			group.setId(groupid);
			grant.setGroup(group);
			grant.setRole(role);
			isNew = true;
		}

		// _add_role_to_role_dics
		addRoleToGroupProjectGrantMetadata(role, grant, false, true);

		if (isNew) {
			groupProjectGrantDao.persist(grant);
		} else {
			groupProjectGrantDao.merge(grant);
		}

		// TODO should we do this?
		// groupProjectGrantMetadataDao.persist(groupProjectGrantMetadata);

	}

	@Override
	public void createGrantByUserDomain(String roleid, String userid, String domainid, boolean inherited) {
		Role role = getRole(roleid);
		Domain domain = getDomain(domainid);
		UserDomainGrant grant = null;
		boolean isNew = false;
		try {
			// _get_metadata
			grant = getUserDomainGrant(userid, domainid, roleid);
		} catch (Exception e) {
			grant = new UserDomainGrant();
			grant.setDomain(domain);
			User user = new User();
			user.setId(userid);
			grant.setUser(user);
			grant.setRole(role);
			isNew = true;
		}

		// _add_role_to_role_dics
		addRoleToUserDomainGrantMetadata(role, grant, inherited, true);

		if (isNew) {
			userDomainGrantDao.persist(grant);
		} else {
			userDomainGrantDao.merge(grant);
		}

		// TODO should we do this?
		// userDomainGrantMetadataDao.persist(userDomainGrantMetadata);

	}

	@Override
	public void createGrantByUserProject(String roleid, String userid, String projectid) {
		Role role = getRole(roleid);
		Project project = getProject(projectid);
		UserProjectGrant grant = null;
		boolean isNew = false;
		try {
			// _get_metadata
			grant = getUserProjectGrant(userid, projectid, roleid);
		} catch (Exception e) {
			grant = new UserProjectGrant();
			grant.setProject(project);
			User user = new User();
			user.setId(userid);
			grant.setUser(user);
			grant.setRole(role);
			isNew = true;
		}

		// _add_role_to_role_dics
		addRoleToUserProjectGrantMetadata(role, grant, false, true);

		if (isNew) {
			userProjectGrantDao.persist(grant);
		} else {
			userProjectGrantDao.merge(grant);
		}

		// TODO should we do this?
		// userProjectGrantMetadataDao.persist(userProjectGrantMetadata);

	}

	@Override
	public List<Role> listGrantsByGroupDomain(String groupid, String domainid, boolean inherited) {
		getDomain(domainid);
		List<GroupDomainGrant> grants = null;
		try {
			// _get_metadata
			grants = getGroupDomainGrants(groupid, domainid);
		} catch (Exception e) {
			grants = Lists.newArrayList();
		}
		// _roles_from_role_dics
		grants = filterGroupDomainGrant(grants, inherited);
		List<Role> roles = Lists.newArrayList();
		for (GroupDomainGrant grant : grants) {
			roles.add(grant.getRole());
		}

		return roles;
	}

	@Override
	public List<Role> listGrantsByGroupProject(String groupid, String projectid, boolean inherited) {
		getProject(projectid);
		List<GroupProjectGrant> grants = null;
		try {
			// _get_metadata
			grants = getGroupProjectGrants(groupid, projectid);
		} catch (Exception e) {
			grants = Lists.newArrayList();
		}
		// _roles_from_role_dics
		grants = filterGroupProjectGrant(grants, inherited);
		List<Role> roles = Lists.newArrayList();
		for (GroupProjectGrant grant : grants) {
			roles.add(grant.getRole());
		}
		return roles;
	}

	@Override
	public List<Role> listGrantsByUserProject(String userid, String projectid, boolean inherited) {
		getProject(projectid);
		List<UserProjectGrant> grants = null;
		try {
			// _get_metadata
			grants = getUserProjectGrants(userid, projectid);
		} catch (Exception e) {
			grants = Lists.newArrayList();
		}
		// _roles_from_role_dics
		grants = filterUserProjectGrant(grants, inherited);
		List<Role> roles = Lists.newArrayList();
		for (UserProjectGrant grant : grants) {
			roles.add(grant.getRole());
		}
		return roles;
	}

	@Override
	public List<Role> listGrantsByUserDomain(String userid, String domainid, boolean inherited) {
		getDomain(domainid);
		List<UserDomainGrant> grants = null;
		try {
			// _get_metadata
			grants = getUserDomainGrants(userid, domainid);
		} catch (Exception e) {
			grants = Lists.newArrayList();
		}
		// _roles_from_role_dics
		grants = filterUserDomainGrant(grants, inherited);
		List<Role> roles = Lists.newArrayList();
		for (UserDomainGrant grant : grants) {
			roles.add(grant.getRole());
		}
		return roles;
	}

	private Set<Domain> listDomainsWithInheritedGrantsByUser(List<UserDomainGrant> userDomainGrants) {
		Set<Domain> domains = Sets.newHashSet();
		for (UserDomainGrant userDomainGrant : userDomainGrants) {
			// for (UserDomainGrantMetadata metadata :
			// userDomainGrant.getMetadatas()) {
			if (!Strings.isNullOrEmpty(userDomainGrant.getRole().getInheritedTo())) {
				domains.add(userDomainGrant.getDomain());
			}
			// }
		}
		return domains;
	}

	private Set<Domain> listDomainsWithInheritedGrantsByGroup(List<GroupDomainGrant> groupDomainGrants) {
		Set<Domain> domains = Sets.newHashSet();
		for (GroupDomainGrant groupDomainGrant : groupDomainGrants) {
			// for (GroupDomainGrantMetadata metadata :
			// groupDomainGrant.getGroupDomainGrantMetadatas()) {
			if (!Strings.isNullOrEmpty(groupDomainGrant.getRole().getInheritedTo())) {
				domains.add(groupDomainGrant.getDomain());
			}
			// }
		}
		return domains;
	}

	// _roles_from_role_dics
	private List<GroupDomainGrant> filterGroupDomainGrant(List<GroupDomainGrant> grants, boolean inheritedToProjects) {
		List<GroupDomainGrant> ret = Lists.newArrayList();
		for (GroupDomainGrant grant : grants) {
			if ((!inheritedToProjects && Strings.isNullOrEmpty(grant.getRole().getInheritedTo()))
					|| (inheritedToProjects && PROJECTS.equals(grant.getRole().getInheritedTo()))) {
				ret.add(grant);
			}

		}
		return ret;
	}

	// _roles_from_role_dics
	private List<GroupProjectGrant> filterGroupProjectGrant(List<GroupProjectGrant> grants, boolean inheritedToProjects) {
		List<GroupProjectGrant> ret = Lists.newArrayList();
		for (GroupProjectGrant grant : grants) {
			if ((!inheritedToProjects && Strings.isNullOrEmpty(grant.getRole().getInheritedTo()))
					|| (inheritedToProjects && PROJECTS.equals(grant.getRole().getInheritedTo()))) {
				ret.add(grant);
			}

		}
		return ret;
	}

	// _roles_from_role_dics
	private List<UserDomainGrant> filterUserDomainGrant(List<UserDomainGrant> grants, boolean inheritedToProjects) {
		List<UserDomainGrant> ret = Lists.newArrayList();
		for (UserDomainGrant grant : grants) {
			if ((!inheritedToProjects && Strings.isNullOrEmpty(grant.getRole().getInheritedTo()))
					|| (inheritedToProjects && PROJECTS.equals(grant.getRole().getInheritedTo()))) {
				ret.add(grant);
			}

		}
		return ret;
	}

	// _roles_from_role_dics
	private List<UserProjectGrant> filterUserProjectGrant(List<UserProjectGrant> grants, boolean inheritedToProjects) {
		List<UserProjectGrant> ret = Lists.newArrayList();
		for (UserProjectGrant grant : grants) {
			if ((!inheritedToProjects && Strings.isNullOrEmpty(grant.getRole().getInheritedTo()))
					|| (inheritedToProjects && PROJECTS.equals(grant.getRole().getInheritedTo()))) {
				ret.add(grant);
			}

		}
		return ret;
	}

	// _add_role_to_role_dics
	private void addRoleToGroupDomainGrantMetadata(Role role, GroupDomainGrant grant, boolean inheritedToProjects,
			boolean allowExisting) {
		if (inheritedToProjects) {
			role.setInheritedTo(PROJECTS);
		}
		// GroupDomainGrantMetadata groupDomainGrantMetadata = new
		// GroupDomainGrantMetadata();
		// groupDomainGrantMetadata.setRole(role);
		// groupDomainGrantMetadata.setGrant(grant);
		if (!allowExisting) {
			// for (GroupDomainGrantMetadata metadata :
			// grant.getGroupDomainGrantMetadatas()) {
			if (!Strings.isNullOrEmpty(grant.getId())) {
				throw new IllegalArgumentException();
			}
			// }
		}

		// grant.getGroupDomainGrantMetadatas().add(groupDomainGrantMetadata);

		// return groupDomainGrantMetadata;
	}

	// _add_role_to_role_dics
	private void addRoleToGroupProjectGrantMetadata(Role role, GroupProjectGrant grant, boolean inheritedToProjects,
			boolean allowExisting) {
		if (inheritedToProjects) {
			role.setInheritedTo(PROJECTS);
		}
		// GroupProjectGrantMetadata groupProjectGrantMetadata = new
		// GroupProjectGrantMetadata();
		// groupProjectGrantMetadata.setRole(role);
		// groupProjectGrantMetadata.setGrant(grant);
		if (!allowExisting) {
			// for (GroupProjectGrantMetadata metadata : grant.getMetadatas()) {
			// if (role.getId().equals(metadata.getRole().getId())) {
			// throw new IllegalArgumentException();
			// }
			// }
			if (!Strings.isNullOrEmpty(grant.getId())) {
				throw new IllegalArgumentException();
			}
		}
		// grant.getMetadatas().add(groupProjectGrantMetadata);

		// return groupProjectGrantMetadata;
	}

	// _add_role_to_role_dics
	private void addRoleToUserDomainGrantMetadata(Role role, UserDomainGrant grant, boolean inheritedToProjects,
			boolean allowExisting) {
		if (inheritedToProjects) {
			role.setInheritedTo(PROJECTS);
		}
		// UserDomainGrantMetadata userDomainGrantMetadata = new
		// UserDomainGrantMetadata();
		// userDomainGrantMetadata.setRole(role);
		// userDomainGrantMetadata.setGrant(grant);
		if (!allowExisting) {
			// for (UserDomainGrantMetadata metadata : grant.getMetadatas()) {
			// if (role.getId().equals(metadata.getRole().getId())) {
			// throw new IllegalArgumentException();
			// }
			// }
			if (!Strings.isNullOrEmpty(grant.getId())) {
				throw new IllegalArgumentException();
			}
		}
		// grant.getMetadatas().add(userDomainGrantMetadata);

		// return userDomainGrantMetadata;
	}

	// _add_role_to_role_dics
	private void addRoleToUserProjectGrantMetadata(Role role, UserProjectGrant grant, boolean inheritedToProjects,
			boolean allowExisting) {
		if (inheritedToProjects) {
			role.setInheritedTo(PROJECTS);
		}
		// UserProjectGrantMetadata userProjectGrantMetadata = new
		// UserProjectGrantMetadata();
		// userProjectGrantMetadata.setRole(role);
		// userProjectGrantMetadata.setGrant(grant);
		if (!allowExisting) {
			// for (UserProjectGrantMetadata metadata : grant.getMetadatas()) {
			// if (role.getId().equals(metadata.getRole().getId())) {
			// throw new IllegalArgumentException("duplicate role");
			// }
			// }
			if (!Strings.isNullOrEmpty(grant.getId())) {
				throw new IllegalArgumentException();
			}
		}
		// grant.getMetadatas().add(userProjectGrantMetadata);

		// return userProjectGrantMetadata;
	}

	// // _remove_role_from_role_dics
	// private GroupProjectGrantMetadata
	// removeRoleFromGroupProjectGrantMetadata(String roleid, GroupProjectGrant
	// grant,
	// boolean inheritedToProjects) {
	// for (GroupProjectGrantMetadata ref : grant.getMetadatas()) {
	// if (roleid.equals(ref.getRole().getId())) {
	// grant.getMetadatas().remove(ref);
	// return ref;
	// }
	// }
	// // KeyError
	// throw new IllegalArgumentException();
	// }

	// _remove_role_from_role_dics
	// private GroupDomainGrantMetadata
	// removeRoleFromGroupDomainGrantMetadata(String roleid, GroupDomainGrant
	// grant,
	// boolean inheritedToProjects) {
	// for (GroupDomainGrantMetadata ref : grant.getGroupDomainGrantMetadatas())
	// {
	// if (roleid.equals(ref.getRole().getId())) {
	// grant.getGroupDomainGrantMetadatas().remove(ref);
	// return ref;
	// }
	// }
	// // KeyError
	// throw new IllegalArgumentException();
	// }

	// // _remove_role_from_role_dics
	// private UserProjectGrantMetadata
	// removeRoleFromUserProjectGrantMetadata(String roleid, UserProjectGrant
	// grant,
	// boolean inheritedToProjects) {
	// for (UserProjectGrantMetadata ref : grant.getMetadatas()) {
	// if (roleid.equals(ref.getRole().getId())) {
	// grant.getMetadatas().remove(ref);
	// return ref;
	// }
	// }
	// // KeyError
	// throw new IllegalArgumentException();
	// }

	// // _remove_role_from_role_dics
	// private UserDomainGrantMetadata
	// removeRoleFromUserDomainGrantMetadata(String roleid, UserDomainGrant
	// grant,
	// boolean inheritedToProjects) {
	// for (UserDomainGrantMetadata ref : grant.getMetadatas()) {
	// if (roleid.equals(ref.getRole().getId())) {
	// grant.getMetadatas().remove(ref);
	// return ref;
	// }
	// }
	// // KeyError
	// throw new IllegalArgumentException();
	// }
}
