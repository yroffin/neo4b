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

package org.yroffin.neo4b.services;

import java.util.List;

import javax.ws.rs.client.WebTarget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.yroffin.neo4b.components.GraphDbService;
import org.yroffin.neo4b.exception.TechnicalException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ma.glasnost.orika.MapperFactory;

public class DefaultResource {
	protected static final Logger logger = LoggerFactory.getLogger(DefaultResource.class);

	@Autowired
	protected GraphDbService graphDbService;

	protected MapperFactory mapperFactory;
	protected ObjectMapper mapperNeo4j = new ObjectMapper();
	protected ObjectMapper mapperSpark = new ObjectMapper();
	protected WebTarget client;

	/**
	 * init
	 */
	protected void init() {
		mapperNeo4j.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);
	}

	/**
	 * find all
	 * 
	 * @param klass
	 * @param limit
	 * @return
	 */
	protected <T> List<T> findAll(Class<T> klass, int limit) {
		return graphDbService.findAll(client, klass, limit);
	}

	/**
	 * find by label
	 * 
	 * @param klass
	 * @param label
	 * @param limit
	 * @return
	 */
	protected <T> List<T> findAll(Class<T> klass, String label, int limit) {
		return graphDbService.findAll(client, klass, label, limit);
	}

	/**
	 * create node
	 * 
	 * @param body
	 * @param klass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T> T create(T value, String label) {
		try {
			return graphDbService.create(client, mapperNeo4j.writeValueAsString(value), (Class<T>) value.getClass(),
					label);
		} catch (JsonProcessingException e) {
			logger.error("Error {}", e);
			throw new TechnicalException(e);
		}
	}
}
