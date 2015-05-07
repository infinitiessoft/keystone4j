/*******************************************************************************
 * # Copyright 2015 InfinitiesSoft Solutions Inc.
 * #
 * # Licensed under the Apache License, Version 2.0 (the "License"); you may
 * # not use this file except in compliance with the License. You may obtain
 * # a copy of the License at
 * #
 * #      http://www.apache.org/licenses/LICENSE-2.0
 * #
 * # Unless required by applicable law or agreed to in writing, software
 * # distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * # WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * # License for the specific language governing permissions and limitations
 * # under the License.
 *******************************************************************************/
package com.infinities.keystone4j.trust.api;

import java.util.List;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.notification.Notifications;
import com.infinities.keystone4j.trust.TrustApi;
import com.infinities.keystone4j.trust.TrustDriver;
import com.infinities.keystone4j.trust.api.command.ConsumeUseCommand;
import com.infinities.keystone4j.trust.api.command.CreateTrustCommand;
import com.infinities.keystone4j.trust.api.command.DeleteTrustCommand;
import com.infinities.keystone4j.trust.api.command.GetTrustCommand;
import com.infinities.keystone4j.trust.api.command.ListTrustsCommand;
import com.infinities.keystone4j.trust.api.command.ListTrustsForTrusteeCommand;
import com.infinities.keystone4j.trust.api.command.ListTrustsForTrustorCommand;

public class TrustApiImpl implements TrustApi {

	private final TrustDriver trustDriver;
	private final static String _TRUST = "OS-TRUST:trust";


	public TrustApiImpl(TrustDriver trustDriver) {
		super();
		this.trustDriver = trustDriver;
	}

	@Override
	public Trust createTrust(String trustid, Trust trust, List<Role> cleanRoles) throws Exception {
		NonTruncatedCommand<Trust> command = Notifications.created(new CreateTrustCommand(trustDriver, trustid, trust,
				cleanRoles), _TRUST);
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
	public Trust getTrust(String trustid, boolean deleted) {
		GetTrustCommand command = new GetTrustCommand(trustDriver, trustid, deleted);
		return command.execute();
	}

	@Override
	public void deleteTrust(String trustid) throws Exception {
		NonTruncatedCommand<Trust> command = Notifications.deleted(new DeleteTrustCommand(trustDriver, trustid), _TRUST);
		command.execute();
	}

	@Override
	public void consumeUse(String trustid) throws Exception {
		ConsumeUseCommand command = new ConsumeUseCommand(trustDriver, trustid);
		command.execute();
	}

}
