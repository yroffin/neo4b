/**
 * 
 */
package org.yroffin.neo4b.components;

import java.util.Date;
import java.util.List;

import javax.ws.rs.client.WebTarget;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.yroffin.neo4b.model.rest.neo4j.Neo4jRequest;
import org.yroffin.neo4b.model.rest.neo4j.Neo4jResponse;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.Data;

/**
 * @author yannick
 *
 */
public class GraphDbServiceTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRequest() throws JsonProcessingException {
		GraphDbService service = new GraphDbService();
		Neo4jRequest request = new Neo4jRequest();
		request.addStatement("CREATE (n) RETURN id(n)");
		WebTarget client = service.factory("http://192.168.1.12:7474", "neo4j", "123456");
		Neo4jResponse response = service.request(client, request);
		System.err.println(response);
	}

	@Test
	public void testCypherRequest() {
		GraphDbService service = new GraphDbService();
		WebTarget client = service.factory("http://192.168.1.12:7474", "neo4j", "123456");
		Neo4jResponse response = service.cypher(client, "CREATE (n) RETURN id(n)");
		System.err.println(response);
	}

	@Data
	public static class BoardTest {
		String id = "1";
		String value = "test";
		int i = 9;
		double x = 9.9;
		Date timestamp;
		String data;
		int sequence;
	}

	@Test
	public void testBoardRequest() {
		GraphDbService service = new GraphDbService();
		WebTarget client = service.factory("http://192.168.1.12:7474", "neo4j", "123456");
		List<BoardTest> response = service.cypherFind(client, "MATCH (BoardTest) RETURN BoardTest LIMIT 2",
				BoardTest.class);
		System.err.println(response);
	}
}
