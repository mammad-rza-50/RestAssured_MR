package GoRestTodos;

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


public class GoRestTodoTest {

    int todoID;
    Faker faker = new Faker();
    RequestSpecification reqSpecf;

    @BeforeClass
    public void SetupTodo() {

        baseURI = "https://gorest.co.in/public/v2/todos";

        reqSpecf = new RequestSpecBuilder()
                .addHeader("Authorization",
                        "Bearer 953855dc118c8e3317cbfd8896d71a85ddde430a9c26f6d46ee37d09e712bbe7")
                .setContentType(ContentType.JSON).build();

    }

    @Test
    public void createTodo() {

        Map<String, Object> todoData = new HashMap<>();
        todoData.put("user_id", 1313060);
        todoData.put("title", faker.book().title());
        todoData.put("due_on", " 2023-05-07T00:00:00.000+05:30");
        todoData.put("status", "completed");

        todoID =
                given()
                        .spec(reqSpecf)
                        .body(todoData)

                        .when()
                        .post("")
                        .then()
                        .statusCode(201)
                        .log().body()
                        .contentType(ContentType.JSON)
                        .extract().path("id");

    }

    @Test(dependsOnMethods = "createTodo")
    public void getTodoById() {

        given()
                .spec(reqSpecf)
                .when()
                .get("" + todoID)
                .then()
                .contentType(ContentType.JSON)
                .log().body()
                .statusCode(200)
                .body("id", equalTo(todoID));

    }

    @Test(dependsOnMethods = "getTodoById")
    public void updateTodo() {

        Map<String, Object> updateTodo = new HashMap<>();
        updateTodo.put("title", faker.book().title());

        given().spec(reqSpecf)
                .body(updateTodo)
                .when()
                .put("" + todoID)
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(todoID));

    }

    @Test(dependsOnMethods = "updateTodo")
    public void deleteTodo() {

        given().spec(reqSpecf)
                .when()
                .delete("" + todoID)
                .then()
                .log().body()
                .statusCode(204);
    }

    @Test(dependsOnMethods = "deleteTodo")
    public void deleteTodoNegative() {

        given().spec(reqSpecf)
                .when()
                .delete("" + todoID)
                .then()
                .log().body()
                .statusCode(404)
                .body("message", equalTo("Resource not found"));
    }
}
