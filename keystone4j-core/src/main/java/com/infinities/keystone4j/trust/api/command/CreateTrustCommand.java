package com.infinities.keystone4j.trust.api.command;

import java.util.List;

import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.notification.NotifiableCommand;
import com.infinities.keystone4j.trust.TrustDriver;

public class CreateTrustCommand extends AbstractTrustCommand implements NotifiableCommand<Trust> {

	private final String trustid;
	private final Trust trust;
	private final List<Role> cleanRoles;


	public CreateTrustCommand(TrustDriver trustDriver, String trustid, Trust trust, List<Role> cleanRoles) {
		super(trustDriver);
		this.trustid = trustid;
		this.trust = trust;
		this.cleanRoles = cleanRoles;
	}

	@Override
	public Trust execute() {
		if (trust.getRemainingUses() != null) {
			if (trust.getRemainingUses() <= 0) {
				String msg = "remaining_uses must be a positive integer or null.";
				throw Exceptions.ValidationException.getInstance(msg);
			}
		}
		return this.getTrustDriver().createTrust(trustid, trust, cleanRoles);
	}

	@Override
	public Object getArgs(int index) {
		if (index == 1) {
			return trustid;
		} else if (index == 2) {
			return trust;
		} else if (index == 3) {
			return cleanRoles;
		}
		throw new IllegalArgumentException("invalid index");
	}

}
