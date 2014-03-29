package com.infinities.keystone4j.common;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;
import com.infinities.keystone4j.FileScanner;
import com.infinities.keystone4j.option.Option;
import com.infinities.keystone4j.option.Options;
import com.infinities.keystone4j.option.StringOption;

public enum Config {
	Instance;

	public enum Type {
		assignment, auth, cache, catalog, credential, database, ec2, endpoint_filter, federation, identity, kvs, ldap, memcache, pam, paste_deploy, policy, signing, ssl, stats, sql, token, oauth1, os_inherit, DEFAULT, trust;
	}


	// private final String[] DEFAULT_AUTH_METHODS = { "external", "password",
	// "token" };
	private final String[] DEFAULT_AUTH_METHODS = { "password", "token" };
	private final Logger logger = LoggerFactory.getLogger(Config.class);
	private URL DEFAULT_CONFIG_FILENAME;
	private final Table<Type, String, Option> FILE_OPTIONS = HashBasedTable.create();
	private final Pattern pattern = Pattern.compile("%\\((.*?)\\)", Pattern.DOTALL);


	private Config() {
		try {
			preSetup();
			parseConfigFiles();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void preSetup() {
		DEFAULT_CONFIG_FILENAME = Config.class.getResource("/keystone.conf");

		FILE_OPTIONS.put(Config.Type.DEFAULT, "admin_token", Options.newStrOpt("admin_token", true, "ADMIN"));
		FILE_OPTIONS.put(Config.Type.DEFAULT, "public_bind_host", Options.newStrOpt("public_bind_host", "0.0.0.0"));
		FILE_OPTIONS.put(Config.Type.DEFAULT, "admin_bind_host", Options.newStrOpt("admin_bind_host", "0.0.0.0"));
		FILE_OPTIONS.put(Config.Type.DEFAULT, "compute_port", Options.newIntOpt("compute_port", 8774));
		FILE_OPTIONS.put(Config.Type.DEFAULT, "admin_port", Options.newIntOpt("admin_port", 35357));
		FILE_OPTIONS.put(Config.Type.DEFAULT, "public_port", Options.newIntOpt("public_port", 5000));
		FILE_OPTIONS.put(Config.Type.DEFAULT, "public_endpoint",
				Options.newStrOpt("public_endpoint", "http://localhost:%(public_port)/"));
		FILE_OPTIONS.put(Config.Type.DEFAULT, "admin_endpoint",
				Options.newStrOpt("admin_endpoint", "http://localhost:%(admin_port)/"));
		FILE_OPTIONS.put(Config.Type.DEFAULT, "onready", Options.newStrOpt("onready"));
		FILE_OPTIONS.put(Config.Type.DEFAULT, "max_request_body_size", Options.newIntOpt("max_request_body_size", 114688));
		FILE_OPTIONS.put(Config.Type.DEFAULT, "max_param_size", Options.newIntOpt("max_param_size", 64));
		FILE_OPTIONS.put(Config.Type.DEFAULT, "max_token_size", Options.newIntOpt("max_token_size", 8192));
		FILE_OPTIONS.put(Config.Type.DEFAULT, "member_role_id",
				Options.newStrOpt("member_role_id", "9fe2ff9ee4384b1894a90878d3e92bab"));
		FILE_OPTIONS.put(Config.Type.DEFAULT, "member_role_name", Options.newStrOpt("member_role_name", "_member_"));
		FILE_OPTIONS.put(Config.Type.DEFAULT, "crypt_strength", Options.newIntOpt("crypt_strength", 40000));
		FILE_OPTIONS.put(Config.Type.DEFAULT, "tcp_keepalive", Options.newBoolOpt("tcp_keepalive", false));
		FILE_OPTIONS.put(Config.Type.DEFAULT, "tcp_keepidle", Options.newIntOpt("tcp_keepidle", 600));
		FILE_OPTIONS.put(Config.Type.DEFAULT, "policy_file", Options.newStrOpt("policy_file", "/policy.json"));
		FILE_OPTIONS.put(Config.Type.DEFAULT, "domain_id_immutable", Options.newBoolOpt("domain_id_immutable", false));

		FILE_OPTIONS.put(Config.Type.identity, "default_domain_id", Options.newStrOpt("default_domain_id", "default"));
		FILE_OPTIONS.put(Config.Type.identity, "domain_specific_drivers_enabled",
				Options.newBoolOpt("domain_specific_drivers_enabled", false));
		FILE_OPTIONS.put(Config.Type.identity, "domain_config_dir",
				Options.newStrOpt("domain_config_dir", "/etc/keystone/domains"));
		FILE_OPTIONS.put(Config.Type.identity, "driver",
				Options.newStrOpt("driver", "com.infinities.keystone4j.identity.driver.IdentityJpaDriver"));
		FILE_OPTIONS.put(Config.Type.identity, "max_password_length", Options.newIntOpt("max_password_length", 4096));
		FILE_OPTIONS.put(Config.Type.identity, "list_limit", Options.newIntOpt("list_limit", -1));

		FILE_OPTIONS.put(Config.Type.trust, "enabled", Options.newBoolOpt("enabled", true));
		FILE_OPTIONS.put(Config.Type.trust, "driver",
				Options.newStrOpt("driver", "com.infinities.keystone4j.trust.driver.TrustJpaDriver"));

		FILE_OPTIONS.put(Config.Type.os_inherit, "enabled", Options.newBoolOpt("enabled", false));

		FILE_OPTIONS.put(Config.Type.token, "bind", Options.newListOpt("bind", new ArrayList<String>()));
		FILE_OPTIONS.put(Config.Type.token, "enforce_token_bind", Options.newStrOpt("enforce_token_bind", "permissive"));
		FILE_OPTIONS.put(Config.Type.token, "expiration", Options.newIntOpt("expiration", 86400));
		FILE_OPTIONS.put(Config.Type.token, "provider",
				Options.newStrOpt("provider", "com.infinities.keystone4j.token.provider.driver.PkiProvider"));
		FILE_OPTIONS.put(Config.Type.token, "driver",
				Options.newStrOpt("driver", "com.infinities.keystone4j.token.driver.TokenJpaDriver"));
		FILE_OPTIONS.put(Config.Type.token, "caching", Options.newBoolOpt("caching", true));
		FILE_OPTIONS.put(Config.Type.token, "revocation_cache_time", Options.newIntOpt("revocation_cache_time", 3600));
		FILE_OPTIONS.put(Config.Type.token, "cache_time", Options.newIntOpt("cache_time", 0));

		FILE_OPTIONS.put(Config.Type.cache, "config_prefix", Options.newStrOpt("config_prefix", "cache.keystone"));
		FILE_OPTIONS.put(Config.Type.cache, "expiration_time", Options.newIntOpt("expiration_time", 600));
		FILE_OPTIONS.put(Config.Type.cache, "backend", Options.newStrOpt("backend", "keystone.common.cache.noop"));
		FILE_OPTIONS.put(Config.Type.cache, "use_key_mangler", Options.newBoolOpt("use_key_mangler", true));
		FILE_OPTIONS.put(Config.Type.cache, "backend_argument",
				Options.newListOpt("backend_argument", new ArrayList<String>()));
		FILE_OPTIONS.put(Config.Type.cache, "proxies", Options.newListOpt("proxies", new ArrayList<String>()));
		FILE_OPTIONS.put(Config.Type.cache, "enabled", Options.newBoolOpt("enabled", false));
		FILE_OPTIONS.put(Config.Type.cache, "debug_cache_backend", Options.newBoolOpt("debug_cache_backend", false));

		FILE_OPTIONS.put(Config.Type.ssl, "enabled", Options.newBoolOpt("enabled", false));
		FILE_OPTIONS.put(Config.Type.ssl, "cerfile", Options.newStrOpt("cerfile", "keystone.pem"));
		FILE_OPTIONS.put(Config.Type.ssl, "keyfile", Options.newStrOpt("keyfile", "keystonekey.pem"));
		FILE_OPTIONS.put(Config.Type.ssl, "ca_certs", Options.newStrOpt("ca_certs", "ca.pem"));
		FILE_OPTIONS.put(Config.Type.ssl, "ca_key", Options.newStrOpt("ca_key", "cakey.pem"));
		FILE_OPTIONS.put(Config.Type.ssl, "cert_required", Options.newBoolOpt("cert_required", false));
		FILE_OPTIONS.put(Config.Type.ssl, "key_size", Options.newIntOpt("key_size", 1024));
		FILE_OPTIONS.put(Config.Type.ssl, "valid_days", Options.newIntOpt("valid_days", 3650));
		FILE_OPTIONS.put(Config.Type.ssl, "cert_subject",
				Options.newStrOpt("cert_subject", "/C=US/ST=Unset/L=Unset/O=Unset/CN=localhost"));

		FILE_OPTIONS.put(Config.Type.signing, "token_format", Options.newStrOpt("token_format", ""));
		FILE_OPTIONS.put(Config.Type.signing, "certfile", Options.newStrOpt("certfile", "/signing_cert_req.pem"));
		FILE_OPTIONS.put(Config.Type.signing, "keyfile", Options.newStrOpt("keyfile", "/signing_key.pem"));
		FILE_OPTIONS.put(Config.Type.signing, "ca_certs", Options.newStrOpt("ca_certs", "ca.pem"));
		FILE_OPTIONS.put(Config.Type.signing, "ca_key", Options.newStrOpt("ca_key", "cakey.pem"));
		FILE_OPTIONS.put(Config.Type.signing, "key_size", Options.newIntOpt("key_size", 2048));
		FILE_OPTIONS.put(Config.Type.signing, "valid_days", Options.newIntOpt("valid_days", 3650));
		FILE_OPTIONS.put(Config.Type.signing, "cert_subject",
				Options.newStrOpt("cert_subject", "/C=US/ST=Unset/L=Unset/O=Unset/CN=www.example.com"));

		FILE_OPTIONS.put(Config.Type.assignment, "driver",
				Options.newStrOpt("driver", "com.infinities.keystone4j.assignment.driver.AssignmentJpaDriver"));
		FILE_OPTIONS.put(Config.Type.assignment, "caching", Options.newBoolOpt("caching", true));
		FILE_OPTIONS.put(Config.Type.assignment, "cache_time", Options.newIntOpt("cache_time", -1));
		FILE_OPTIONS.put(Config.Type.assignment, "list_limit", Options.newIntOpt("list_limit", -1));

		FILE_OPTIONS.put(Config.Type.credential, "driver",
				Options.newStrOpt("driver", "com.infinities.keystone4j.credential.driver.CredentialJpaDriver"));

		// TDOD oauth driver unimplemented
		FILE_OPTIONS.put(Config.Type.oauth1, "driver",
				Options.newStrOpt("driver", "keystone.contrib.oauth1.backends.sql.OAuth1"));
		FILE_OPTIONS.put(Config.Type.oauth1, "request_token_duration", Options.newIntOpt("request_token_duration", 28800));
		FILE_OPTIONS.put(Config.Type.oauth1, "access_token_duration", Options.newIntOpt("access_token_duration", 86400));

		// TDOD federation driver unimplemented
		FILE_OPTIONS.put(Config.Type.federation, "driver",
				Options.newStrOpt("driver", "keystone.contrib.federation.backends.sql.Federation"));

		FILE_OPTIONS.put(Config.Type.policy, "driver",
				Options.newStrOpt("driver", "com.infinities.keystone4j.policy.driver.PolicyJpaDriver"));
		FILE_OPTIONS.put(Config.Type.policy, "list_limit", Options.newIntOpt("list_limit", -1));

		// TDOD ec2 driver unimplemented
		FILE_OPTIONS.put(Config.Type.ec2, "driver", Options.newStrOpt("driver", "keystone.contrib.ec2.backends.kvs.Ec2"));

		// TDOD endpointerfilter driver unimplemented
		FILE_OPTIONS.put(Config.Type.endpoint_filter, "driver",
				Options.newStrOpt("driver", "keystone.contrib.endpoint_filter.backends.sql.EndpointFilter"));
		FILE_OPTIONS.put(Config.Type.endpoint_filter, "return_all_endpoints_id_no_filter",
				Options.newBoolOpt("return_all_endpoints_id_no_filter", true));

		// TDOD stats driver unimplemented
		FILE_OPTIONS.put(Config.Type.stats, "driver",
				Options.newStrOpt("driver", "keystone.contrib.stats.backends.kvs.Stats"));

		FILE_OPTIONS.put(Config.Type.ldap, "url", Options.newStrOpt("url", "ldap://localhost"));
		FILE_OPTIONS.put(Config.Type.ldap, "user", Options.newStrOpt("user", ""));
		FILE_OPTIONS.put(Config.Type.ldap, "password", Options.newStrOpt("password", true, ""));
		FILE_OPTIONS.put(Config.Type.ldap, "suffix", Options.newStrOpt("suffix", "cn=example,cn=com"));
		FILE_OPTIONS.put(Config.Type.ldap, "user_dump_member", Options.newBoolOpt("user_dump_member", false));
		FILE_OPTIONS.put(Config.Type.ldap, "dump_member", Options.newStrOpt("dump_member", "cn=dumb,dc=nonexistent"));
		FILE_OPTIONS.put(Config.Type.ldap, "allow_subtree_delete", Options.newBoolOpt("allow_subtree_delete", false));
		FILE_OPTIONS.put(Config.Type.ldap, "query_scope", Options.newStrOpt("query_scope", "one"));
		FILE_OPTIONS.put(Config.Type.ldap, "page_siz", Options.newIntOpt("page_siz", 0));
		FILE_OPTIONS.put(Config.Type.ldap, "alias_dereferencing", Options.newStrOpt("alias_dereferencing", "default"));
		FILE_OPTIONS.put(Config.Type.ldap, "user_tree_dn", Options.newStrOpt("user_tree_dn", ""));
		FILE_OPTIONS.put(Config.Type.ldap, "user_filter", Options.newStrOpt("user_filter", ""));
		FILE_OPTIONS.put(Config.Type.ldap, "user_objectclass", Options.newStrOpt("user_objectclass", "inetOrgPerson"));
		FILE_OPTIONS.put(Config.Type.ldap, "user_id_attribute", Options.newStrOpt("user_id_attribute", "cn"));
		FILE_OPTIONS.put(Config.Type.ldap, "user_name_attribute", Options.newStrOpt("user_name_attribute", "sn"));
		FILE_OPTIONS.put(Config.Type.ldap, "user_mail_attribute", Options.newStrOpt("user_mail_attribute", "email"));
		FILE_OPTIONS.put(Config.Type.ldap, "user_pass_attribute", Options.newStrOpt("user_pass_attribute", "userPassword"));
		FILE_OPTIONS.put(Config.Type.ldap, "user_enabled_attribute", Options.newStrOpt("user_enabled_attribute", "enabled"));
		FILE_OPTIONS.put(Config.Type.ldap, "user_enabled_mask", Options.newIntOpt("user_enabled_mask", 0));
		FILE_OPTIONS.put(Config.Type.ldap, "user_enabled_default", Options.newBoolOpt("user_enabled_default", true));
		FILE_OPTIONS.put(Config.Type.ldap, "user_attribute_ignore",
				Options.newListOpt("user_attribute_ignore", Arrays.asList(new String[] { "default_project_id,tenants" })));
		FILE_OPTIONS.put(Config.Type.ldap, "user_additional_attribute_mapping",
				Options.newStrOpt("user_additional_attribute_mapping", ""));
		FILE_OPTIONS.put(Config.Type.ldap, "tenant_tree_dn", Options.newStrOpt("tenant_tree_dn", ""));
		FILE_OPTIONS.put(Config.Type.ldap, "tenant_filter", Options.newStrOpt("tenant_filter", ""));
		FILE_OPTIONS.put(Config.Type.ldap, "tenant_objectclass", Options.newStrOpt("tenant_objectclass", "groupOfNames"));
		FILE_OPTIONS.put(Config.Type.ldap, "tenant_id_attribute", Options.newStrOpt("tenant_id_attribute", "cn"));
		FILE_OPTIONS
				.put(Config.Type.ldap, "tenant_member_attribute", Options.newStrOpt("tenant_member_attribute", "member"));
		FILE_OPTIONS.put(Config.Type.ldap, "tenant_name_attribute", Options.newStrOpt("tenant_name_attribute", "ou"));
		FILE_OPTIONS.put(Config.Type.ldap, "tenant_desc_attribute",
				Options.newStrOpt("tenant_desc_attribute", "description"));
		FILE_OPTIONS.put(Config.Type.ldap, "tenant_enabled_attribute",
				Options.newStrOpt("tenant_enabled_attribute", "enabled"));
		FILE_OPTIONS.put(Config.Type.ldap, "tenant_domain_id_attribute",
				Options.newStrOpt("tenant_domain_id_attribute", "businessCategory"));
		FILE_OPTIONS.put(Config.Type.ldap, "tenant_attribute_ignore",
				Options.newListOpt("tenant_attribute_ignore", new ArrayList<String>()));
		FILE_OPTIONS.put(Config.Type.ldap, "tenant_allow_create", Options.newBoolOpt("tenant_allow_create", true));
		FILE_OPTIONS.put(Config.Type.ldap, "tenant_allow_update", Options.newBoolOpt("tenant_allow_update", true));
		FILE_OPTIONS.put(Config.Type.ldap, "tenant_allow_delete", Options.newBoolOpt("tenant_allow_delete", true));
		FILE_OPTIONS
				.put(Config.Type.ldap, "tenant_enabled_emulation", Options.newBoolOpt("tenant_enabled_emulation", false));
		FILE_OPTIONS.put(Config.Type.ldap, "tenant_enabled_emulation_dn",
				Options.newStrOpt("tenant_enabled_emulation_dn", ""));
		FILE_OPTIONS.put(Config.Type.ldap, "tenant_additional_attribute_mapping",
				Options.newListOpt("tenant_additional_attribute_mapping", new ArrayList<String>()));
		FILE_OPTIONS.put(Config.Type.ldap, "role_tree_dn", Options.newStrOpt("role_tree_dn", ""));
		FILE_OPTIONS.put(Config.Type.ldap, "role_filter", Options.newStrOpt("role_filter", ""));
		FILE_OPTIONS.put(Config.Type.ldap, "role_objectclass", Options.newStrOpt("role_objectclass", "organizationalRole"));
		FILE_OPTIONS.put(Config.Type.ldap, "role_id_attribute", Options.newStrOpt("role_id_attribute", "cn"));
		FILE_OPTIONS.put(Config.Type.ldap, "role_member_attribute",
				Options.newStrOpt("role_member_attribute", "roleOccupant"));
		FILE_OPTIONS.put(Config.Type.ldap, "role_name_attribute", Options.newStrOpt("role_name_attribute", "ou"));
		FILE_OPTIONS.put(Config.Type.ldap, "role_allow_create", Options.newBoolOpt("role_allow_create", true));
		FILE_OPTIONS.put(Config.Type.ldap, "role_allow_update", Options.newBoolOpt("role_allow_update", true));
		FILE_OPTIONS.put(Config.Type.ldap, "role_allow_delete", Options.newBoolOpt("role_allow_delete", true));
		FILE_OPTIONS.put(Config.Type.ldap, "role_additional_attribute_mapping",
				Options.newListOpt("role_additional_attribute_mapping", new ArrayList<String>()));
		FILE_OPTIONS.put(Config.Type.ldap, "group_tree_dn", Options.newStrOpt("group_tree_dn", ""));
		FILE_OPTIONS.put(Config.Type.ldap, "group_filter", Options.newStrOpt("group_filter", ""));
		FILE_OPTIONS.put(Config.Type.ldap, "group_objectclass", Options.newStrOpt("group_objectclass", "groupOfNamed"));
		FILE_OPTIONS.put(Config.Type.ldap, "group_id_attribute", Options.newStrOpt("group_id_attribute", "cn"));
		FILE_OPTIONS.put(Config.Type.ldap, "group_member_attribute", Options.newStrOpt("group_member_attribute", "member"));
		FILE_OPTIONS.put(Config.Type.ldap, "group_name_attribute", Options.newStrOpt("group_name_attribute", "ou"));
		FILE_OPTIONS.put(Config.Type.ldap, "group_desc_attribute", Options.newStrOpt("group_desc_attribute", "description"));
		FILE_OPTIONS.put(Config.Type.ldap, "group_attribute_ignore",
				Options.newListOpt("group_attribute_ignore", new ArrayList<String>()));
		FILE_OPTIONS.put(Config.Type.ldap, "group_allow_create", Options.newBoolOpt("group_allow_create", true));
		FILE_OPTIONS.put(Config.Type.ldap, "group_allow_update", Options.newBoolOpt("group_allow_update", true));
		FILE_OPTIONS.put(Config.Type.ldap, "group_allow_delete", Options.newBoolOpt("group_allow_delete", true));
		FILE_OPTIONS.put(Config.Type.ldap, "group_additional_attribute_mapping",
				Options.newListOpt("group_additional_attribute_mapping", new ArrayList<String>()));
		FILE_OPTIONS.put(Config.Type.ldap, "tls_cacertfile", Options.newStrOpt("tls_cacertfile", ""));
		FILE_OPTIONS.put(Config.Type.ldap, "tls_cacertdir", Options.newStrOpt("tls_cacertdir", ""));
		FILE_OPTIONS.put(Config.Type.ldap, "use_tls", Options.newBoolOpt("use_tls", false));
		FILE_OPTIONS.put(Config.Type.ldap, "tls_req_cert", Options.newStrOpt("tls_req_cert", "demand"));

		FILE_OPTIONS.put(Config.Type.pam, "userid", Options.newStrOpt("userid", ""));
		FILE_OPTIONS.put(Config.Type.pam, "password", Options.newStrOpt("password", ""));

		FILE_OPTIONS.put(Config.Type.auth, "methods", Options.newListOpt("methods", Arrays.asList(DEFAULT_AUTH_METHODS)));
		FILE_OPTIONS.put(Config.Type.auth, "password",
				Options.newStrOpt("password", "com.infinities.keystone4j.auth.driver.PasswordAuthDriver"));
		FILE_OPTIONS.put(Config.Type.auth, "token",
				Options.newStrOpt("token", "com.infinities.keystone4j.auth.driver.TokenAuthDriver"));

		// TDOD external driver unimplemented
		FILE_OPTIONS.put(Config.Type.auth, "external",
				Options.newStrOpt("external", "keystone.auth.plugins.external.DefaultDomain"));

		FILE_OPTIONS.put(Config.Type.paste_deploy, "config_file", Options.newStrOpt("config_file", ""));

		FILE_OPTIONS.put(Config.Type.memcache, "servers",
				Options.newListOpt("servers", Arrays.asList(new String[] { "localhost:11211" })));
		FILE_OPTIONS.put(Config.Type.memcache, "max_compare_and_set_retry",
				Options.newIntOpt("max_compare_and_set_retry", 16));

		FILE_OPTIONS.put(Config.Type.catalog, "template_file",
				Options.newStrOpt("template_file", "default_catalog.template"));
		FILE_OPTIONS.put(Config.Type.catalog, "driver",
				Options.newStrOpt("driver", "com.infinities.keystone4j.catalog.driver.CatalogJpaDriver"));
		FILE_OPTIONS.put(Config.Type.catalog, "list_limit", Options.newIntOpt("list_limit", -1));

		FILE_OPTIONS.put(Config.Type.kvs, "backends", Options.newListOpt("backends", new ArrayList<String>()));
		FILE_OPTIONS.put(Config.Type.kvs, "config_prefix", Options.newStrOpt("config_prefix", "keystone.kvs"));
		FILE_OPTIONS.put(Config.Type.kvs, "enable_key_mangler", Options.newBoolOpt("enable_key_mangler", true));
		FILE_OPTIONS.put(Config.Type.kvs, "default_lock_timeout", Options.newIntOpt("default_lock_timeout", 5));

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
