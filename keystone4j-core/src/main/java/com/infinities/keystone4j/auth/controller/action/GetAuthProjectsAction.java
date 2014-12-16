package com.infinities.keystone4j.auth.controller.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.infinities.keystone4j.AbstractAction;
import com.infinities.keystone4j.FilterProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.controller.action.project.AbstractProjectAction;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.common.Authorization;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class GetAuthProjectsAction extends AbstractAuthAction implements FilterProtectedAction<Project> {

	private final static Logger logger = LoggerFactory.getLogger(GetAuthProjectsAction.class);


	public GetAuthProjectsAction(AssignmentApi assignmentApi, CatalogApi catalogApi, IdentityApi identityApi,
			TokenProviderApi tokenProviderApi, PolicyApi policyApi) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		super(assignmentApi, catalogApi, identityApi, tokenProviderApi, policyApi);
	}

	@Override
	public CollectionWrapper<Project> execute(ContainerRequestContext context, String... filters) throws Exception {
		Authorization.AuthContext authContext = AbstractAction.getAuthContext(context);
		String userid = authContext.getUserid();
		List<Project> userRefs = new ArrayList<Project>();
		if (!Strings.isNullOrEmpty(userid)) {
			try {
				userRefs = this.assignmentApi.listProjectsForUser(userid, null);
			} catch (Exception e) {
				logger.debug("ignore", e);
			}
		}

		List<String> groupids = authContext.getGroupIds();
		List<Project> groupRefs = new ArrayList<Project>();
		if (groupids != null && !groupids.isEmpty()) {
			groupRefs = this.assignmentApi.listProjectsForGroups(groupids);
		}

		List<Project> refs = combineListsUniquely(userRefs, groupRefs);

		return new AbstractProjectAction(assignmentApi, tokenProviderApi, policyApi) {

		}.wrapCollection(context, refs);
	}

	private List<Project> combineListsUniquely(List<Project> a, List<Project> b) {
		if (a != null && b != null) {
			Map<String, Project> map = new HashMap<String, Project>();
			for (Project p : a) {
				map.put(p.getId(), p);
			}
			for (Project p : b) {
				map.put(p.getId(), p);
			}
			return Lists.newArrayList(map.values());
		} else if (a != null) {
			return a;
		} else {
			return b;
		}
	}

	@Override
	public String getName() {
		return "get_auth_projects";
	}

}
