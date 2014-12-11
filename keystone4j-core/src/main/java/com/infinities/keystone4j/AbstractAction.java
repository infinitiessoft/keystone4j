package com.infinities.keystone4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.beanutils.PropertyUtils;
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
import com.infinities.keystone4j.model.DomainScoped;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.common.Links;
import com.infinities.keystone4j.token.model.KeystoneToken;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public abstract class AbstractAction<T extends BaseEntity> {

	private final static Logger logger = LoggerFactory.getLogger(AbstractAction.class);
	private final TokenProviderApi tokenProviderApi;


	public AbstractAction(TokenProviderApi tokenProviderApi) {
		this.tokenProviderApi = tokenProviderApi;
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

	protected String getBaseUrl(ContainerRequestContext context, String path) {
		String endpoint = Wsgi.getBaseUrl(context, "public");
		if (Strings.isNullOrEmpty(path)) {
			path = getCollectionName();
		}
		return String.format("%s/%s/s", endpoint, "v3", StringUtils.removeStart(path, "/"));
	}

	public void addSelfReferentialLink(ContainerRequestContext context, BaseEntity ref) {
		Links links = new Links();
		String self = getBaseUrl(context, null) + "/" + ref.getId();
		links.setSelf(self);
		ref.setLinks(links);
	}

	protected MemberWrapper<T> wrapMember(ContainerRequestContext context, T ref) {
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


	public void requireMatchingId(String id, BaseEntity entity) {
		KeystonePreconditions.requireMatchingId(id, entity);
	}

	public void requireMatchingDomainId(DomainScoped ref, DomainScoped existingRef) {
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

	public void normalizeDomainid(KeystoneContext context, DomainScoped domainScoped) {
		if (domainScoped.getDomain() == null || Strings.isNullOrEmpty(domainScoped.getDomain().getId())) {
			String domainid = getDomainFromToken(context);
			Domain domain = new Domain();
			domain.setId(domainid);
			domainScoped.setDomain(domain);
		}
	}

	public String getDomainFromToken(KeystoneContext context) {
		KeystoneToken tokenRef = null;
		try {
			tokenRef = new KeystoneToken(context.getTokenid(), tokenProviderApi.validToken(context.getTokenid()));
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
			tokenRef = new KeystoneToken(context.getTokenid(), tokenProviderApi.validToken(context.getTokenid()));
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

	protected abstract CollectionWrapper<T> getCollectionWrapper();

	protected abstract MemberWrapper<T> getMemberWrapper();

	public abstract String getCollectionName();

	public abstract String getMemberName();

}
