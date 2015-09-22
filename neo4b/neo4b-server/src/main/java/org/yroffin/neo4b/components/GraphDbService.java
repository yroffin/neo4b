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

import javax.annotation.PostConstruct;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GraphDbService extends DefaultService {

	protected static final Logger logger = LoggerFactory.getLogger(GraphDbService.class);
	private static final String NEO4J_PROPERTIES = "neo4j.properties";
	GraphDatabaseService graphDb;

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
				String dbPath = getDbPath(file);
				logger.info("Storing into {}", dbPath);
				graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(dbPath)
						.loadPropertiesFromFile(file.getAbsolutePath()).newGraphDatabase();
				// register hook
				registerShutdownHook(this);
			} else {
				logger.error("Unable to find any {}", NEO4J_PROPERTIES);
			}
		} else {
			logger.error("Unable to find any {} in classpath", NEO4J_PROPERTIES);
		}
		logger.info("Component {} is loaded", this.getClass().getName());
	}

	/**
	 * read properties file to find dbPath
	 * 
	 * @param properties
	 * @return
	 * @throws IOException
	 */
	private String getDbPath(File properties) throws IOException {
		return getProperties(properties).getProperty("db.path");
	}

	@Override
	protected void shutdown() {
		graphDb.shutdown();
	}
}
