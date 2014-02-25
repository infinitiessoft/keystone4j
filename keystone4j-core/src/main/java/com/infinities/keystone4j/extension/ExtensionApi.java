package com.infinities.keystone4j.extension;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.Sets;
import com.infinities.keystone4j.extension.model.Extension;
import com.infinities.keystone4j.extension.model.ExtensionWrapper;
import com.infinities.keystone4j.extension.model.ExtensionsWrapper;

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
