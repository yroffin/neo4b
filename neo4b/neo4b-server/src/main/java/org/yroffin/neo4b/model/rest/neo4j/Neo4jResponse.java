package org.yroffin.neo4b.model.rest.neo4j;

import java.util.List;

import org.yroffin.neo4b.model.rest.neo4j.cypher.Neo4jResult;

import lombok.Data;

@Data
public class Neo4jResponse {
	List<Neo4jResult> results;
	List<String> errors;
}
