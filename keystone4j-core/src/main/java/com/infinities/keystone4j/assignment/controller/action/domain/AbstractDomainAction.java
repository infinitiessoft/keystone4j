package com.infinities.keystone4j.assignment.controller.action.domain;

import com.infinities.keystone4j.AbstractAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.wrapper.DomainWrapper;
import com.infinities.keystone4j.model.assignment.wrapper.DomainsWrapper;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public abstract class AbstractDomainAction extends AbstractAction<Domain> {

	// private final static Logger logger =
	// LoggerFactory.getLogger(AbstractDomainAction.class);
	protected AssignmentApi assignmentApi;


	public AbstractDomainAction(AssignmentApi assignmentApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		super(tokenProviderApi, policyApi);
		this.assignmentApi = assignmentApi;
	}

	public AssignmentApi getAssignmentApi() {
		return assignmentApi;
	}

	public void setAssignmentApi(AssignmentApi assignmentApi) {
		this.assignmentApi = assignmentApi;
	}

	@Override
	public CollectionWrapper<Domain> getCollectionWrapper() {
		return new DomainsWrapper();
	}

	@Override
	public MemberWrapper<Domain> getMemberWrapper() {
		return new DomainWrapper();
	}

	@Override
	public String getCollectionName() {
		return "domains";
	}

	@Override
	public String getMemberName() {
		return "domain";
	}

}
