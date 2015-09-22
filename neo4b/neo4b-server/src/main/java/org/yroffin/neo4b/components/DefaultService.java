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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * default service
 */
public class DefaultService {
	/**
	 * logger
	 */
	protected static final Logger logger = LoggerFactory.getLogger(DefaultService.class);

	/**
	 * read properties
	 * 
	 * @param properties
	 * @return
	 * @throws IOException
	 */
	protected static Properties getProperties(File properties) throws IOException {
		Properties prop = new Properties();
		InputStream input = null;
		input = new FileInputStream(properties.getAbsolutePath());
		prop.load(input);
		input.close();
		return prop;
	}

	/**
	 * hook to stop service
	 * 
	 * @param service
	 */
	protected static void registerShutdownHook(final DefaultService service) {
		// Registers a shutdown hook for the service instance so that it
		// shuts down nicely when the VM exits (even if you "Ctrl-C" the
		// running application).
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				logger.info("Shutdown ...");
				service.shutdown();
				logger.info("Shutdown ok");
			}
		});
	}

	/**
	 * shutdown handler
	 */
	protected void shutdown() {
		throw new TechnicalException("Not implemented yet");
	}
}
