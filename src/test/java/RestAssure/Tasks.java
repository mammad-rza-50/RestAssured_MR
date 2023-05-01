package RestAssure;

import Model.Task2_Location;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Tasks {


    /**
     * Task 2
     * create a request to https://httpstat.us/203
     * expect status 203
     * expect content type TEXT
     */
    @Test
    public void task1() {
        given()

                .when()
                .get("https://httpstat.us/203")

                .then()
                .log().all()
                .statusCode(203)
                .contentType(ContentType.TEXT)
        ;

    }

    @Test
    public void task2() {

        /** Task 1

         create a request to https://jsonplaceholder.typicode.com/todos/2
         expect status 200
         Converting Into POJO*/
        Task2_Location task2Location =
                given()

                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos/2")

                        .then()
                        .log().body()
                        .statusCode(200)
                        .extract().body().as(Task2_Location.class);

        System.out.println("task2Location = " + task2Location);                              // Umumi verir
        System.out.println("task2Location.getUserId() = " + task2Location.getUserId());      // tek usrId
        System.out.println("task2Location.getId() = " + task2Location.getId());              // id
        System.out.println("task2Location.getTitle() = " + task2Location.getTitle());        // title
        System.out.println("task2Location.isCompleted() = " + task2Location.isCompleted());  //  boolean
    }

    @Test
    public void task3() {

        /**
         Task 3
         create a request to https://jsonplaceholder.typicode.com/todos/2
         expect status 200
         expect content type JSON
         expect title in response body to be "quis ut nam facilis et officia qui"
         */

        given()
                .when()
                .get(" https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
               .body("title",equalTo("quis ut nam facilis et officia qui"))
        ;

    }

    @Test
    public void task4(){

        /**
         Task 4
         create a request to https://jsonplaceholder.typicode.com/todos/2
         expect status 200
         expect content type JSON
         expect response completed status to be false
         extract completed field and testNG assertion
         */

        // first way

        given()
                .when()
                .get(" https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
              .body("completed" , equalTo(false))
                ;

        // second way

        Boolean completed=
        given()
                .when()
                .get(" https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().path("completed")
        ;
        Assert.assertFalse(completed);
    }
}