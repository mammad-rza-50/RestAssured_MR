package GoRestUsers;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.*;


import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class GoRestUsersTests {

    Faker random = new Faker();
    int userID;
    RequestSpecification reqSpec;

    @BeforeClass
    public void setupUsers() {

        baseURI = "https://gorest.co.in/public/v2/users";
        //baseURI ="https://test.gorest.co.in/public/v2/users/";

        reqSpec = new RequestSpecBuilder()
                .addHeader("Authorization",
                        "Bearer 953855dc118c8e3317cbfd8896d71a85ddde430a9c26f6d46ee37d09e712bbe7")
                .setContentType(ContentType.JSON)
                .build();

    }

    @Test(enabled = false)
    public void createUserJSON() {

        // "Authorization: Bearer 953855dc118c8e3317cbfd8896d71a85ddde430a9c26f6d46ee37d09e712bbe7
        // POST https://gorest.co.in/public/v2/users
        // {"name":"MR1214", "gender":"male", "email":"MRzayev@gmail.com", "status":"active"}

        String rndFullName = random.name().fullName();
        String rndEmail = random.internet().emailAddress();

        userID =
                given()
                        .spec(reqSpec)
                        .body("{\"name\":\"" + rndFullName + "\", \"gender\":\"male\", \"email\":\"" + rndEmail + "\", \"status\":\"active\"}")
//                        .log().uri()
//                        .log().body()
                        .when().post("")
                        .then()
                        .log().all()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id");

    }

    @Test
    public void createUserMap() {

        System.out.println("baseURI = " + baseURI);
        String rndFullName = random.name().fullName();
        String rndEmail = random.internet().emailAddress();

        Map<String, String> newUser = new HashMap<>();
        newUser.put("name", rndFullName);
        newUser.put("gender", "male");
        newUser.put("email", rndEmail);
        newUser.put("status", "active");

        userID =
                given()
                        .spec(reqSpec)
                        .body(newUser)
                        .log().uri()
                        //.log().body()
                        .when().post("")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id");

    }

    @Test(enabled = false)
    public void createUserClass() {

        String rndFullName = random.name().fullName();
        String rndEmail = random.internet().emailAddress();

        User newUser = new User();
        newUser.setName(rndFullName);
        newUser.setGender("male");
        newUser.setEmail(rndEmail);
        newUser.setStatus("active");

        userID =
                given()
                        .spec(reqSpec)
                        .body(newUser)
                        // .log().uri()
                        // .log().body()
                        .when().post("")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id");

    }

    @Test(dependsOnMethods = "createUserMap")
    public void getUserById() {

        given()
                .spec(reqSpec)
                .when().get("" + userID)

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(userID));
    }

    @Test(dependsOnMethods = "getUserById")
    public void updateUser() {

        Map<String, String> updateUser = new HashMap<>();
        updateUser.put("name", "Memmed Rzayev");

        given()
                .spec(reqSpec)
                .contentType(ContentType.JSON)
                .body(updateUser)
                .log().uri()

                .when()
                .put("" + userID)

                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(userID))
                .body("name", equalTo("Memmed Rzayev"));

    }

    @Test(dependsOnMethods = "updateUser")
    public void deleteUser() {

        given()
                .spec(reqSpec)
                .when()
                .delete("" + userID)

                .then()
                .log().all()
                .statusCode(204);

    }

    @Test(dependsOnMethods = "deleteUser")
    public void deleteNegativeUser() {

        given()
                .spec(reqSpec)
                .when()
                .delete("" + userID)

                .then()
                .log().all()
                .statusCode(404)
                .body("message",equalTo("Resource not found"));

    }

}
