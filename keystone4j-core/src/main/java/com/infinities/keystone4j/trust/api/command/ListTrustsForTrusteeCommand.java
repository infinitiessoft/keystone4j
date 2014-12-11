package com.infinities.keystone4j.trust.api.command;

import java.util.List;

import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.trust.TrustDriver;

public class ListTrustsForTrusteeCommand extends AbstractTrustCommand<List<Trust>> {

	private final String trusteeid;


	public ListTrustsForTrusteeCommand(TrustDriver trustDriver, String trusteeid) {
		super(trustDriver);
		this.trusteeid = trusteeid;
	}

	@Override
	public List<Trust> execute() {
		return this.getTrustDriver().listTrustsForTrustee(trusteeid);
	}

}
