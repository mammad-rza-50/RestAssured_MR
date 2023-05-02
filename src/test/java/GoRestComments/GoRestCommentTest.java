package GoRestComments;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class GoRestCommentTest {

    Faker faker = new Faker();
    int commentID;
    RequestSpecification reqSpec;

    @BeforeClass
    public void SetupComment() {

        baseURI = "https://gorest.co.in/public/v2/comments";

        reqSpec = new RequestSpecBuilder()
                .addHeader("Authorization",
                        "Bearer 953855dc118c8e3317cbfd8896d71a85ddde430a9c26f6d46ee37d09e712bbe7")
                .setContentType(ContentType.JSON).build();

    }

    @Test
    public void createComment() {

        Map<String, Object> commentData = new HashMap<>();
        commentData.put("post_id", 17228);
        commentData.put("name", faker.name().fullName());
        commentData.put("email", faker.internet().emailAddress());
        commentData.put("body", faker.book().title());

        commentID =

                given()
                        .spec(reqSpec)
                        .body(commentData)

                        .when()
                        .post("")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id");
    }

    @Test(dependsOnMethods = "createComment")
    public void getCommentById() {

        given()
                .spec(reqSpec)

                .when()
                .get("" + commentID)
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(commentID));

    }

    @Test(dependsOnMethods = "getCommentById")
    public void updateComment() {

        Map<String, Object> updateComment = new HashMap<>();
        updateComment.put("name", faker.name().fullName());
        updateComment.put("email", faker.internet().emailAddress());
        updateComment.put("body", faker.book().title());
        given()
                .spec(reqSpec)
                .body(updateComment)

                .when()
                .put("" + commentID)
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(commentID))
        ;

    }

    @Test(dependsOnMethods = "updateComment")
    public void deleteComment() {

        given()
                .spec(reqSpec)

                .when()
                .delete("" + commentID)
                .then()
                .log().body()
                .statusCode(204)
        ;

    }

    @Test(dependsOnMethods = "deleteComment")
    public void deleteCommentNegative() {

        given()
                .spec(reqSpec)

                .when()
                .delete("" + commentID)
                .then()
                .log().body()
                .statusCode(404)
                .body("message", equalTo("Resource not found"))
        ;

    }
}
