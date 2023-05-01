package RestAssure;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class ApiKeyTest {

    @Test
    public void Setup() {

        given()

                .header("x-api-key",
                        "GwMco9Tpstd5vbzBzlzW9I7hr6E1D7w2zEIrhOra")
                .when()
                .get("https://l9njuzrhf3.execute-api.eu-west-1.amazonaws.com/prod/user")
                .then()
                .log().body()
                .statusCode(200);

    }
}
