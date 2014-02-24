package com.infinities.keystone4j.trust.api;

import java.util.List;

import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.trust.TrustApi;
import com.infinities.keystone4j.trust.TrustDriver;
import com.infinities.keystone4j.trust.command.CreateTrustCommand;
import com.infinities.keystone4j.trust.command.DeleteTrustCommand;
import com.infinities.keystone4j.trust.command.GetTrustCommand;
import com.infinities.keystone4j.trust.command.ListTrustsCommand;
import com.infinities.keystone4j.trust.command.ListTrustsForTrusteeCommand;
import com.infinities.keystone4j.trust.command.ListTrustsForTrustorCommand;
import com.infinities.keystone4j.trust.model.Trust;

public class TrustApiImpl implements TrustApi {

	private final TrustDriver trustDriver;


	public TrustApiImpl(TrustDriver trustDriver) {
		super();
		this.trustDriver = trustDriver;
	}

	@Override
	public Trust createTrust(Trust trust, List<Role> cleanRoles) {
		CreateTrustCommand command = new CreateTrustCommand(trustDriver, trust, cleanRoles);
		return command.execute();
	}

	@Override
	public List<Trust> listTrusts() {
		ListTrustsCommand command = new ListTrustsCommand(trustDriver);
		return command.execute();
	}

	@Override
	public List<Trust> listTrustsForTrustor(String trustorid) {
		ListTrustsForTrustorCommand command = new ListTrustsForTrustorCommand(trustDriver, trustorid);
		return command.execute();
	}

	@Override
	public List<Trust> listTrustsForTrustee(String trusteeid) {
		ListTrustsForTrusteeCommand command = new ListTrustsForTrusteeCommand(trustDriver, trusteeid);
		return command.execute();
	}

	@Override
	public Trust getTrust(String trustid) {
		GetTrustCommand command = new GetTrustCommand(trustDriver, trustid);
		return command.execute();
	}

	@Override
	public void deleteTrust(String trustid) {
		DeleteTrustCommand command = new DeleteTrustCommand(trustDriver, trustid);
		command.execute();
	}

}
