package com.infinities.keystone4j.trust.command;

import java.util.List;

import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.trust.TrustDriver;

public class CreateTrustCommand extends AbstractTrustCommand<Trust> {

	private final Trust trust;
	private final List<Role> cleanRoles;


	public CreateTrustCommand(TrustDriver trustDriver, Trust trust, List<Role> cleanRoles) {
		super(trustDriver);
		this.trust = trust;
		this.cleanRoles = cleanRoles;
	}

	@Override
	public Trust execute() {
		return this.getTrustDriver().createTrust(trust, cleanRoles);
	}

}
