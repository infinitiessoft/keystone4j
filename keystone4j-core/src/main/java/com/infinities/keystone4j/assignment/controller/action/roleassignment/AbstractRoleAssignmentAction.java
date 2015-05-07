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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.ws.rs.container.ContainerRequestContext;

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
import com.infinities.keystone4j.model.assignment.FormattedRoleAssignment;
import com.infinities.keystone4j.model.assignment.RoleAssignment;
import com.infinities.keystone4j.model.assignment.wrapper.RoleAssignmentWrapper;
import com.infinities.keystone4j.model.assignment.wrapper.RoleAssignmentsWrapper;
import com.infinities.keystone4j.utils.ReflectUtils;

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

	protected CollectionWrapper<FormattedRoleAssignment> getCollectionWrapper() {
		return new RoleAssignmentsWrapper();
	}

	protected MemberWrapper<RoleAssignment> getMemberWrapper() {
		return new RoleAssignmentWrapper();
	}

	protected MemberWrapper<FormattedRoleAssignment> wrapMember(ContainerRequestContext context, FormattedRoleAssignment ref) {
		// addSelfReferentialLink(context, ref);
		// MemberWrapper<Assignment> wrapper = getMemberWrapper();
		// wrapper.setRef(ref);
		// return wrapper;
		return null;
	}

	protected CollectionWrapper<FormattedRoleAssignment> wrapCollection(ContainerRequestContext context,
			List<FormattedRoleAssignment> refs, Hints hints) {
		if (hints != null) {
			refs = filterByAttributes(refs, hints);
		}

		Entry<Boolean, List<FormattedRoleAssignment>> entry = limit(refs, hints);
		Boolean listLimited = entry.getKey();
		refs = entry.getValue();

		for (FormattedRoleAssignment ref : refs) {
			wrapMember(context, ref);
		}

		CollectionWrapper<FormattedRoleAssignment> container = getCollectionWrapper();
		container.setRefs(refs);
		container.getLinks().setSelf(getFullUrl(context, context.getUriInfo().getPath().replace("/v3", "")));

		if (listLimited) {
			container.setTruncated(true);
		}

		return container;
	}

	private String getFullUrl(ContainerRequestContext context, String path) {
		String url = getBaseUrl(context, path);
		String queryStr = context.getUriInfo().getRequestUri().getRawQuery();
		if (!Strings.isNullOrEmpty(queryStr)) {
			url = String.format("%s?%s", url, queryStr);
		}

		return url;
	}

	private Entry<Boolean, List<FormattedRoleAssignment>> limit(List<FormattedRoleAssignment> refs, Hints hints) {
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

	private List<FormattedRoleAssignment> filterByAttributes(List<FormattedRoleAssignment> refs, Hints hints) {
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

		String ret = String.format("%s/%s/%s", endpoint, "v3", StringUtils.removeStart(path, "/"));
		if (ret.endsWith("/")) {
			ret = ret.substring(0, ret.length() - 1);
		}
		return ret;
	}


	private class AttrMatchFunction implements Function<List<FormattedRoleAssignment>, List<FormattedRoleAssignment>> {

		private final String attr;
		private final Object value;


		private AttrMatchFunction(String attr, Object value) {
			this.attr = attr;
			this.value = value;
		}

		@Override
		public List<FormattedRoleAssignment> apply(List<FormattedRoleAssignment> input) {
			List<FormattedRoleAssignment> output = new ArrayList<FormattedRoleAssignment>();
			for (FormattedRoleAssignment t : input) {
				try {
					if (attrMatch(ReflectUtils.reflact(t, attr), value)) {
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
