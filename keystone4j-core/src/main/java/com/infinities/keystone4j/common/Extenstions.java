package com.infinities.keystone4j.common;

import com.infinities.keystone4j.common.model.ExtensionApi;

public class Extenstions {

	private ExtensionApi adminExtensions;
	private ExtensionApi publicExtensions;


	public ExtensionApi getPublicExtensions() {
		return publicExtensions;
	}

	public void setPublicExtensions(ExtensionApi publicExtensions) {
		this.publicExtensions = publicExtensions;
	}

	public ExtensionApi getAdminExtensions() {
		return adminExtensions;
	}

	public void setAdminExtensions(ExtensionApi adminExtensions) {
		this.adminExtensions = adminExtensions;
	}

}
