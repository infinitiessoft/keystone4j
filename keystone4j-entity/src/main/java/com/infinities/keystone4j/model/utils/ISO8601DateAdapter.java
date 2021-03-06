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
package com.infinities.keystone4j.model.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ISO8601DateAdapter extends XmlAdapter<String, Date> {

	private final SimpleDateFormat iso8601Format;
	private final static Logger logger = LoggerFactory.getLogger(ISO8601DateAdapter.class);


	public ISO8601DateAdapter() {
		iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		iso8601Format.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	@Override
	public Date unmarshal(String v) throws Exception {
		if (v == null) {
			return null;
		}
		try {
			DateFormat format = (DateFormat) iso8601Format.clone();
			return format.parse(v);
		} catch (ParseException e) {
			logger.debug("Failed to parse string {}", v);
			return null;
		}

	}

	@Override
	public String marshal(Date v) throws Exception {

		try {
			DateFormat format = (DateFormat) iso8601Format.clone();
			return format.format(v);
		} catch (Exception e) {
			logger.warn("Failed to format date {}", v.toString());
			return null;
		}
	}

}
