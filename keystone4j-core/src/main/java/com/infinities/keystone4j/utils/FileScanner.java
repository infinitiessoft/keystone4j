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
package com.infinities.keystone4j.utils;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.common.Config.Type;

public class FileScanner {

	private final static Logger logger = LoggerFactory.getLogger(FileScanner.class);
	private final URL url;


	public FileScanner(URL url) {
		this.url = url;
	}

	public Table<Type, String, String> read() throws IOException {
		logger.debug("Readig from file.");
		Table<Type, String, String> table = HashBasedTable.create();
		Scanner scanner = new Scanner(url.openStream());
		try {
			Config.Type type = Config.Type.DEFAULT;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (!line.startsWith("#") && !line.isEmpty()) {
					if (line.startsWith("[") && line.endsWith("]")) {
						type = Config.Type.valueOf(line.substring(1, line.length() - 1));
						continue;
					}
					String[] split = line.split("=", 2);
					if (split.length == 2) {
						table.put(type, split[0].trim(), split[1].trim());
					}
				}
			}
		} finally {
			scanner.close();
		}
		logger.debug("Text read in: " + table);
		return table;
	}
}
