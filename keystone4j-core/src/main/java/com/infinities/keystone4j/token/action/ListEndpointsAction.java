package com.infinities.keystone4j.token.action;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.catalog.Catalog;
import com.infinities.keystone4j.model.catalog.Endpoint;
import com.infinities.keystone4j.model.common.Link;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.model.token.v2.Access;
import com.infinities.keystone4j.model.token.v2.EndpointsV2Wrapper;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;
import com.infinities.keystone4j.utils.KeystoneUtils;

public class ListEndpointsAction extends AbstractTokenAction<EndpointsV2Wrapper> {

	// private final static Logger logger =
	// LoggerFactory.getLogger(GetRevocationListAction.class);
	private final String tokenid;
	private final PolicyApi policyApi;


	public ListEndpointsAction(AssignmentApi assignmentApi, CatalogApi catalogApi, IdentityApi identityApi,
			TokenApi tokenApi, TokenProviderApi tokenProviderApi, TrustApi trustApi, PolicyApi policyApi, String tokenid) {
		super(assignmentApi, catalogApi, identityApi, tokenApi, tokenProviderApi, trustApi);
		this.tokenid = tokenid;
		this.policyApi = policyApi;
	}

	@Override
	public EndpointsV2Wrapper execute(ContainerRequestContext request) {
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		new KeystoneUtils().assertAdmin(policyApi, tokenApi, context);
		Token tokenRef = tokenApi.getToken(tokenid);
		Catalog catalogRef = null;
		if (tokenRef.getProject() != null) {
			catalogRef = catalogApi.getV3Catalog(tokenRef.getUser().getId(), tokenRef.getProject().getId());
		}

		return formatEndpointList(catalogRef);
	}

	private EndpointsV2Wrapper formatEndpointList(Catalog catalogRef) {
		if (catalogRef == null) {
			return new EndpointsV2Wrapper();
		}

		EndpointsV2Wrapper endpoints = new EndpointsV2Wrapper();
		List<Access.Service> services = new ArrayList<Access.Service>();
		for (com.infinities.keystone4j.model.catalog.Service service : catalogRef.getServices()) {
			Access.Service s = new Access.Service();
			s.setName(service.getName());
			s.setType(service.getType());
			s.setEndpointsLinks(new ArrayList<Link>());
			Access.Service.Endpoint e = new Access.Service.Endpoint();
			for (Endpoint endpoint : service.getEndpoints()) {
				e.setRegion(endpoint.getRegion());
				if (endpoint.getInterfaceType().toLowerCase().contains("public")) {
					e.setPublicURL(endpoint.getUrl());
				}
				if (endpoint.getInterfaceType().toLowerCase().contains("internal")) {
					e.setInternalURL(endpoint.getUrl());
				}
				if (endpoint.getInterfaceType().toLowerCase().contains("admin")) {
					e.setAdminURL(endpoint.getUrl());
				}
			}
			s.getEndpoints().add(e);
		}
		endpoints.setEndpoints(services);
		return endpoints;
	}

	@Override
	public String getName() {
		return "endpoints";
	}
}
