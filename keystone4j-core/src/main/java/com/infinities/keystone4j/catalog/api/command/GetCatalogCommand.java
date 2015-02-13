package com.infinities.keystone4j.catalog.api.command;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.token.Metadata;

public class GetCatalogCommand extends AbstractCatalogCommand implements
		NonTruncatedCommand<Map<String, Map<String, Map<String, String>>>> {

	private final static Logger logger = LoggerFactory.getLogger(GetCatalogCommand.class);
	private final String userid;
	private final String tenantid;
	private final Metadata metadata;


	public GetCatalogCommand(CatalogDriver catalogDriver, String userid, String tenantid, Metadata metadata) {
		super(catalogDriver);
		this.userid = userid;
		this.tenantid = tenantid;
		this.metadata = metadata;
	}

	@Override
	public Map<String, Map<String, Map<String, String>>> execute() {
		try {
			return this.getCatalogDriver().getCatalog(userid, tenantid, metadata);
		} catch (Exception e) {
			logger.warn("get catalog failed", e);
			throw Exceptions.NotFoundException.getInstance("Catalog not found for user and tenant");
		}
	}

}
