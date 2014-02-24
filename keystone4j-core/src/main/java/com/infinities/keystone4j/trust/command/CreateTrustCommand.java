package com.infinities.keystone4j.trust.command;

import java.util.List;

import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.trust.TrustDriver;
import com.infinities.keystone4j.trust.model.Trust;

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
