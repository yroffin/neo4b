/**
 *  Copyright 2015 Yannick Roffin
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.yroffin.neo4b.components;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.yroffin.neo4b.model.rest.neo4j.Neo4jRequest;
import org.yroffin.neo4b.model.rest.neo4j.Neo4jResponse;
import org.yroffin.neo4b.model.rest.neo4j.cypher.Neo4jData;
import org.yroffin.neo4b.model.rest.neo4j.cypher.Neo4jResult;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class GraphDbService extends DefaultService {

	protected static final Logger logger = LoggerFactory.getLogger(GraphDbService.class);
	private static final String NEO4J_PROPERTIES = "neo4j.properties";

	private Properties properties;
	static private String SERVER_ROOT_URI;

	/**
	 * post construct hook
	 * 
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	@PostConstruct
	void init() throws URISyntaxException, IOException {
		logger.info("Component {} is loading", this.getClass().getName());
		URL url = this.getClass().getResource("/" + NEO4J_PROPERTIES);
		if (url != null) {
			File file = new File(url.toURI());
			if (file.exists()) {
				logger.info("Loading from {}", NEO4J_PROPERTIES);

				properties = getProperties(file);
				SERVER_ROOT_URI = properties.getProperty("org.neo4j.webserver.address");

				logger.info("Url {}", SERVER_ROOT_URI);
			} else {
				logger.error("Unable to find any {}", NEO4J_PROPERTIES);
			}
		} else {
			logger.error("Unable to find any {} in classpath", NEO4J_PROPERTIES);
		}
		logger.info("Component {} is loaded", this.getClass().getName());
	}

	/**
	 * jackson mapper
	 */
	ObjectMapper mapper = new ObjectMapper();

	public enum METHOD {
		POST, GET, PUT, DELETE
	}

	/**
	 * client factory
	 * 
	 * @param serverRootUri
	 * @param user
	 * @param password
	 * @return
	 */
	public WebTarget factory(String serverRootUri, String user, String password) {
		/**
		 * basic auth
		 */
		Client client = ClientBuilder.newClient();
		HttpAuthenticationFeature basic = HttpAuthenticationFeature.basic(user, password);
		client.register(basic);

		/**
		 * retrieve target
		 */
		WebTarget webTarget = client.target(UriBuilder.fromUri(serverRootUri).build());
		return webTarget;
	}

	/**
	 * generic jax-rs call
	 * 
	 * @param webTarget
	 * @param entity
	 * @param uri
	 * @param method
	 * @param body
	 * @return
	 * @throws IOException
	 */
	public <T, E> T execute(WebTarget webTarget, Class<T> entity, String uri, METHOD method, E body)
			throws IOException {
		WebTarget resourceWebTarget = webTarget.path(uri);
		Invocation.Builder invocationBuilder = resourceWebTarget.request();
		invocationBuilder.accept(MediaType.APPLICATION_JSON);
		invocationBuilder.acceptEncoding("UTF-8");

		/**
		 * add body if exist
		 */
		Entity<String> payload = null;
		if (body != null) {
			payload = Entity.entity(mapper.writeValueAsString(body), MediaType.APPLICATION_JSON);
		}
		Response response = null;
		switch (method) {
		case GET:
			response = invocationBuilder.get();
			break;
		case POST:
			response = invocationBuilder.post(payload);
			break;
		default:
			break;
		}

		/**
		 * build result
		 */
		T result = null;
		if (response.getStatus() == 200) {
			result = (T) mapper.readValue(response.readEntity(String.class), entity);
		} else {
			response.close();
			throw new TechnicalException(response.toString());
		}
		response.close();

		/**
		 * some logs
		 */
		logger.info("Method:{} Uri:{} Status:{}\n>> {}", method.name(), webTarget.getUri().toASCIIString(), uri,
				result);
		return result;
	}

	/**
	 * POST http://localhost:7474/db/data/transaction/commit Accept:
	 * application/json; charset=UTF-8 Content-Type: application/json
	 * 
	 * @param request
	 * @return
	 */
	public Neo4jResponse request(WebTarget webTarget, Neo4jRequest request) {
		Neo4jResponse response = null;
		try {
			response = execute(webTarget, Neo4jResponse.class, "/db/data/transaction/commit", METHOD.POST, request);
		} catch (IOException e) {
			logger.error("Error {}", e);
			throw new TechnicalException(e);
		}
		return response;
	}

	/**
	 * cypher request
	 * 
	 * @param webTarget
	 * @param cypher
	 * @return
	 */
	public Neo4jResponse cypher(WebTarget webTarget, String cypher) {
		Neo4jRequest request = new Neo4jRequest();
		request.addStatement(cypher);
		return request(webTarget, request);
	}

	/**
	 * find node by label
	 * 
	 * @param webTarget
	 * @param label
	 * @param klass
	 * @return
	 */
	public <T> List<T> cypherFind(WebTarget webTarget, String cypher, Class<T> klass) {
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		List<T> results = new ArrayList<T>();
		Neo4jResponse cyphers = cypher(webTarget, cypher);
		for (Neo4jResult item : cyphers.getResults()) {
			for (Neo4jData data : item.getData()) {
				for (Object row : data.getRow()) {
					try {
						results.add((T) mapper.readValue(row.toString(), klass));
						logger.info("Entity:{}", row.toString());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return results;
	}

	/**
	 * finder all
	 * 
	 * @param client
	 * @param klass
	 * @return
	 */
	public <T> List<T> findAll(WebTarget client, Class<T> klass, int limit) {
		return cypherFind(client,
				"MATCH (" + klass.getSimpleName() + ") RETURN " + klass.getSimpleName() + " LIMIT " + limit, klass);
	}
}
