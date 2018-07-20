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
package com.infinities.keystone4j;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.utils.JsonUtils;

public class PatchClient {

	private final Logger logger = LoggerFactory.getLogger(PatchClient.class);
	private final String url;


	public PatchClient(String url) {
		this.url = url;
	}

	public JsonNode connect(Object obj) throws ClientProtocolException, IOException {
		String input = JsonUtils.toJsonWithoutPrettyPrint(obj);
		logger.debug("input: {}", input);
		StringEntity requestEntity = new StringEntity(input, ContentType.create("application/json", Consts.UTF_8));

		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPatch request = new HttpPatch(url);
			request.addHeader("accept", "application/json");
			request.addHeader("X-Auth-Token", Config.getOpt(Config.Type.DEFAULT, "admin_token").asText());
			request.setEntity(requestEntity);
			ResponseHandler<JsonNode> rh = new ResponseHandler<JsonNode>() {

				@Override
				public JsonNode handleResponse(final HttpResponse response) throws IOException {
					StatusLine statusLine = response.getStatusLine();
					HttpEntity entity = response.getEntity();
					if (entity == null) {
						throw new ClientProtocolException("Response contains no content");
					}
					String output = getStringFromInputStream(entity.getContent());
					logger.debug("output: {}", output);
					assertEquals(200, statusLine.getStatusCode());

					JsonNode node = JsonUtils.convertToJsonNode(output);
					return node;
				}

				private String getStringFromInputStream(InputStream is) {

					BufferedReader br = null;
					StringBuilder sb = new StringBuilder();

					String line;
					try {

						br = new BufferedReader(new InputStreamReader(is));
						while ((line = br.readLine()) != null) {
							sb.append(line);
						}

					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						if (br != null) {
							try {
								br.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}

					return sb.toString();

				}
			};
			JsonNode node = httpclient.execute(request, rh);

			return node;
		} finally {
			httpclient.close();
		}
	}

}
