package com.github.paulcwarren.scei3;

import com.github.paulcwarren.ginkgo4j.Ginkgo4jSpringRunner;
import com.jayway.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;

import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.BeforeEach;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Context;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Describe;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.It;
import static com.jayway.restassured.RestAssured.given;

@RunWith(Ginkgo4jSpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment=WebEnvironment.RANDOM_PORT)
public class ApplicationTest {

	@Autowired
	private Application.FileStore store;
	
    @LocalServerPort
    int port;
    
    {
    	Describe("Spring Content REST", () -> {
    		BeforeEach(() -> {
    			RestAssured.port = port;
    		});
    		Context("given a claim", () -> {
    			It("should be POSTable with new content with 201 Created", () -> {

					String newContent = "This is some new content";
					
					// POST the new content
					given()
    					.contentType("text/plain")
    					.content(newContent.getBytes())
					.when()
    					.put("/files/a/b/test.txt")
					.then()
    					.statusCode(HttpStatus.SC_OK);
					
					// assert that it now exists
					given()
    					.header("accept", "text/plain")
    					.get("/files/a/b/test.txt")
					.then()
    					.statusCode(HttpStatus.SC_OK)
    					.assertThat()
    					.contentType(Matchers.startsWith("text/plain"))
    					.body(Matchers.equalTo(newContent));
    			});
			});
    	});
    }
    
    @Test
    public void noop() {}
}
