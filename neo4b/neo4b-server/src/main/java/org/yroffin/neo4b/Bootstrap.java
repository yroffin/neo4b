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

package org.yroffin.neo4b;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * main application boot strap
 */
public class Bootstrap {
	public static final String CONFIG_PATH = "classpath*:application-config.xml";

	/**
	 * main entry
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		final ApplicationContext context = new ClassPathXmlApplicationContext(CONFIG_PATH);
		((AbstractApplicationContext) context).close();
	}
}
