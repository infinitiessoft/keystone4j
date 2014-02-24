package com.infinities.keystone4j.trust.command;

import java.util.List;

import com.infinities.keystone4j.trust.TrustDriver;
import com.infinities.keystone4j.trust.model.Trust;

public class ListTrustsForTrustorCommand extends AbstractTrustCommand<List<Trust>> {

	private final String trustorid;


	public ListTrustsForTrustorCommand(TrustDriver trustDriver, String trustorid) {
		super(trustDriver);
		this.trustorid = trustorid;
	}

	@Override
	public List<Trust> execute() {
		return this.getTrustDriver().listTrustsForTrustor(trustorid);
	}

}
