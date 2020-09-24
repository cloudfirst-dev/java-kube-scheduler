package dev.cloudfirst.controlapi;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class JobResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/control-api")
          .then()
             .statusCode(200)
             .body(is("hello"));
    }

}