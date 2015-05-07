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
package com.infinities.keystone4j.policy;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import com.infinities.keystone4j.model.policy.Context;
import com.infinities.keystone4j.policy.check.BaseCheck;

public interface Enforcer {

	public void setRules(Map<String, BaseCheck> rules, boolean overwrite, boolean useConf);

	public void clear();

	public void loadRules(boolean forceReload) throws MalformedURLException, IOException;

	public boolean enforce(Object rule, Map<String, Object> target, Context creds, boolean doRaise, Exception ex)
			throws Exception;

	public Map<String, BaseCheck> getRules();
}
