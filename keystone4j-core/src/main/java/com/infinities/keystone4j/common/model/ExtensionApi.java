package com.infinities.keystone4j.common.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.Sets;

public class ExtensionApi {

	private Map<String, Extension> extensions = new ConcurrentHashMap<String, Extension>(0);


	public ExtensionsWrapper getExtensionsInfo() {
		return new ExtensionsWrapper(Sets.newHashSet(extensions.values()));
	}

	public ExtensionWrapper getExtensionInfo(String alias) {
		return new ExtensionWrapper(extensions.get(alias));
	}

	public void registerExtension(String urlPrefix, Extension extension) {
		extensions.put(urlPrefix, extension);
	}
}
