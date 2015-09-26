package org.yroffin.neo4b.model.rest.neo4j.cypher;

import java.util.List;

import lombok.Data;

@Data
public class Neo4jResult {
	List<String> columns;
	List<Neo4jData> data;
}
