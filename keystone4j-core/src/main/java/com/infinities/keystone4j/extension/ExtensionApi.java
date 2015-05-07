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
