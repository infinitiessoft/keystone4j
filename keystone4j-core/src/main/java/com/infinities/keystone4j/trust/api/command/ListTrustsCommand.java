package com.infinities.keystone4j.trust.api.command;

import java.util.List;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.trust.TrustDriver;

public class ListTrustsCommand extends AbstractTrustCommand implements NonTruncatedCommand<List<Trust>> {

	public ListTrustsCommand(TrustDriver trustDriver) {
		super(trustDriver);
	}

	@Override
	public List<Trust> execute() {
		return this.getTrustDriver().listTrusts();
	}

}
