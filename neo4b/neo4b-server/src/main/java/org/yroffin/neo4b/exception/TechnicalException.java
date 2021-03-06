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

package org.yroffin.neo4b.exception;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * technical exception
 */
public class TechnicalException extends RuntimeException {

	public TechnicalException(String message) {
		super(message);
	}

	public TechnicalException(JsonProcessingException e) {
		super(e);
	}

	public TechnicalException(IOException e) {
		super(e);
	}

	public TechnicalException(Exception e) {
		super(e);
	}

	/**
	 * default serial
	 */
	private static final long serialVersionUID = 1L;

}
