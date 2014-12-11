package com.infinities.keystone4j.assignment.controller.action.roleassignment;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.infinities.keystone4j.AbstractAction;
import com.infinities.keystone4j.FilterProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.assignment.Assignment;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.assignment.RoleAssignment;
import com.infinities.keystone4j.model.assignment.RoleAssignment.DomainScope;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.utils.TextUtils;

public class ListRoleAssignmentsAction extends AbstractRoleAssignmentAction implements FilterProtectedAction<RoleAssignment> {

	private final static Logger logger = LoggerFactory.getLogger(ListRoleAssignmentsAction.class);


	public ListRoleAssignmentsAction(AssignmentApi assignmentApi, IdentityApi identityApi) {
		super(assignmentApi, identityApi);
	}

	@Override
	public CollectionWrapper<RoleAssignment> execute(ContainerRequestContext request, String... filters) throws Exception {
		Hints hints = AbstractAction.buildDriverHints(request, filters);
		List<Assignment> refs = assignmentApi.listRoleAssignments();

		List<RoleAssignment> formattedRefs = new ArrayList<RoleAssignment>();

		for (Assignment ref : refs) {
			if (filterInherited(ref)) {
				formattedRefs.add(formatEntity(request, ref));
			}
		}

		if (request.getUriInfo().getQueryParameters().containsKey("effective")
				&& AbstractAction.queryFilterIsTrue(request.getUriInfo().getQueryParameters().getFirst("effective"))) {
			formattedRefs = expandIndirectAssignments(request, formattedRefs);
		}

		return this.wrapCollection(request, formattedRefs, hints);
	}

	private List<RoleAssignment> expandIndirectAssignments(ContainerRequestContext request, List<RoleAssignment> refs) {
		List<RoleAssignment> newRefs = new ArrayList<RoleAssignment>();
		for (RoleAssignment ref : refs) {
			if (!Strings.isNullOrEmpty(ref.getScope().getInheritedTo())) {
				List<String> projectids = new ArrayList<String>();
				RoleAssignment.DomainScope domainScope = (DomainScope) (ref.getScope());
				String domainid = domainScope.getDomain().getId();
				for (Project project : this.assignmentApi.listProjectsInDomain(domainid)) {
					projectids.add(project.getId());
				}

				for (String p : projectids) {
					if (ref.getGroup() != null) {
						List<User> members = getGroupMembers(ref);
						String groupid = ref.getGroup().getId();

						for (User m : members) {
							RoleAssignment newEntry = buildProjectEquivalentOfGroupDomainRole(request, m.getId(), groupid,
									p, domainid, ref);
							newRefs.add(newEntry);
						}
					} else {
						RoleAssignment newEntry = buildProjectEquivalentOfUserDomainRole(request, p, domainid, ref);
						newRefs.add(newEntry);
					}
				}

			} else if (ref.getGroup() != null) {
				List<User> members = getGroupMembers(ref);
				String groupid = ref.getGroup().getId();
				for (User m : members) {
					RoleAssignment newEntry = buildUserAssignmentEquivalentOfGroup(request, m, groupid, ref);
					newRefs.add(newEntry);
				}
			} else {
				newRefs.add(ref);
			}
		}
		return refs;
	}

	private RoleAssignment buildUserAssignmentEquivalentOfGroup(ContainerRequestContext request, User user, String groupid,
			RoleAssignment template) {
		RoleAssignment userEntry = new RoleAssignment();
		userEntry.setId(template.getId());
		userEntry.setRole(template.getRole());
		userEntry.setScope(template.getScope());

		RoleAssignment.User u = new RoleAssignment.User();
		u.setId(user.getId());
		userEntry.setUser(u);

		userEntry.getLinks().setMembership(getBaseUrl(request, String.format("/groups/%s/users/%s", groupid, user.getId())));

		return userEntry;
	}

	private RoleAssignment buildProjectEquivalentOfUserDomainRole(ContainerRequestContext request, String projectid,
			String domainid, RoleAssignment template) {
		RoleAssignment projectEntry = new RoleAssignment();
		projectEntry.setId(template.getId());
		projectEntry.setUser(template.getUser());
		projectEntry.setRole(template.getRole());
		RoleAssignment.ProjectScope scope = new RoleAssignment.ProjectScope();
		scope.getProject().setId(projectid);
		projectEntry.setScope(scope);
		projectEntry.getLinks().setAssignment(
				getBaseUrl(request, String.format("/OS-INHERIT/domains/%s/users/%s/roles/%s/inherited_to_projects",
						domainid, projectEntry.getUser().getId(), projectEntry.getRole().getId())));

		return projectEntry;
	}

	private RoleAssignment buildProjectEquivalentOfGroupDomainRole(ContainerRequestContext request, String userid,
			String groupid, String projectid, String domainid, RoleAssignment template) {
		RoleAssignment projectEntry = new RoleAssignment();
		projectEntry.setId(template.getId());
		projectEntry.setRole(template.getRole());
		RoleAssignment.User user = new RoleAssignment.User();
		user.setId(userid);
		projectEntry.setUser(user);
		RoleAssignment.ProjectScope scope = new RoleAssignment.ProjectScope();
		scope.getProject().setId(projectid);
		projectEntry.setScope(scope);

		projectEntry.getLinks().setAssignment(
				getBaseUrl(request, String.format("/OS-INHERIT/domains/%s/groups/%s/roles/%s/inherited_to_projects",
						domainid, groupid, projectEntry.getRole().getId())));

		projectEntry.getLinks().setMembership(getBaseUrl(request, String.format("/groups/%s/users/%s", groupid, userid)));

		return projectEntry;
	}

	private List<User> getGroupMembers(RoleAssignment ref) {
		List<User> members;
		try {
			members = identityApi.listUsersInGroup(ref.getGroup().getId(), null);
		} catch (Exception e) {
			members = new ArrayList<User>();
			String target = "Unknown";

			if (ref.getScope() != null) {
				if (ref.getScope() instanceof RoleAssignment.DomainScope) {
					String domid = TextUtils.get(((RoleAssignment.DomainScope) ref.getScope()).getDomain().getId(),
							"Unknown");
					target = String.format("Domain: %s", domid);
				} else if (ref.getScope() instanceof RoleAssignment.ProjectScope) {
					String projid = TextUtils.get(((RoleAssignment.ProjectScope) ref.getScope()).getProject().getId(),
							"Unknown");
					target = String.format("Project: %s", projid);
				}
			}

			String roleid = "Unknown";
			if (ref.getRole() != null && !Strings.isNullOrEmpty(ref.getRole().getId())) {
				roleid = ref.getRole().getId();
			}
			logger.warn("Group {} not found for role-assignment - {} with Role: {}", new Object[] { ref.getGroup().getId(),
					target, roleid });
		}
		return members;
	}

	private RoleAssignment formatEntity(ContainerRequestContext context, Assignment entity) {
		String suffix = "";
		String actorLink = "";
		String targetLink = "";
		RoleAssignment formattedEntity = new RoleAssignment();
		if (entity.getUser() != null) {
			RoleAssignment.User user = new RoleAssignment.User();
			user.setId(entity.getUser().getId());
			formattedEntity.setUser(user);
			actorLink = String.format("users/%s", entity.getUser().getId());
		}
		if (entity.getGroup() != null) {
			RoleAssignment.Group group = new RoleAssignment.Group();
			group.setId(entity.getGroup().getId());
			formattedEntity.setGroup(group);
			actorLink = String.format("groups/%s", entity.getGroup().getId());
		}
		if (entity.getRole() != null) {
			RoleAssignment.Role role = new RoleAssignment.Role();
			role.setId(entity.getRole().getId());
			formattedEntity.setRole(role);
		}
		if (entity.getProject() != null) {
			RoleAssignment.ProjectScope scope = new RoleAssignment.ProjectScope();
			scope.getProject().setId(entity.getProject().getId());
			formattedEntity.setScope(scope);
			targetLink = String.format("/projects/%s", entity.getProject().getId());
		}
		if (entity.getDomain() != null) {
			RoleAssignment.DomainScope scope = new RoleAssignment.DomainScope();
			scope.getDomain().setId(entity.getDomain().getId());
			formattedEntity.setScope(scope);
			if (entity.getInheritedToProjects() != null) {
				formattedEntity.getScope().setInheritedTo("projects");
				targetLink = String.format("/OS-INHERIT/domain/%s", entity.getDomain().getId());
				suffix = "/inherited_to_projects";
			} else {
				targetLink = String.format("/domain/%s", entity.getDomain().getId());
			}
		}
		String path = String.format("%s/%s/roles/%s/%s", targetLink, actorLink, entity.getRole().getId(), suffix);
		formattedEntity.getLinks().setAssignment(getBaseUrl(context, path));

		return formattedEntity;
	}

	private boolean filterInherited(Assignment entry) {
		if (entry.getInheritedToProjects() != null && !Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean()) {
			return false;
		}
		return true;
	}

	@Override
	public String getName() {
		return "list_role_assignments";
	}

}
