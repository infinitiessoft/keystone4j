package com.infinities.keystone4j.middleware;
///*******************************************************************************
// * Copyright 2015 InfinitiesSoft Solutions Inc.
// *
// * Licensed under the Apache License, Version 2.0 (the "License"); you may
// * not use this file except in compliance with the License. You may obtain
// * a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
// * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
// * License for the specific language governing permissions and limitations
// * under the License.
// *******************************************************************************/
//
//package com.infinities.keystonemiddleware;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.util.Properties;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class PropertiesHolder {
//
//	private static final Logger logger = LoggerFactory.getLogger(PropertiesHolder.class);
//	private static final String SKYPORT_PROPERTIES_FILE = "keystone.properties";
//	private final Properties skyportProperties = new Properties();
//	public static final String CONFIG_FOLDER = "config";
//	public static final String UI_ENABLED = "ui.enabled";
//	// public static final String MAX_PORT = "vnc.max";
//	// public static final String MIN_PORT = "vnc.min";
//	public static final String KEY = "profile.key";
//	// public static final String WEBAPI_CONTEXT = "webapi.context";
//	public static final String CLUSTER_DELEGATE = "cluster.delegate";
//	public static final String TIMEZONE = "timezone";
//	public static final String CACHE_ENABLED = "cache.enabled";
//	public static final String LIMIT_MAX = "limit.max";
//	public static final String THREADPOOL_CORE_MIN = "threadpool.core.min";
//	public static final String THREADPOOL_CORE_MAX = "threadpool.core.max";
//	public static final String THREADPOOL_MAX_MIN = "threadpool.max.min";
//	public static final String THREADPOOL_MAX_MAX = "threadpool.max.max";
//	public static final String THREADPOOL_CAPACITY_MIN = "threadpool.capacity.min";
//	public static final String THREADPOOL_CAPACITY_MAX = "threadpool.capacity.max";
//	public static final String DELAY_MIN = "delay.min";
//	public static final String DELAY_MAX = "delay.max";
//	public static final String TIMEOUT_MIN = "timeout.min";
//	public static final String TIMEOUT_MAX = "timeout.max";
//
//	private static PropertiesHolder instance = new PropertiesHolder();
//
//
//	private PropertiesHolder() {
//		String configFileLocation = CONFIG_FOLDER + File.separator + SKYPORT_PROPERTIES_FILE;
//		try {
//			skyportProperties.load(new FileInputStream(configFileLocation));
//		} catch (FileNotFoundException e) {
//			String message = configFileLocation + " file is not found";
//			logger.warn(message, e);
//			throw new RuntimeException(message, e);
//		} catch (IOException e) {
//			String message = "Error while reading" + configFileLocation + " file";
//			logger.warn(message, e);
//			throw new RuntimeException(message, e);
//		}
//	}
//
//	public static String getProperty(String key) {
//		return instance.skyportProperties.getProperty(key);
//	}
//
// }
