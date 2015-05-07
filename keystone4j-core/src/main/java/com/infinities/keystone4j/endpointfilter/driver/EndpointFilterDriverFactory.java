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
//package com.infinities.keystone4j.endpointfilter.driver;
//
//import org.glassfish.hk2.api.Factory;
//
//import com.infinities.keystone4j.common.Config;
//import com.infinities.keystone4j.endpointfilter.EndpointFilterDriver;
//
//public class EndpointFilterDriverFactory implements Factory<EndpointFilterDriver> {
//
//	public EndpointFilterDriverFactory() {
//	}
//
//	@Override
//	public void dispose(EndpointFilterDriver arg0) {
//
//	}
//
//	@Override
//	public EndpointFilterDriver provide() {
//		String driver = Config.Instance.getOpt(Config.Type.endpoint_filter, "driver").asText();
//		try {
//			Class<?> c = Class.forName(driver);
//			return (EndpointFilterDriver) c.newInstance();
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
// }
