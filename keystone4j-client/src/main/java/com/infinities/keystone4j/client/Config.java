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
package com.infinities.keystone4j.client;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;
import com.infinities.keystone4j.client.utils.FileScanner;
import com.infinities.keystone4j.option.Option;
import com.infinities.keystone4j.option.Options;
import com.infinities.keystone4j.option.StringOption;

public enum Config {
	Instance;

	public enum Type {
		keystone_authtoken;
	}


	private final Logger logger = LoggerFactory.getLogger(Config.class);
	private URL DEFAULT_CONFIG_FILENAME;
	public final static String CONFIG_FOLDER = "config";
	private final Table<Type, String, Option> OPTIONS = HashBasedTable.create();
	private final Pattern pattern = Pattern.compile("%\\((.*?)\\)", Pattern.DOTALL);


	private Config() {
		try {
			preSetup();
			parseConfigFiles();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void preSetup() throws MalformedURLException {
		// DEFAULT_CONFIG_FILENAME =
		// getClass().getResource(KeystoneApplication.CONF_DIR +
		// "keystone.conf");
		DEFAULT_CONFIG_FILENAME = getURL(CONFIG_FOLDER + File.separator + "keystone4j-client.conf");

		// keystonemiddleware.auth_token
		// deprecated use identity_uri
		OPTIONS.put(Config.Type.keystone_authtoken, "auth_admin_prefix", Options.newStrOpt("auth_admin_prefix", ""));
		// deprecated use identity_uri
		OPTIONS.put(Config.Type.keystone_authtoken, "auth_host", Options.newStrOpt("auth_host", "127.0.0.1"));
		// deprecated use identity_uri
		OPTIONS.put(Config.Type.keystone_authtoken, "auth_port", Options.newIntOpt("auth_port", 35357));
		// deprecated use identity_uri
		OPTIONS.put(Config.Type.keystone_authtoken, "auth_protocol", Options.newStrOpt("auth_protocol", "https"));
		OPTIONS.put(Config.Type.keystone_authtoken, "auth_uri", Options.newStrOpt("auth_uri", ""));
		OPTIONS.put(Config.Type.keystone_authtoken, "identity_uri", Options.newStrOpt("identity_uri", ""));
		OPTIONS.put(Config.Type.keystone_authtoken, "auth_version", Options.newStrOpt("auth_version", ""));
		OPTIONS.put(Config.Type.keystone_authtoken, "delay_auth_decision", Options.newBoolOpt("delay_auth_decision", false));
		OPTIONS.put(Config.Type.keystone_authtoken, "http_connect_timeout", Options.newIntOpt("http_connect_timeout", 1000));
		OPTIONS.put(Config.Type.keystone_authtoken, "http_request_max_retries",
				Options.newIntOpt("http_request_max_retries", 3));
		OPTIONS.put(Config.Type.keystone_authtoken, "admin_token", Options.newStrOpt("admin_token", true, ""));
		OPTIONS.put(Config.Type.keystone_authtoken, "admin_user", Options.newStrOpt("admin_user"));
		OPTIONS.put(Config.Type.keystone_authtoken, "admin_password", Options.newStrOpt("admin_password", true, ""));
		OPTIONS.put(Config.Type.keystone_authtoken, "admin_tenant_name", Options.newStrOpt("admin_tenant_name", "admin"));
		OPTIONS.put(Config.Type.keystone_authtoken, "cache", Options.newStrOpt("cache", ""));
		OPTIONS.put(Config.Type.keystone_authtoken, "certfile", Options.newStrOpt("certfile", ""));
		OPTIONS.put(Config.Type.keystone_authtoken, "keyfile", Options.newStrOpt("keyfile", ""));
		OPTIONS.put(Config.Type.keystone_authtoken, "cafile", Options.newStrOpt("cafile", ""));
		OPTIONS.put(Config.Type.keystone_authtoken, "insecure", Options.newBoolOpt("insecure", false));
		OPTIONS.put(Config.Type.keystone_authtoken, "signing_dir", Options.newStrOpt("signing_dir", ""));
		OPTIONS.put(Config.Type.keystone_authtoken, "memcached_servers",
				Options.newListOpt("memcached_servers", new ArrayList<String>()));
		OPTIONS.put(Config.Type.keystone_authtoken, "token_cache_time", Options.newIntOpt("token_cache_time", 300));
		OPTIONS.put(Config.Type.keystone_authtoken, "revocation_cache_time", Options.newIntOpt("revocation_cache_time", 10));
		OPTIONS.put(Config.Type.keystone_authtoken, "memcache_security_strategy",
				Options.newStrOpt("memcache_security_strategy", ""));
		OPTIONS.put(Config.Type.keystone_authtoken, "memcache_secret_key",
				Options.newStrOpt("memcache_secret_key", true, ""));
		OPTIONS.put(Config.Type.keystone_authtoken, "include_service_catalog",
				Options.newBoolOpt("include_service_catalog", true));
		OPTIONS.put(Config.Type.keystone_authtoken, "enforce_token_bind",
				Options.newStrOpt("enforce_token_bind", "permissive"));
		OPTIONS.put(Config.Type.keystone_authtoken, "check_revocations_for_cached",
				Options.newBoolOpt("check_revocations_for_cached", false));

		List<String> algorithms = new ArrayList<String>();
		algorithms.add("md5");
		OPTIONS.put(Config.Type.keystone_authtoken, "hash_algorithms", Options.newListOpt("hash_algorithms", algorithms));
	}

	private void parseConfigFiles() throws IOException {
		FileScanner scanner = new FileScanner(DEFAULT_CONFIG_FILENAME);
		Table<Type, String, String> customTable = scanner.read();
		for (Cell<Type, String, String> cell : customTable.cellSet()) {
			if (OPTIONS.contains(cell.getRowKey(), cell.getColumnKey())) {
				logger.debug("reset FILE_OPTIONS {}.{} = {}",
						new Object[] { cell.getRowKey(), cell.getColumnKey(), cell.getValue() });
				OPTIONS.get(cell.getRowKey(), cell.getColumnKey()).resetValue(cell.getValue());
			} else {
				OPTIONS.put(cell.getRowKey(), cell.getColumnKey(), Options.newStrOpt(cell.getValue()));
			}
		}
	}

	public Option getOpt(Type type, String attr) {
		Option option = OPTIONS.get(type, attr);
		if (option instanceof StringOption) {
			Option newOption = Options.newStrOpt(option.getName(), option.getValue());
			Matcher matcher = pattern.matcher(option.asText());
			while (matcher.find()) {
				String match = matcher.group(1);

				logger.debug("sub-option pattern match: {}", match);
				Option suboption = OPTIONS.get(type, match);
				if (suboption != null) {
					String newValue = matcher.replaceFirst(suboption.getValue());
					newOption.setValue(newValue);
				}
			}
			return newOption;
		} else {
			return option;
		}
	}

	public Option getOpt(String attr) {
		return getOpt(Type.keystone_authtoken, attr);
	}

	public URL getURL(String filePath) {
		File file = new File(filePath);
		try {
			return file.toURI().toURL();
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public String findFile(String name) {
		String path = CONFIG_FOLDER + File.separator + name;
		File f = new File(path);
		if (f.exists()) {
			return f.getAbsolutePath();
		} else {
			return null;
		}
	}
}
