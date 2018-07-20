/*******************************************************************************
 * Copyright 2015 InfinitiesSoft Solutions Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package com.infinities.keystone4j.middleware;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import com.infinities.keystone4j.middleware.exception.InvalidUserTokenException;
import com.infinities.keystone4j.middleware.model.TokenWrapper;
import com.infinities.keystone4j.utils.Cms;

public class TokenCache {

	private final static Logger logger = LoggerFactory.getLogger(TokenCache.class);
	private static String CACHE_KEY_TEMPLATE = "tokens/%s";
	// private static String INVALID_INDICATOR = "invalid";
	private final int cacheTime;
	private final List<String> hashAlgorithms;
	// private String envCacheName;
	// private List<String> memcachedServers;
	private final String memcacheSecurityStrategy;
	private final String memcacheSecretKey;
	private Cache<String, TokenWrapperCache> cachePool;
	private boolean initialized = false;


	public TokenCache(int token_cache_time, List<String> hash_algorithms, String cache, List<String> memcached_servers,
			String memcacheSecurityStrategy, String memcache_secret_key) {
		this.cacheTime = token_cache_time;
		this.hashAlgorithms = hash_algorithms;
		// this.envCacheName = cache;
		// this.memcachedServers = memcached_servers;
		this.memcacheSecurityStrategy = memcacheSecurityStrategy;

		if (!Strings.isNullOrEmpty(memcacheSecurityStrategy)) {
			memcacheSecurityStrategy = memcacheSecurityStrategy.toUpperCase();
		}

		this.memcacheSecretKey = memcache_secret_key;
		this.cachePool = null;
		this.initialized = false;
		assertValidMemcacheProtectionConfig();
	}

	public void initialize() {
		if (initialized) {
			return;
		}

		// cachePool = new CachedPool(env.getProperty(envCacheName),
		// this.memcachedServers);
		cachePool = CacheBuilder.newBuilder().expireAfterAccess(cacheTime, TimeUnit.SECONDS).build();
		this.initialized = true;
	}

	public Entry<List<String>, TokenWrapper> get(String userToken) {
		if (Cms.isAsn1Token(userToken)) {
			List<String> tokenHashes = new ArrayList<String>();
			for (String algo : this.hashAlgorithms) {
				tokenHashes.add(Cms.cmsHashToken(userToken, Cms.Algorithm.valueOf(algo)));
			}

			for (String tokenHash : tokenHashes) {
				TokenWrapper cached = cacheGet(tokenHash);
				if (cached != null) {
					return Maps.immutableEntry(tokenHashes, cached);
				}
			}

			return Maps.immutableEntry(tokenHashes, null);
		}

		String tokenid = userToken;
		TokenWrapper cached = cacheGet(tokenid);
		List<String> tokens = new ArrayList<String>();
		tokens.add(tokenid);

		return Maps.immutableEntry(tokens, cached);
	}

	public void store(String tokenid, TokenWrapper data, Calendar expires) {
		logger.debug("Storing token in cache");

		TokenWrapperCache cache = new TokenWrapperCache();
		cache.setCalendar(expires);
		cache.setToken(data);
		cache.setValid(true);

		cacheStore(tokenid, cache);
	}

	public void storeInvalid(String tokenid) {
		logger.debug("Marking token as unauthorized in cache");
		TokenWrapperCache cache = new TokenWrapperCache();
		cache.setValid(false);
		cacheStore(tokenid, cache);
	}

	private void assertValidMemcacheProtectionConfig() {
		if (!Strings.isNullOrEmpty(this.memcacheSecurityStrategy)) {
			if (!"MAC".equals(memcacheSecurityStrategy) && !"ENCRYPT".equals(memcacheSecurityStrategy)) {
				throw new IllegalStateException("memcache security strategy must be ENCRYPT or MAC");
			}

			if (Strings.isNullOrEmpty(memcacheSecretKey)) {
				throw new IllegalStateException(
						"memcache security key must be defined when a memcache_security_strategy is defined");
			}
		}
	}

	private TokenWrapper cacheGet(String tokenid) {
		if (Strings.isNullOrEmpty(tokenid)) {
			return null;
		}

		String key = String.format(TokenCache.CACHE_KEY_TEMPLATE, tokenid);
		TokenWrapperCache cache = cachePool.getIfPresent(key);
		if (cache == null) {
			return null;
		}

		if (!cache.isValid()) {
			logger.debug("Cached Token is marked unauthorized");
			throw new InvalidUserTokenException("Token authorization failed");
		}

		TokenWrapper data = cache.getToken();
		Calendar expires = cache.getCalendar();

		Calendar utcnow = Calendar.getInstance();

		if (expires.after(utcnow)) {
			logger.debug("Returning cached token");
			return data;
		} else {
			logger.debug("Cached Token seems expired");
			throw new InvalidUserTokenException("Token authorization failed");
		}
	}

	private void cacheStore(String tokenid, TokenWrapperCache cache) {
		String cacheKey = String.format(CACHE_KEY_TEMPLATE, tokenid);
		cachePool.put(cacheKey, cache);
	}


	private static class TokenWrapperCache {

		private TokenWrapper token;
		private Calendar calendar;
		private boolean valid;


		public TokenWrapper getToken() {
			return token;
		}

		public void setToken(TokenWrapper token) {
			this.token = token;
		}

		public Calendar getCalendar() {
			return calendar;
		}

		public void setCalendar(Calendar calendar) {
			this.calendar = calendar;
		}

		public boolean isValid() {
			return valid;
		}

		public void setValid(boolean valid) {
			this.valid = valid;
		}

	}
}
