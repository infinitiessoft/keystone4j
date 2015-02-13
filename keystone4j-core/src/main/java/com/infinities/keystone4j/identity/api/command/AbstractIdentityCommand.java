package com.infinities.keystone4j.identity.api.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.common.Hints.Filter;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.DomainConfigs;
import com.infinities.keystone4j.identity.IdMappingApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.IdentityDriver;
import com.infinities.keystone4j.model.BaseEntity;
import com.infinities.keystone4j.model.DomainAwared;
import com.infinities.keystone4j.model.identity.mapping.EntityType;
import com.infinities.keystone4j.model.identity.mapping.IdMapping;

public abstract class AbstractIdentityCommand {

	private final static Logger logger = LoggerFactory.getLogger(AbstractIdentityCommand.class);
	private final AssignmentApi assignmentApi;
	private final CredentialApi credentialApi;
	private final IdentityApi identityApi;
	private final IdentityDriver identityDriver;
	private final RevokeApi revokeApi;
	private final IdMappingApi idMappingApi;
	private final static DomainConfigs domainConfigs = new DomainConfigs();


	public AbstractIdentityCommand(AssignmentApi assignmentApi, CredentialApi credentialApi, RevokeApi revokeApi,
			IdentityApi identityApi, IdMappingApi idMappingApi, IdentityDriver identityDriver) {
		super();
		this.assignmentApi = assignmentApi;
		this.credentialApi = credentialApi;
		this.revokeApi = revokeApi;
		this.identityApi = identityApi;
		this.idMappingApi = idMappingApi;
		this.identityDriver = identityDriver;
	}

	public CredentialApi getCredentialApi() {
		return credentialApi;
	}

	public IdentityDriver getIdentityDriver() {
		return identityDriver;
	}

	public IdentityApi getIdentityApi() {
		return identityApi;
	}

	public AssignmentApi getAssignmentApi() {
		return assignmentApi;
	}

	public RevokeApi getRevokeApi() {
		return revokeApi;
	}

	public IdMappingApi getIdMappingApi() {
		return idMappingApi;
	}

	public DomainConfigs getDomainConfigs() {
		return domainConfigs;
	}

	protected <T extends BaseEntity & DomainAwared> List<T> setDomainIdAndMapping(List<T> ref, String domainId,
			IdentityDriver driver, EntityType entityType) throws Exception {
		if (!needsPostProcessing(driver)) {
			return ref;
		}
		logger.debug("ID Mapping - Domain ID: {}, Default Driver: {}, Domains: {}, UUIDs: {}, Compatible IDs: {}",
				new Object[] { domainId, driver == this.getIdentityDriver(), driver.isDomainAware(), driver.generateUuids(),
						Config.Instance.getOpt(Config.Type.identity, "backward_compatible_ids").asBoolean() });
		List<T> refs = new ArrayList<T>();

		for (T r : ref) {
			refs.add(setDomainIdAndMapping(r, domainId, driver, entityType));
		}

		return refs;
	}

	protected <T extends BaseEntity & DomainAwared> T setDomainIdAndMapping(T ref, String domainId, IdentityDriver driver,
			EntityType entityType) throws Exception {
		if (!needsPostProcessing(driver)) {
			return ref;
		}

		logger.debug("ID Mapping - Domain ID: {}, Default Driver: {}, Domains: {}, UUIDs: {}, Compatible IDs: {}",
				new Object[] { domainId, driver == this.getIdentityDriver(), driver.isDomainAware(), driver.generateUuids(),
						Config.Instance.getOpt(Config.Type.identity, "backward_compatible_ids").asBoolean() });

		return setDomainIdAndMappingForSingleRef(ref, domainId, driver, entityType);

	}

	private boolean needsPostProcessing(IdentityDriver driver) {
		return !driver.equals(this.getIdentityDriver()) || !driver.generateUuids() || !driver.isDomainAware();
	}

	private <T extends BaseEntity & DomainAwared> T setDomainIdAndMappingForSingleRef(T ref, String domainId,
			IdentityDriver driver, EntityType entityType) throws Exception {
		logger.debug("Local ID: {}", ref.getId());
		insertDomainIdIfNeeded(ref, driver, domainId);
		if (isMappingNeeded(driver)) {
			IdMapping localEntity = new IdMapping();
			localEntity.setDomainId(ref.getDomainId());
			localEntity.setLocalId(ref.getId());
			localEntity.setEntityType(entityType);

			String publicId = this.getIdMappingApi().getPublicId(localEntity);
			if (!Strings.isNullOrEmpty(publicId)) {
				ref.setId(publicId);
				logger.debug("Found existing mapping to public ID: %s", ref.getId());
			} else {
				if (driver.generateUuids()) {
					publicId = ref.getId();
				}
				ref.setId(this.getIdMappingApi().createIdMapping(localEntity, publicId));
				logger.debug("Created new mapping to public ID: {}", ref.getId());
			}
		}
		return ref;
	}

	private boolean isMappingNeeded(IdentityDriver driver) {
		boolean isNotDefaultDriver = !driver.equals(this.getIdentityDriver());

		return isNotDefaultDriver
				|| (!driver.generateUuids() && !Config.Instance.getOpt(Config.Type.identity, "backward_compatible_ids")
						.asBoolean());
	}

	private void insertDomainIdIfNeeded(DomainAwared ref, IdentityDriver driver, String domainId) {
		if (!driver.isDomainAware()) {
			if (Strings.isNullOrEmpty(domainId)) {
				domainId = Config.Instance.getOpt(Config.Type.identity, "default_domain_id").asText();
			}
			ref.setDomainId(domainId);
		}
	}

	protected DomainIdDriverAndEntityId getDomainDriverAndEntityId(String publicId) throws Exception {
		if (Config.Instance.getOpt(Config.Type.identity, "domain_specific_drivers_enabled").asBoolean()) {
			IdMapping localIdRef = this.getIdMappingApi().getIdMapping(publicId);
			if (localIdRef != null) {
				DomainIdDriverAndEntityId ret = new DomainIdDriverAndEntityId();
				ret.setDomainId(localIdRef.getDomainId());
				ret.setDriver(selectIdentityDriver(localIdRef.getDomainId()));
				ret.setLocalId(localIdRef.getLocalId());
				return ret;
			}
		}
		IdentityDriver driver = this.getIdentityDriver();
		if (driver.generateUuids()) {
			if (driver.isDomainAware()) {
				DomainIdDriverAndEntityId ret = new DomainIdDriverAndEntityId();
				ret.setDriver(driver);
				ret.setLocalId(publicId);
				return ret;
			} else {
				DomainIdDriverAndEntityId ret = new DomainIdDriverAndEntityId();
				ret.setDomainId(Config.Instance.getOpt(Config.Type.DEFAULT, "default_domain_id").asText());
				ret.setDriver(driver);
				ret.setLocalId(publicId);
				return ret;
			}
		}

		if (!Config.Instance.getOpt(Config.Type.identity_mapping, "backward_compatible_ids").asBoolean()) {
			IdMapping localIdRef = this.getIdMappingApi().getIdMapping(publicId);
			if (localIdRef != null) {
				DomainIdDriverAndEntityId ret = new DomainIdDriverAndEntityId();
				ret.setDomainId(localIdRef.getDomainId());
				ret.setDriver(driver);
				ret.setLocalId(localIdRef.getLocalId());
				return ret;
			}
		}

		DomainIdDriverAndEntityId ret = new DomainIdDriverAndEntityId();
		ret.setDomainId(Config.Instance.getOpt(Config.Type.DEFAULT, "default_domain_id").asText());
		ret.setDriver(driver);
		ret.setLocalId(publicId);
		return ret;
	}

	protected IdentityDriver selectIdentityDriver(String domainId) {
		IdentityDriver driver = null;
		if (Strings.isNullOrEmpty(domainId)) {
			driver = this.getIdentityDriver();
		} else {
			driver = getDomainConfigs().getDomainDriver(domainId);
			if (driver == null) {
				driver = this.getIdentityDriver();
			}
		}

		if (!driver.isDomainAware() && driver.equals(this.getIdentityDriver())
				&& !Config.Instance.getOpt(Config.Type.DEFAULT, "default_domain_id").asText().equals(domainId)
				&& !Strings.isNullOrEmpty(domainId)) {
			logger.warn(
					"Found multiple domains being mapped to a driver that does not support that (e.g. LDAP; - Domain ID: {}, Default Driver: {}",
					new Object[] { domainId, driver.equals(this.getIdentityDriver()) });
			throw Exceptions.DomainNotFoundException.getInstance(domainId);
		}

		return driver;
	}

	protected void ensureDomainIdInHints(Hints hints, String domainid) {
		if (!Strings.isNullOrEmpty(domainid) && hints.getExactFilterByName("domain_id") != null) {
			hints.addFilter("domain_id", domainid);
		}
	}

	protected void markDomainIdFilterSatisfied(Hints hints) {
		if (hints != null) {
			for (Iterator<Filter> iterator = hints.getFilters().iterator(); iterator.hasNext();) {
				Filter filter = iterator.next();
				if ("domain_id".equals(filter.getName()) && "equals".equals(filter.getComparator())) {
					iterator.remove();
				}
			}
		}
	}

	protected <T extends DomainAwared> T clearDomainIdIfDomainUnaware(IdentityDriver driver, T ref) {
		if (!driver.isDomainAware() && !Strings.isNullOrEmpty(ref.getDomainId())) {
			ref.setDomain(null);
		}
		return ref;
	}

	protected void assertUserAndGroupInSameBackend(String userEntityId, IdentityDriver userDriver, String groupEntityId,
			IdentityDriver groupDriver) {
		if (!userDriver.equals(groupDriver)) {
			userDriver.getUser(userEntityId);
			groupDriver.getGroup(groupEntityId);
			throw Exceptions.CrossBackendNotAllowedException.getInstance(null, groupEntityId, userEntityId);
		}
	}


	public static class DomainIdDriverAndEntityId {

		private String domainId;
		private IdentityDriver driver;
		private String localId;


		public String getDomainId() {
			return domainId;
		}

		public void setDomainId(String domainId) {
			this.domainId = domainId;
		}

		public IdentityDriver getDriver() {
			return driver;
		}

		public void setDriver(IdentityDriver driver) {
			this.driver = driver;
		}

		public String getLocalId() {
			return localId;
		}

		public void setLocalId(String localId) {
			this.localId = localId;
		}

	}

}
