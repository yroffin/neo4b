package org.yroffin.neo4b.model.rest.neo4j;

import java.util.ArrayList;
import java.util.List;

import org.yroffin.neo4b.model.rest.neo4j.cypher.Neo4jStatement;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Neo4jRequest {
	List<Neo4jStatement> statements = new ArrayList<Neo4jStatement>();

	/**
	 * add a new statement
	 * 
	 * @param statement
	 * @return
	 */
	public Neo4jStatement addStatement(String statement) {
		Neo4jStatement result = new Neo4jStatement();
		result.setStatement(statement);
		statements.add(result);
		return result;
	}
}
