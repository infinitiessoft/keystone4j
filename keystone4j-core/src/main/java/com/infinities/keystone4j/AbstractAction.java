package com.infinities.keystone4j;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.infinities.keystone4j.common.Authorization;
import com.infinities.keystone4j.common.Authorization.AuthContext;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.common.Hints.Filter;
import com.infinities.keystone4j.common.Wsgi;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.BaseEntity;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.DomainAwared;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.common.MemberLinks;
import com.infinities.keystone4j.model.token.wrapper.ITokenDataWrapper;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.model.KeystoneToken;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.utils.ReflectUtils;

public abstract class AbstractAction<T extends BaseEntity> implements Action {

	private final static Logger logger = LoggerFactory.getLogger(AbstractAction.class);
	private final TokenProviderApi tokenProviderApi;
	protected PolicyApi policyApi;


	public AbstractAction(TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		this.tokenProviderApi = tokenProviderApi;
		this.policyApi = policyApi;
	}

	protected void assignUniqueId(BaseEntity ref) {
		UUID uuid = UUID.randomUUID();
		ref.setId(uuid.toString());
	}

	public static Hints buildDriverHints(ContainerRequestContext context, String[] filters) {
		Hints hints = new Hints();
		MultivaluedMap<String, String> queryDict = context.getUriInfo().getQueryParameters();

		if (queryDict == null || queryDict.isEmpty()) {
			return hints;
		}
		List<String> supportedFilters = Arrays.asList(filters);

		for (String key : queryDict.keySet()) {
			if (supportedFilters == null || supportedFilters.contains(key)) {
				String val = queryDict.getFirst(key);
				Object value = queryDict.getFirst(key);
				if ("enabled".equals(key)) {
					value = Boolean.parseBoolean(val);
				}
				hints.addFilter(key, value);
				continue;
			}

			// TODO ignore query_String
		}

		return hints;
	}

	public CollectionWrapper<T> wrapCollection(ContainerRequestContext context, List<T> refs) {
		return wrapCollection(context, refs, null);
	}

	public CollectionWrapper<T> wrapCollection(ContainerRequestContext context, List<T> refs, Hints hints) {
		if (hints != null) {
			refs = filterByAttributes(refs, hints);
		}

		Entry<Boolean, List<T>> entry = limit(refs, hints);
		Boolean listLimited = entry.getKey();
		refs = entry.getValue();

		for (T ref : refs) {
			wrapMember(context, ref);
		}

		CollectionWrapper<T> container = getCollectionWrapper();
		container.setRefs(refs);
		logger.debug("uri path: {}", context.getUriInfo().getPath());
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

	public void addSelfReferentialLink(ContainerRequestContext context, BaseEntity ref) {
		MemberLinks links = new MemberLinks();
		String self = getBaseUrl(context, null) + "/" + ref.getId();
		links.setSelf(self);
		ref.setLinks(links);
	}

	public MemberWrapper<T> wrapMember(ContainerRequestContext context, T ref) {
		addSelfReferentialLink(context, ref);
		MemberWrapper<T> wrapper = getMemberWrapper();
		wrapper.setRef(ref);
		return wrapper;
	}

	private Entry<Boolean, List<T>> limit(List<T> refs, Hints hints) {
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

	private List<T> filterByAttributes(List<T> refs, Hints hints) {
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

	// keystone.common.wsgi.Application._get_trust_id_for_request
	public String getTrustIdForRequest(KeystoneContext context) throws UnsupportedEncodingException,
			NoSuchAlgorithmException, DecoderException {
		if (Strings.isNullOrEmpty(context.getTokenid())
				|| Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText().equals(context.getTokenid())) {
			logger.debug("will not lookup trust as the request auth token is either absent or it is the system admin token");
			return null;
		}
		ITokenDataWrapper tokenData;
		try {
			tokenData = tokenProviderApi.validateToken(context.getTokenid(), null);
		} catch (Exception e) {
			logger.warn("Invalid token in _get_trust_id_for_request", e);
			throw Exceptions.UnauthorizedException.getInstance();
		}
		KeystoneToken tokenRef = new KeystoneToken(context.getTokenid(), tokenData);
		logger.debug("trustId: {}", tokenRef.getTrustId());
		return tokenRef.getTrustId();
	}

	// keystone.common.wsgi.Application.assert_admin
	public void assertAdmin(KeystoneContext context) throws Exception {
		if (!context.isAdmin()) {
			KeystoneToken userTokenRef = null;
			try {
				userTokenRef = new KeystoneToken(context.getTokenid(), tokenProviderApi.validateToken(context.getTokenid(),
						null));
			} catch (Exception e) {
				throw Exceptions.UnauthorizedException.getInstance(e);
			}
			Wsgi.validateTokenBind(context, userTokenRef);
			Authorization.AuthContext creds = new Authorization.AuthContext();
			creds.setRoles(userTokenRef.getMetadata().getRoles());
			creds.setTrustId(userTokenRef.getMetadata().getTrustId());

			try {
				creds.setUserId(userTokenRef.getUserId());
			} catch (Exception e) {
				logger.debug("Invalid user");
				throw Exceptions.UnauthorizedException.getInstance();
			}

			if (userTokenRef.isProjectScoped()) {
				creds.setTenantId(userTokenRef.getProjectId());
			} else {
				logger.debug("Invalid tenant");
				throw Exceptions.UnauthorizedException.getInstance();
			}

			creds.setRoles(userTokenRef.getRoleNames());
			policyApi.enforce(creds, "admin_required", new HashMap<String, Object>());
		}
	}


	private class AttrMatchFunction implements Function<List<T>, List<T>> {

		private final String attr;
		private final Object value;


		private AttrMatchFunction(String attr, Object value) {
			this.attr = attr;
			this.value = value;
		}

		@Override
		public List<T> apply(List<T> input) {
			List<T> output = new ArrayList<T>();
			for (T t : input) {
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


	public void requireMatchingId(String id, BaseEntity entity) {
		KeystonePreconditions.requireMatchingId(id, entity);
	}

	public void requireMatchingDomainId(DomainAwared ref, DomainAwared existingRef) {
		boolean domainidImmutable = Config.Instance.getOpt(Config.Type.DEFAULT, "domain_id_immutable").asBoolean();
		if (domainidImmutable && ref.getDomain() != null && !Strings.isNullOrEmpty(ref.getDomain().getId())) {
			if (!ref.getDomain().getId().equals(existingRef.getDomain().getId())) {
				throw Exceptions.ValidationException.getInstance("Cannot change Domain ID");
			}
		}

	}

	public static AuthContext getAuthContext(ContainerRequestContext context) {
		if (context.getPropertyNames().contains(Authorization.AUTH_CONTEXT_ENV)) {
			AuthContext authContext = (AuthContext) context.getProperty(Authorization.AUTH_CONTEXT_ENV);
			return authContext;
		}
		return new AuthContext();
	}

	public static boolean queryFilterIsTrue(String filterValue) {
		if ("0".equals(filterValue)) {
			return false;
		} else {
			return true;
		}
	}

	// keystone.common.controller.V3Controller
	public void normalizeDomainid(KeystoneContext context, DomainAwared domainScoped) {
		if (domainScoped.getDomain() == null || Strings.isNullOrEmpty(domainScoped.getDomain().getId())) {
			String domainid = getDomainIdFromToken(context);
			Domain domain = new Domain();
			domain.setId(domainid);
			domainScoped.setDomain(domain);
		}
	}

	// keystone.common.controller.V3Controller
	public String getDomainIdFromToken(KeystoneContext context) {
		KeystoneToken tokenRef = null;
		try {
			tokenRef = new KeystoneToken(context.getTokenid(), tokenProviderApi.validateToken(context.getTokenid(), null));
		} catch (WebApplicationException e) {
			logger.warn("Invalid token found while getting domain ID for list request");
			throw Exceptions.UnauthorizedException.getInstance();
		} catch (Exception e) {
			logger.warn("Token validate failed", e);
			throw Exceptions.ValidationException.getInstance("A domain-scoped token must be used");
		}

		if (tokenRef.isDomainScoped()) {
			return tokenRef.getDomainId();
		} else {
			return Config.Instance.getOpt(Config.Type.identity, "default_domain_id").asText();
		}
	}

	// keystone.common.controller.V3Controller
	public String getDomainidForListRequest(ContainerRequestContext request) {
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		if (!Config.Instance.getOpt(Config.Type.identity, "domain_specific_drivers_enabled").asBoolean()) {
			return null;
		}

		if (!Strings.isNullOrEmpty(request.getUriInfo().getQueryParameters().getFirst("domain_id"))) {
			return request.getUriInfo().getQueryParameters().getFirst("domain_id");
		}

		KeystoneToken tokenRef = null;
		try {
			tokenRef = new KeystoneToken(context.getTokenid(), tokenProviderApi.validateToken(context.getTokenid(), null));
		} catch (Exception e) {
			logger.warn("Invalid token found while getting domain ID for list request");
			throw Exceptions.UnauthorizedException.getInstance();
		}

		if (tokenRef.isDomainScoped()) {
			return tokenRef.getDomainId();
		} else {
			logger.warn("No domain information specified as part of list request");
			throw Exceptions.UnauthorizedException.getInstance();
		}
	}

	public abstract CollectionWrapper<T> getCollectionWrapper();

	public abstract MemberWrapper<T> getMemberWrapper();
	//
	// @Override
	// public abstract String getCollectionName();
	//
	// @Override
	// public abstract String getMemberName();

}
