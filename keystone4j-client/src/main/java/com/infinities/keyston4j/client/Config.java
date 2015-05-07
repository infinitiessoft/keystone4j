package com.infinities.keyston4j.client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;
import com.infinities.keyston4j.client.utils.FileScanner;
import com.infinities.keystone4j.option.Option;
import com.infinities.keystone4j.option.Options;
import com.infinities.keystone4j.option.StringOption;
import com.infinities.keystone4j.utils.URLUtils;

public enum Config {
	Instance;

	public enum Type {
		keystone_authtoken
	}


	private final static Logger logger = LoggerFactory.getLogger(Config.class);
	private URL DEFAULT_CONFIG_FILENAME;
	private final Table<Type, String, Option> FILE_OPTIONS = HashBasedTable.create();
	private final Pattern pattern = Pattern.compile("%\\((.*?)\\)", Pattern.DOTALL);
	private final static String KEYSTONE_CLIENT_CONF_DIR = "conf/";


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
		// getClass().getResource(KEYSTONE_CLIENT_CONF_DIR +
		// "keystone.conf");
		DEFAULT_CONFIG_FILENAME = URLUtils.Instance.getURL(KEYSTONE_CLIENT_CONF_DIR + "keystone.conf");
		FILE_OPTIONS.put(Config.Type.keystone_authtoken, "auth_admin_prefix", Options.newStrOpt("auth_admin_prefix", ""));
		FILE_OPTIONS.put(Config.Type.keystone_authtoken, "auth_host", Options.newStrOpt("auth_host", "127.0.0.1"));
		FILE_OPTIONS.put(Config.Type.keystone_authtoken, "auth_protocol", Options.newIntOpt("auth_port", 9999));
		FILE_OPTIONS.put(Config.Type.keystone_authtoken, "auth_port", Options.newStrOpt("auth_protocol", "https"));
		FILE_OPTIONS.put(Config.Type.keystone_authtoken, "auth_uri", Options.newStrOpt("auth_uri", ""));
		FILE_OPTIONS.put(Config.Type.keystone_authtoken, "identity_uri", Options.newStrOpt("identity_uri", ""));
		FILE_OPTIONS.put(Config.Type.keystone_authtoken, "auth_version", Options.newStrOpt("auth_version", ""));
		FILE_OPTIONS.put(Config.Type.keystone_authtoken, "delay_auth_decision",
				Options.newBoolOpt("delay_auth_decision", false));
		FILE_OPTIONS.put(Config.Type.keystone_authtoken, "http_request_max_retries",
				Options.newStrOpt("http_request_max_retries", ""));
		FILE_OPTIONS.put(Config.Type.keystone_authtoken, "admin_token", Options.newStrOpt("admin_token", true, ""));
		FILE_OPTIONS.put(Config.Type.keystone_authtoken, "admin_user", Options.newStrOpt("admin_user", ""));
		FILE_OPTIONS.put(Config.Type.keystone_authtoken, "admin_password", Options.newStrOpt("admin_password", true, ""));
		FILE_OPTIONS.put(Config.Type.keystone_authtoken, "cache", Options.newStrOpt("cache", ""));
		FILE_OPTIONS.put(Config.Type.keystone_authtoken, "certfile", Options.newStrOpt("certfile", ""));
		FILE_OPTIONS.put(Config.Type.keystone_authtoken, "keyfile", Options.newStrOpt("keyfile", ""));
		FILE_OPTIONS.put(Config.Type.keystone_authtoken, "cafile", Options.newStrOpt("cafile", ""));
		FILE_OPTIONS.put(Config.Type.keystone_authtoken, "insecure", Options.newBoolOpt("insecure", false));
		FILE_OPTIONS.put(Config.Type.keystone_authtoken, "signing_dir", Options.newStrOpt("signing_dir", ""));
		FILE_OPTIONS.put(Config.Type.keystone_authtoken, "memcached_servers",
				Options.newListOpt("memcached_servers", new ArrayList<String>()));
		FILE_OPTIONS.put(Config.Type.keystone_authtoken, "token_cache_time", Options.newIntOpt("token_cache_time", 300));
		FILE_OPTIONS.put(Config.Type.keystone_authtoken, "revocation_cache_time",
				Options.newIntOpt("revocation_cache_time", 300));
		FILE_OPTIONS.put(Config.Type.keystone_authtoken, "memcache_security_strategy",
				Options.newStrOpt("memcache_security_strategy", ""));
		FILE_OPTIONS.put(Config.Type.keystone_authtoken, "memcache_security_key",
				Options.newStrOpt("memcache_security_key", true, ""));
		FILE_OPTIONS.put(Config.Type.keystone_authtoken, "include_service_catalog",
				Options.newBoolOpt("include_service_catalog", true));
		FILE_OPTIONS.put(Config.Type.keystone_authtoken, "enforce_token_bind",
				Options.newStrOpt("enforce_token_bind", "permissive"));
	}

	private void parseConfigFiles() throws IOException {
		FileScanner scanner = new FileScanner(DEFAULT_CONFIG_FILENAME);
		Table<Type, String, String> customTable = scanner.read();
		for (Cell<Type, String, String> cell : customTable.cellSet()) {
			if (FILE_OPTIONS.contains(cell.getRowKey(), cell.getColumnKey())) {
				logger.debug("reset FILE_OPTIONS {}.{} = {}",
						new Object[] { cell.getRowKey(), cell.getColumnKey(), cell.getValue() });
				FILE_OPTIONS.get(cell.getRowKey(), cell.getColumnKey()).resetValue(cell.getValue());
			} else {
				FILE_OPTIONS.put(cell.getRowKey(), cell.getColumnKey(), Options.newStrOpt(cell.getValue()));
			}
		}
	}

	public Option getOpt(Type type, String attr) {
		Option option = FILE_OPTIONS.get(type, attr);
		if (option instanceof StringOption) {
			Option newOption = Options.newStrOpt(option.getName(), option.getValue());
			Matcher matcher = pattern.matcher(option.asText());
			while (matcher.find()) {
				String match = matcher.group(1);

				logger.debug("sub-option pattern match: {}", match);
				Option suboption = FILE_OPTIONS.get(type, match);
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
}
