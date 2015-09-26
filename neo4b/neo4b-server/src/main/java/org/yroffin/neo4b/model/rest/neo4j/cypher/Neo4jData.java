package org.yroffin.neo4b.model.rest.neo4j.cypher;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Neo4jData {
	@JsonProperty("row")
	Collection<Neo4jRaw> row;
}
