package com.infinities.keystone4j.assignment.controller.action.project;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class GetProjectAction extends AbstractProjectAction implements ProtectedAction<Project> {

	private final String projectid;


	public GetProjectAction(AssignmentApi assignmentApi, TokenProviderApi tokenProviderApi, String projectid) {
		super(assignmentApi, tokenProviderApi);
		this.projectid = projectid;
	}

	@Override
	public MemberWrapper<Project> execute(ContainerRequestContext context) {
		Project ref = this.getAssignmentApi().getProject(projectid);
		expandProjectRef(context, ref);
		return this.wrapMember(context, ref);
	}

	private void expandProjectRef(ContainerRequestContext context, Project ref) {
		String userid = getAuthContext(context).getUserid();
		if (context.getUriInfo().getQueryParameters().containsKey("parents_as_list")
				&& queryFilterIsTrue(context.getUriInfo().getQueryParameters().getFirst("parents_as_list"))) {
			List<Project> parents = assignmentApi.listProjectsParents(ref.getId(), userid);
			for (Project p : parents) {
				ref.getParents().add(wrapMember(context, p));
			}
		}

		if (context.getUriInfo().getQueryParameters().containsKey("subtree_as_list")
				&& queryFilterIsTrue(context.getUriInfo().getQueryParameters().getFirst("subtree_as_list"))) {
			List<Project> subtree = assignmentApi.listProjectsInSubtree(ref.getId(), userid);
			for (Project p : subtree) {
				ref.getSubtree().add(wrapMember(context, p));
			}
		}
	}

	@Override
	public String getName() {
		return "get_project";
	}
}
