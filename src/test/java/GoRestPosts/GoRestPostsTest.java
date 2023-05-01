package GoRestPosts;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class GoRestPostsTest {

    Faker faker = new Faker();
    RequestSpecification reqSpec;
    int postID;


    @BeforeClass
    public void SetupPosts() {

        baseURI = "https://gorest.co.in/public/v2/posts";

        reqSpec = new RequestSpecBuilder()
                .addHeader("Authorization",
                        "Bearer 953855dc118c8e3317cbfd8896d71a85ddde430a9c26f6d46ee37d09e712bbe7")
                .setContentType(ContentType.JSON).build();
    }

    @Test
    public void createPostMap() {

        Map<String, Object> postData = new HashMap<>();
        postData.put("user_id", 1313061);
        postData.put("title", faker.name().title());
        postData.put("body", faker.book().title());

        postID =
                given()
                        .spec(reqSpec)
                        .body(postData)
                        .log().body()

                        .when()
                        .post("")

                        .then()
                        // .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id");

    }

    @Test(dependsOnMethods = "createPostMap")
    public void getPostId() {

        given()
                .spec(reqSpec)

                .log().body()

                .when()
                .get("" + postID)

                .then()
                // .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .log().body()
                .body("id", equalTo(postID));


    }

    @Test(dependsOnMethods = "getPostId")
    public void updatePost() {

        Map<String, Object> updatepostData = new HashMap<>();
        updatepostData.put("title", "Title Memmed Rzayev");
        updatepostData.put("body", "Body MR");

        given()
                .spec(reqSpec)
                .body(updatepostData)
                .log().body()

                .when()
                .put("" + postID)

                .then()
                // .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .log().body()
                .body("id", equalTo(postID))
                .body("title", equalTo("Title Memmed Rzayev"))
                .body("body", equalTo("Body MR"));
    }

    @Test(dependsOnMethods = "updatePost")
    public void deletePost() {

        given()
                .spec(reqSpec)

                .log().body()

                .when()
                .delete("" + postID)

                .then()
                // .log().body()
                .statusCode(204)
        ;
    }

    @Test(dependsOnMethods = "deletePost")
    public void deletePostNegative() {

        given()
                .spec(reqSpec)

                .log().body()

                .when()
                .delete("" + postID)

                .then()
                 .log().body()
                .statusCode(404)
                .body("message", equalTo("Resource not found"))
        ;
    }
}
