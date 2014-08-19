package com.infinities.keystone4j.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public enum URLUtils {
	Instance;

	public URL getURL(String filePath) {
		File file = new File(filePath);
		try {
			return file.toURI().toURL();
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

}
