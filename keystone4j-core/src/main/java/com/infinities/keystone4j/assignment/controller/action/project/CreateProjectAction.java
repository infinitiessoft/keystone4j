package com.infinities.keystone4j.assignment.controller.action.project;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class CreateProjectAction extends AbstractProjectAction implements ProtectedAction<Project> {

	// private final static Logger logger =
	// LoggerFactory.getLogger(CreateProjectAction.class);
	private final Project project;


	public CreateProjectAction(AssignmentApi assignmentApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi,
			Project project) {
		super(assignmentApi, tokenProviderApi, policyApi);
		this.project = project;
	}

	@Override
	public MemberWrapper<Project> execute(ContainerRequestContext request) throws Exception {
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		assignUniqueId(project);
		normalizeDomainid(context, project);
		Project ref = assignmentApi.createProject(project.getId(), project);
		return this.wrapMember(request, ref);
	}

	@Override
	public String getName() {
		return "create_project";
	}
}
