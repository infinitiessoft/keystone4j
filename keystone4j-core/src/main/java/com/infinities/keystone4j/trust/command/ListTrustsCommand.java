package com.infinities.keystone4j.trust.command;

import java.util.List;

import com.infinities.keystone4j.trust.TrustDriver;
import com.infinities.keystone4j.trust.model.Trust;

public class ListTrustsCommand extends AbstractTrustCommand<List<Trust>> {

	public ListTrustsCommand(TrustDriver trustDriver) {
		super(trustDriver);
	}

	@Override
	public List<Trust> execute() {
		return this.getTrustDriver().listTrusts();
	}

}
