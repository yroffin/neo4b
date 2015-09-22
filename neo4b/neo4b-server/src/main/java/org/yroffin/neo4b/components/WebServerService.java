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
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yroffin.neo4b.services.ResourceMapper;

@Component
public class WebServerService extends DefaultService {

	@Autowired
	ResourceMapper resourceMapper;

	protected static final Logger logger = LoggerFactory.getLogger(WebServerService.class);
	private static final String SERVER_PROPERTIES = "server.properties";
	private Properties properties;

	/**
	 * post construct
	 * 
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	@PostConstruct
	void init() throws IOException, URISyntaxException {
		logger.info("Component {} is loading", this.getClass().getName());
		URL url = this.getClass().getResource("/" + SERVER_PROPERTIES);
		if (url != null) {
			File file = new File(url.toURI());

			if (file.exists()) {
				properties = getProperties(file);
				String iface = properties.getProperty("listen");
				int port = Integer.parseInt(properties.getProperty("port"));
				spark.Spark.ipAddress(iface);
				spark.Spark.port(port);
				// register hook
				registerShutdownHook(this);
				// Map resources
				resourceMapper.map();
			} else {
				logger.error("Unable to find any {}", SERVER_PROPERTIES);
			}
		} else {
			logger.error("Unable to find any {} in classpath", SERVER_PROPERTIES);
		}
		logger.info("Component {} is loaded", this.getClass().getName());
	}

	@Override
	protected void shutdown() {
		spark.Spark.stop();
	}
}
