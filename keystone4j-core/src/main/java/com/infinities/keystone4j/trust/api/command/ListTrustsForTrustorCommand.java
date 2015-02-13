package com.infinities.keystone4j.trust.api.command;

import java.util.List;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.trust.TrustDriver;

public class ListTrustsForTrustorCommand extends AbstractTrustCommand implements NonTruncatedCommand<List<Trust>> {

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
