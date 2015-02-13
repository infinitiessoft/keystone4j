package com.infinities.keystone4j.catalog.api.command;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.catalog.Service;

public class GetV3CatalogCommand extends AbstractCatalogCommand implements NonTruncatedCommand<List<Service>> {

	private final static Logger logger = LoggerFactory.getLogger(GetV3CatalogCommand.class);
	private final String userid;
	private final String projectid;


	public GetV3CatalogCommand(CatalogDriver catalogDriver, String userid, String projectid) {
		super(catalogDriver);
		this.userid = userid;
		this.projectid = projectid;
	}

	@Override
	public List<Service> execute() {
		try {
			return this.getCatalogDriver().getV3Catalog(userid, projectid);
		} catch (Exception e) {
			logger.warn("get catalog failed", e);
			throw Exceptions.NotFoundException.getInstance("Catalog not found for user and tenant");
		}
	}
}
