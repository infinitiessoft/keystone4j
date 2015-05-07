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
import com.infinities.keystone4j.model.assignment.FormattedRoleAssignment;
import com.infinities.keystone4j.model.assignment.FormattedRoleAssignment.DomainScope;
import com.infinities.keystone4j.model.assignment.FormattedRoleAssignment.ProjectScope;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.utils.TextUtils;

public class ListRoleAssignmentsAction extends AbstractRoleAssignmentAction implements
		FilterProtectedAction<FormattedRoleAssignment> {

	private final static Logger logger = LoggerFactory.getLogger(ListRoleAssignmentsAction.class);


	public ListRoleAssignmentsAction(AssignmentApi assignmentApi, IdentityApi identityApi) {
		super(assignmentApi, identityApi);
	}

	@Override
	public CollectionWrapper<FormattedRoleAssignment> execute(ContainerRequestContext request, String... filters)
			throws Exception {
		Hints hints = AbstractAction.buildDriverHints(request, filters);
		List<Assignment> refs = assignmentApi.listRoleAssignments();

		List<FormattedRoleAssignment> formattedRefs = new ArrayList<FormattedRoleAssignment>();

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

	private List<FormattedRoleAssignment> expandIndirectAssignments(ContainerRequestContext request,
			List<FormattedRoleAssignment> refs) throws Exception {
		List<FormattedRoleAssignment> newRefs = new ArrayList<FormattedRoleAssignment>();
		for (FormattedRoleAssignment ref : refs) {
			if (!Strings.isNullOrEmpty(ref.getScope().getInheritedTo())) {
				List<String> projectids = new ArrayList<String>();
				String targetType;
				String targetId;
				String domainId;
				String projectId;
				if (ref.getScope() instanceof FormattedRoleAssignment.DomainScope) {

					FormattedRoleAssignment.DomainScope domainScope = (DomainScope) (ref.getScope());
					domainId = domainScope.getDomain().getId();
					for (Project project : this.assignmentApi.listProjectsInDomain(domainId)) {
						projectids.add(project.getId());
					}
					targetType = "domains";
					targetId = domainId;
				} else {
					FormattedRoleAssignment.ProjectScope projectScope = (ProjectScope) (ref.getScope());
					projectId = projectScope.getProject().getId();
					for (Project project : this.assignmentApi.listProjectsInSubtree(projectId, null)) {
						projectids.add(project.getId());
					}
					targetType = "projects";
					targetId = projectId;
				}

				for (String p : projectids) {
					if (ref.getGroup() != null) {
						List<User> members = getGroupMembers(ref);
						String groupid = ref.getGroup().getId();

						for (User m : members) {
							FormattedRoleAssignment newEntry = buildProjectEquivalentOfGroupTargetRole(request, m.getId(),
									groupid, p, targetId, targetType, ref);
							newRefs.add(newEntry);
						}
					} else {
						FormattedRoleAssignment newEntry = buildProjectEquivalentOfUserTargetRole(request, p, targetId,
								targetType, ref);
						newRefs.add(newEntry);
					}
				}

			} else if (ref.getGroup() != null) {
				List<User> members = getGroupMembers(ref);
				String groupid = ref.getGroup().getId();
				for (User m : members) {
					FormattedRoleAssignment newEntry = buildUserAssignmentEquivalentOfGroup(request, m, groupid, ref);
					newRefs.add(newEntry);
				}
			} else {
				newRefs.add(ref);
			}
		}
		return refs;
	}

	private FormattedRoleAssignment buildUserAssignmentEquivalentOfGroup(ContainerRequestContext request, User user,
			String groupid, FormattedRoleAssignment template) {
		FormattedRoleAssignment userEntry = new FormattedRoleAssignment();
		userEntry.setRole(template.getRole());
		userEntry.setScope(template.getScope());
		FormattedRoleAssignment.User u = new FormattedRoleAssignment.User();
		u.setId(user.getId());
		userEntry.setUser(u);
		userEntry.getLinks().setMembership(getBaseUrl(request, String.format("/groups/%s/users/%s", groupid, user.getId())));

		return userEntry;
	}

	private FormattedRoleAssignment buildProjectEquivalentOfUserTargetRole(ContainerRequestContext request,
			String projectid, String targetid, String targetType, FormattedRoleAssignment template) {
		FormattedRoleAssignment projectEntry = new FormattedRoleAssignment();
		projectEntry.setUser(template.getUser());
		projectEntry.setRole(template.getRole());
		FormattedRoleAssignment.ProjectScope scope = new FormattedRoleAssignment.ProjectScope();
		scope.getProject().setId(projectid);
		projectEntry.setScope(scope);
		projectEntry.getLinks().setAssignment(
				getBaseUrl(request, String.format("/OS-INHERIT/%s/%s/users/%s/roles/%s/inherited_to_projects", targetType,
						targetid, projectEntry.getUser().getId(), projectEntry.getRole().getId())));

		return projectEntry;
	}

	private FormattedRoleAssignment buildProjectEquivalentOfGroupTargetRole(ContainerRequestContext request, String userid,
			String groupid, String projectid, String targetId, String targetType, FormattedRoleAssignment template) {
		FormattedRoleAssignment projectEntry = new FormattedRoleAssignment();
		projectEntry.setRole(template.getRole());
		FormattedRoleAssignment.User user = new FormattedRoleAssignment.User();
		user.setId(userid);
		projectEntry.setUser(user);
		FormattedRoleAssignment.ProjectScope scope = new FormattedRoleAssignment.ProjectScope();
		scope.getProject().setId(projectid);
		projectEntry.setScope(scope);

		projectEntry.getLinks().setAssignment(
				getBaseUrl(request, String.format("/OS-INHERIT/%s/%s/groups/%s/roles/%s/inherited_to_projects", targetType,
						targetId, groupid, projectEntry.getRole().getId())));

		projectEntry.getLinks().setMembership(getBaseUrl(request, String.format("/groups/%s/users/%s", groupid, userid)));

		return projectEntry;
	}

	private List<User> getGroupMembers(FormattedRoleAssignment ref) {
		List<User> members;
		try {
			members = identityApi.listUsersInGroup(ref.getGroup().getId(), null);
		} catch (Exception e) {
			members = new ArrayList<User>();
			String target = "Unknown";

			if (ref.getScope() != null) {
				if (ref.getScope() instanceof FormattedRoleAssignment.DomainScope) {
					String domid = TextUtils.get(((FormattedRoleAssignment.DomainScope) ref.getScope()).getDomain().getId(),
							"Unknown");
					target = String.format("Domain: %s", domid);
				} else if (ref.getScope() instanceof FormattedRoleAssignment.ProjectScope) {
					String projid = TextUtils.get(((FormattedRoleAssignment.ProjectScope) ref.getScope()).getProject()
							.getId(), "Unknown");
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

	private FormattedRoleAssignment formatEntity(ContainerRequestContext context, Assignment entity) {
		String suffix = "";
		String actorLink = "";
		String targetLink = "";
		FormattedRoleAssignment formattedEntity = new FormattedRoleAssignment();
		if (!Strings.isNullOrEmpty(entity.getUserId())) {
			FormattedRoleAssignment.User user = new FormattedRoleAssignment.User();
			user.setId(entity.getUserId());
			formattedEntity.setUser(user);
			actorLink = String.format("users/%s", entity.getUserId());
		}
		if (!Strings.isNullOrEmpty(entity.getGroupId())) {
			FormattedRoleAssignment.Group group = new FormattedRoleAssignment.Group();
			group.setId(entity.getGroupId());
			formattedEntity.setGroup(group);
			actorLink = String.format("groups/%s", entity.getGroupId());
		}
		if (!Strings.isNullOrEmpty(entity.getRoleId())) {
			FormattedRoleAssignment.Role role = new FormattedRoleAssignment.Role();
			role.setId(entity.getRoleId());
			formattedEntity.setRole(role);
		}
		if (!Strings.isNullOrEmpty(entity.getProjectId())) {
			FormattedRoleAssignment.ProjectScope scope = new FormattedRoleAssignment.ProjectScope();
			scope.getProject().setId(entity.getProjectId());
			formattedEntity.setScope(scope);
			if (!Strings.isNullOrEmpty(entity.getInheritedToProjects())) {
				scope.setInheritedTo("projects");
				targetLink = String.format("/OS-INHERIT/projects/%s", entity.getProjectId());
				suffix = "/inherited_to_projects";
			} else {
				targetLink = String.format("/projects/%s", entity.getProjectId());
			}
		}
		if (!Strings.isNullOrEmpty(entity.getDomainId())) {
			FormattedRoleAssignment.DomainScope scope = new FormattedRoleAssignment.DomainScope();
			scope.getDomain().setId(entity.getDomainId());
			formattedEntity.setScope(scope);
			if (entity.getInheritedToProjects() != null) {
				formattedEntity.getScope().setInheritedTo("projects");
				targetLink = String.format("/OS-INHERIT/domains/%s", entity.getDomainId());
				suffix = "/inherited_to_projects";
			} else {
				targetLink = String.format("/domains/%s", entity.getDomainId());
			}
		}
		String path = String.format("%s/%s/roles/%s/%s", targetLink, actorLink, entity.getRoleId(), suffix);
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
