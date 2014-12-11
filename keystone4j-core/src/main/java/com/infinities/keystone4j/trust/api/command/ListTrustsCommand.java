package com.infinities.keystone4j.trust.api.command;

import java.util.List;

import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.trust.TrustDriver;

public class ListTrustsCommand extends AbstractTrustCommand<List<Trust>> {

	public ListTrustsCommand(TrustDriver trustDriver) {
		super(trustDriver);
	}

	@Override
	public List<Trust> execute() {
		return this.getTrustDriver().listTrusts();
	}

}
