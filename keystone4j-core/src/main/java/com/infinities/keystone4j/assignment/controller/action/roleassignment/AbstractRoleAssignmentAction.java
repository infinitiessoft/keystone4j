package com.infinities.keystone4j.assignment.controller.action.roleassignment;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.ws.rs.container.ContainerRequestContext;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.common.Hints.Filter;
import com.infinities.keystone4j.common.Wsgi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.RoleAssignment;
import com.infinities.keystone4j.model.assignment.RoleAssignmentWrapper;
import com.infinities.keystone4j.model.assignment.RoleAssignmentsWrapper;

public abstract class AbstractRoleAssignmentAction {

	// private final static Logger logger =
	// LoggerFactory.getLogger(AbstractDomainAction.class);
	protected AssignmentApi assignmentApi;
	protected IdentityApi identityApi;
	protected Method getMemberFromDriver;


	public AbstractRoleAssignmentAction(AssignmentApi assignmentApi, IdentityApi identityApi) {
		this.assignmentApi = assignmentApi;
		this.identityApi = identityApi;
	}

	public AssignmentApi getAssignmentApi() {
		return assignmentApi;
	}

	public void setAssignmentApi(AssignmentApi assignmentApi) {
		this.assignmentApi = assignmentApi;
	}

	public IdentityApi getIdentityApi() {
		return identityApi;
	}

	public void setIdentityApi(IdentityApi identityApi) {
		this.identityApi = identityApi;
	}

	protected CollectionWrapper<RoleAssignment> getCollectionWrapper() {
		return new RoleAssignmentsWrapper();
	}

	protected MemberWrapper<RoleAssignment> getMemberWrapper() {
		return new RoleAssignmentWrapper();
	}

	protected MemberWrapper<RoleAssignment> wrapMember(ContainerRequestContext context, RoleAssignment ref) {
		// addSelfReferentialLink(context, ref);
		// MemberWrapper<Assignment> wrapper = getMemberWrapper();
		// wrapper.setRef(ref);
		// return wrapper;
		return null;
	}

	protected CollectionWrapper<RoleAssignment> wrapCollection(ContainerRequestContext context, List<RoleAssignment> refs,
			Hints hints) {
		if (hints != null) {
			refs = filterByAttributes(refs, hints);
		}

		Entry<Boolean, List<RoleAssignment>> entry = limit(refs, hints);
		Boolean listLimited = entry.getKey();
		refs = entry.getValue();

		for (RoleAssignment ref : refs) {
			wrapMember(context, ref);
		}

		CollectionWrapper<RoleAssignment> container = getCollectionWrapper();
		container.getLinks().setSelf(getFullUrl(context, context.getUriInfo().getPath()));

		if (listLimited) {
			container.setTruncated(true);
		}

		return container;
	}

	private String getFullUrl(ContainerRequestContext context, String path) {
		String url = getBaseUrl(context, path);
		String queryStr = context.getUriInfo().getRequestUri().getRawQuery();
		if (Strings.isNullOrEmpty(queryStr)) {
			url = String.format("%s?%s", url, queryStr);
		}
		return url;
	}

	private Entry<Boolean, List<RoleAssignment>> limit(List<RoleAssignment> refs, Hints hints) {
		boolean NOT_LIMITED = false;
		boolean LIMITED = true;

		if (hints == null || hints.getLimit() == null) {
			return Maps.immutableEntry(NOT_LIMITED, refs);
		}

		if (hints.getLimit().isTruncated()) {
			return Maps.immutableEntry(LIMITED, refs);
		}

		if (refs.size() > hints.getLimit().getLimit()) {
			return Maps.immutableEntry(LIMITED, refs.subList(0, hints.getLimit().getLimit()));
		}

		return Maps.immutableEntry(NOT_LIMITED, refs);
	}

	private List<RoleAssignment> filterByAttributes(List<RoleAssignment> refs, Hints hints) {
		for (Filter filter : hints.getFilters()) {
			if ("equals".equals(filter.getComparator())) {
				String attr = filter.getName();
				Object value = filter.getValue();
				AttrMatchFunction attrMatchFunction = new AttrMatchFunction(attr, value);
				refs = attrMatchFunction.apply(refs);
			}
			// TODO ignore inexact filter used by query_string

		}
		return refs;
	}

	public String getCollectionName() {
		return "role_assignments";
	}

	public String getMemberName() {
		return "role_assignment";
	}

	protected String getBaseUrl(ContainerRequestContext context, String path) {
		String endpoint = Wsgi.getBaseUrl(context, "public");
		if (Strings.isNullOrEmpty(path)) {
			path = getCollectionName();
		}
		return String.format("%s/%s/s", endpoint, "v3", StringUtils.removeStart(path, "/"));
	}


	private class AttrMatchFunction implements Function<List<RoleAssignment>, List<RoleAssignment>> {

		private final String attr;
		private final Object value;


		private AttrMatchFunction(String attr, Object value) {
			this.attr = attr;
			this.value = value;
		}

		@Override
		public List<RoleAssignment> apply(List<RoleAssignment> input) {
			List<RoleAssignment> output = new ArrayList<RoleAssignment>();
			for (RoleAssignment t : input) {
				try {
					if (attrMatch(PropertyUtils.getProperty(t, attr), value)) {
						output.add(t);
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			return output;
		}

		private boolean attrMatch(Object refAttr, Object valAttr) {
			if (refAttr instanceof Boolean) {
				return valAttr == refAttr;
			} else {
				return valAttr.equals(refAttr);
			}
		}
	}

}
