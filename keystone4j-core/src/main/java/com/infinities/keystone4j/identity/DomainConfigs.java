package com.infinities.keystone4j.identity;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.common.Config.Type;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.option.Option;

public class DomainConfigs {

	private boolean configured = false;
	private IdentityDriver driver = null;
	private boolean anySql = false;
	private final static Logger logger = LoggerFactory.getLogger(DomainConfigs.class);
	private final static String DOMAIN_CONF_FHEAD = "keystone.";
	private final static String DOMAIN_CONF_FTAIL = ".conf";

	private final Map<String, DomainConfig> configs = new HashMap<String, DomainConfig>();


	private IdentityDriver loadDriver(DomainConfig domainConfig, AssignmentApi assignmentApi) throws ClassNotFoundException,
			NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Class<?> c = Class.forName(domainConfig.getCfg().get(Type.identity, "driver").asText());

		Class<?>[] oParam = new Class[1];
		oParam[0] = Table.class;
		Constructor<?> constructor = c.getConstructor(oParam);

		Object[] paramObjs = new Object[0];
		paramObjs[0] = domainConfig.getCfg();

		IdentityDriver domainConfigDriver = (IdentityDriver) constructor.newInstance(paramObjs);
		domainConfigDriver.setAssignmentApi(assignmentApi);
		return domainConfigDriver;
	}

	private void loadConfig(AssignmentApi assignmentApi, List<String> fileList, String domainName)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Domain domainRef = null;
		try {
			domainRef = assignmentApi.getDomainByName(domainName);
		} catch (Exception e) {
			logger.warn("Invalid domain name ({}) found in config file name", domainName);
			return;
		}

		DomainConfig domainConfig = new DomainConfig();
		Config.Instance.configure(domainConfig.getCfg());
		domainConfig.setDriver(loadDriver(domainConfig, assignmentApi));
		assertNoMoreThanOneSqlDriver(domainConfig, fileList);
		configs.put(domainRef.getId(), domainConfig);
	}

	public void setupDomainDrivers(IdentityDriver standardDriver, AssignmentApi assignmentApi)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		this.setConfigured(true);
		this.driver = standardDriver;

		String confDir = Config.Instance.getOpt(Config.Type.identity, "domain_config_dir").asText();
		File configDir = new File(confDir);
		if (!configDir.exists() || !configDir.isDirectory()) {
			logger.warn("Unable to locate domain config directory: %s", confDir);
			return;
		}

		for (File file : configDir.listFiles()) {
			if (file.getName().startsWith(DOMAIN_CONF_FHEAD) && file.getName().endsWith(DOMAIN_CONF_FTAIL) && file.isFile()) {
				if (file.getName().split("\\.").length >= 2) {
					List<String> fnames = new ArrayList<String>();
					fnames.add(file.getAbsolutePath());
					loadConfig(assignmentApi, fnames,
							file.getName().replace(DOMAIN_CONF_FHEAD, "").replace(DOMAIN_CONF_FTAIL, ""));
				} else {
					logger.debug("Ignoring file {} while scanning domain config directory", file.getName());
				}
			}
		}

	}

	private void assertNoMoreThanOneSqlDriver(DomainConfig newConfig, List<String> configFile) {
		if (newConfig.getDriver().isSql() && (this.driver.isSql() || anySql)) {
			throw Exceptions.MultipleSQLDriversInConfigException.getInstance(null, configFile);
		}
		anySql = newConfig.getDriver().isSql();
	}


	private static class DomainConfig {

		private final Table<Type, String, Option> cfg = HashBasedTable.create();
		private IdentityDriver driver;


		public Table<Type, String, Option> getCfg() {
			return cfg;
		}

		public IdentityDriver getDriver() {
			return driver;
		}

		public void setDriver(IdentityDriver driver) {
			this.driver = driver;
		}

	}


	public IdentityDriver getDomainDriver(String domainId) {
		if (configs.containsKey(domainId)) {
			return configs.get(domainId).getDriver();
		}
		return null;
	}

	public boolean isConfigured() {
		return configured;
	}

	public void setConfigured(boolean configured) {
		this.configured = configured;
	}

}
