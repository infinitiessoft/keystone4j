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
package com.infinities.keystone4j.auth.controller.action;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.base.Strings;
import com.infinities.keystone4j.auth.AuthDriver;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.exception.Exceptions;

public class AbstractControllerAction {

	protected final static Map<String, AuthDriver> AUTH_METHODS = new ConcurrentHashMap<String, AuthDriver>();
	protected static boolean AUTH_PLUGINS_LOADED = false;


	public AbstractControllerAction() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		loadAuthMethods();
	}

	private void loadAuthMethods() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		if (AUTH_PLUGINS_LOADED) {
			return;
		}
		List<String> methods = Config.getOpt(Config.Type.auth, "methods").asList();
		AuthDriver driver;
		for (String plugin : methods) {
			if (plugin.contains(".")) {
				String className = plugin;
				Class<?> c = Class.forName(className);
				driver = (AuthDriver) c.newInstance();
				if (Strings.isNullOrEmpty(driver.getMethod())) {
					throw new RuntimeException(
							"Cannot load an auth-plugin by class-name without a 'method' attribute define:" + className);
				}
			} else {
				String className = Config.getOpt(Config.Type.auth, plugin).asText();
				Class<?> c = Class.forName(className);
				driver = (AuthDriver) c.newInstance();
				if (Strings.isNullOrEmpty(driver.getMethod())) {
					throw new RuntimeException(
							"Cannot load an auth-plugin by class-name without a 'method' attribute define:" + className);
				} else {
					if (!plugin.equals(driver.getMethod())) {
						String msg = String.format("Driver requested method %s does not match plugin name %s",
								driver.getMethod(), plugin);
						throw new RuntimeException(msg);
					}

				}
			}

			if (AUTH_METHODS.containsKey(driver.getMethod())) {
				String msg = String.format("Auth plugin %s is requesting previously registered method %s", plugin,
						driver.getMethod());
				throw new RuntimeException(msg);
			}
			AUTH_METHODS.put(driver.getMethod(), driver);
		}
		AUTH_PLUGINS_LOADED = true;
	}

	protected AuthDriver getAuthMethod(String name) {
		if (!AUTH_METHODS.containsKey(name)) {
			throw Exceptions.AuthMethodNotSupportedException.getInstance();
			// AuthDriver driver = loadAuthMethod(name);
			// AUTH_METHODS.put(name, driver);
		}
		return AUTH_METHODS.get(name);
	}

}
