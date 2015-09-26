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

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ws.rs.client.WebTarget;

import org.springframework.stereotype.Component;
import org.yroffin.neo4b.components.TechnicalException;
import org.yroffin.neo4b.model.rest.board.BoardRest;

/**
 * resource mapper
 */
@Component
public class BoardResource extends DefaultResource {

	WebTarget client;

	@PostConstruct
	void init() {
		client = graphDbService.factory("http://192.168.1.12:7474", "neo4j", "123456");
	}

	/**
	 * get all boards
	 * 
	 * @param board
	 * @return
	 * @throws IOException
	 */
	public String getBoards(String body, Class<BoardRest> klass) throws IOException {
		if (body != null && body.length() > 0) {
			mapper.readValue(body, klass);
		}
		return mapper.writeValueAsString(new BoardRest());
	}

	/**
	 * get all boards
	 * 
	 * @param board
	 * @return
	 * @throws IOException
	 */
	public String createBoard(String body, Class<BoardRest> klass) throws IOException {
		if (body != null && body.length() > 0) {
			BoardRest board = mapper.readValue(body, klass);

			List<BoardRest> response = graphDbService.findAll(client, BoardRest.class, 100);

			return mapper.writeValueAsString(response);
		} else {
			throw new TechnicalException("No data");
		}
	}
}
