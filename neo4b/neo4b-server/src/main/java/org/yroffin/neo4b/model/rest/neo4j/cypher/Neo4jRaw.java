package org.yroffin.neo4b.model.rest.neo4j.cypher;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonDeserialize(using = RowDeserializer.class)
public class Neo4jRaw {
}
