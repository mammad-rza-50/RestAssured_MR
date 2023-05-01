package Campus;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class CountryTest {

    RequestSpecification recSpec;
    String countryID;
    String countryName;
    Faker faker = new Faker();

    @BeforeClass
    public void Login() {

        baseURI = "https://test.mersys.io";

        Map<String, String> userCredential = new HashMap<>();
        userCredential.put("username", "turkeyts");
        userCredential.put("password", "TechnoStudy123");
        userCredential.put("rememberMe", "true");

        Cookies cookies =
                given()
                        .contentType(ContentType.JSON)
                        .body(userCredential)

                        .when().post("/auth/login")
                        .then()
                        //.log().all()
                        .statusCode(200)
                        .extract().response().getDetailedCookies();

        recSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addCookies(cookies).build();

    }

    @Test
    public void createCountry() {

        Map<String, String> country = new HashMap<>();
        countryName = faker.address().country() + faker.number().digits(7);
        country.put("name", countryName);
        country.put("code", faker.address().countryCode() + faker.number().digits(7));

        countryID =
                given()
                        .spec(recSpec)
                        .body(country)
                        //.log().body()

                        .when()
                        .post("/school-service/api/countries")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");

       // System.out.println("countyID = " + countryID);

    }

    @Test(dependsOnMethods = "createCountry")
    public void createCountryNegative() {

        Map<String, String> country = new HashMap<>();
        country.put("name", countryName);
        country.put("code", faker.address().countryCode() + faker.number().digits(7));

        given()
                .spec(recSpec)
                .body(country)
               // .log().body()

                .when()
                .post("/school-service/api/countries")

                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already"));

    }

    @Test(dependsOnMethods = "createCountryNegative")
    public void updateCountry() {

        Map<String, String> country = new HashMap<>();
        country.put("id", countryID);

        countryName = "MR country" + faker.number().digits(7);
        country.put("name", countryName);
        country.put("code", faker.address().countryCode() + faker.number().digits(7));

        given()
                .spec(recSpec)
                .body(country)
                // .log().body()

                .when()
                .put("/school-service/api/countries")

                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(countryName));

    }

    @Test(dependsOnMethods = "updateCountry")
    public void deleteCountry() {

        given()
                .spec(recSpec)
                .pathParam("countryID", countryID)
               // .log().uri()

                .when()
                .delete("/school-service/api/countries/{countryID}")

                .then()
                .log().body()
                .statusCode(200);

    }

    @Test(dependsOnMethods = "deleteCountry")
    public void deleteCountryNegative() {

        given().spec(recSpec)
                .pathParam("countryID", countryID)
                //.log().uri()

                .when()
                .delete("/school-service/api/countries/{countryID}")

                .then()
                .log().body()
                .statusCode(404)
                .body("message", equalTo("Country not found"));

    }
}
