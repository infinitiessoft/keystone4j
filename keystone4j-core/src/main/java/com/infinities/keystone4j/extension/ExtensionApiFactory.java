package com.infinities.keystone4j.extension;

import org.glassfish.hk2.api.Factory;

public class ExtensionApiFactory implements Factory<ExtensionApi> {

	private static ExtensionApi extensionApi;


	public ExtensionApiFactory() {
		extensionApi = new ExtensionApi();
	}

	@Override
	public void dispose(ExtensionApi arg0) {

	}

	@Override
	public ExtensionApi provide() {
		return extensionApi;
	}

}
