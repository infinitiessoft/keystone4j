package com.infinities.keystone4j.trust.command;

import java.util.List;

import com.infinities.keystone4j.trust.TrustDriver;
import com.infinities.keystone4j.trust.model.Trust;

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
