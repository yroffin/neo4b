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
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yroffin.neo4b.model.rest.board.BoardRest;

/**
 * resource mapper
 */
@Component
public class BoardMapper extends ResourceMapper implements IResourceMapper {

	@Autowired
	BoardResource boardResource;

	public void map() throws IOException, URISyntaxException {
		// GET
		spark.Spark.get("/api/boardManagement/v1/boards", (request, response) -> {
			response.type("application/json");
			response.body(boardResource.getBoards(request.body(), BoardRest.class));
			return response.body();
		});
		// POST
		spark.Spark.post("/api/boardManagement/v1/boards", (request, response) -> {
			response.type("application/json");
			response.body(boardResource.createBoard(request.body(), BoardRest.class));
			return response.body();
		});
	}
}
