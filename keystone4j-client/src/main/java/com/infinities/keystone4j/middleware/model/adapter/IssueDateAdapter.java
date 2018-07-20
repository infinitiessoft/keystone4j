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
package com.infinities.keystone4j.middleware.model.adapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class IssueDateAdapter extends XmlAdapter<String, Calendar> {

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");


	@Override
	public String marshal(Calendar v) throws Exception {
		return dateFormat.format(v.getTime());
	}

	@Override
	public Calendar unmarshal(String v) throws Exception {
		Date date = dateFormat.parse(v);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

}