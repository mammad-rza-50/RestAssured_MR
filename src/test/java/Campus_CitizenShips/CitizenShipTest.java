package Campus_CitizenShips;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;

public class CitizenShipTest {

    Faker faker = new Faker();
    RequestSpecification reqSpecf;

    String citizehShipID;
    String citizehShipName;
    String citizehShipShortName;


    @BeforeClass
    public void Login() {

        baseURI = "https://test.mersys.io";

        Map<String, Object> userDatas = new HashMap<>();
        userDatas.put("username", "turkeyts");
        userDatas.put("password", "TechnoStudy123");
        userDatas.put("rememberMe", "true");

        Cookies cookies =
                given()
                        .contentType(ContentType.JSON)
                        .body(userDatas)

                        .when()
                        .post("/auth/login")

                        .then()
                        .statusCode(200)
                        //.log().all()
                        .extract().response().getDetailedCookies();
        reqSpecf = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addCookies(cookies).build();

    }

    @Test
    public void CitizeShipCreate() {

        Map<String,Object> citShipData=new HashMap<>();
        citizehShipName=faker.name().fullName();
        citizehShipShortName=faker.name().username();
        citShipData.put("name",citizehShipName);
        citShipData.put("shortName",citizehShipShortName);

        citizehShipID =

                given().spec(reqSpecf)
                        .body(citShipData)
                        .log().body()

                        .when()
                        .post("/school-service/api/citizenships")
                        .then()
                        .log().all()
                        .statusCode(201)
                        .extract().path("id");

        System.out.println("citizehShipID = " + citizehShipID);
    }

    @Test(dependsOnMethods = "CitizeShipCreate")
    public void CitizeShipCreateNegative() {

        Map<String,Object> citShipData=new HashMap<>();
        citShipData.put("name",citizehShipName);
        citShipData.put("shortName",citizehShipShortName);

        given().spec(reqSpecf)
                .body(citShipData)
                .log().body()

                .when()
                .post("/school-service/api/citizenships")
                .then()
                .log().all()
                .statusCode(400)
                .body("message",containsString("already exists"));

    }

    @Test(dependsOnMethods = "CitizeShipCreateNegative")
    public void CitizeShipUpdate() {

        Map<String,Object> citShipData=new HashMap<>();
        citizehShipName="MR_"+faker.name().fullName();
        citizehShipShortName="mr_"+faker.name().username();
        citShipData.put("name",citizehShipName);
        citShipData.put("shortName",citizehShipShortName);
        citShipData.put("id",citizehShipID);

        given().spec(reqSpecf)
                .body(citShipData)
                .log().body()

                .when()
                .put("/school-service/api/citizenships")
                .then()
                .log().all()
                .statusCode(200)
               ;

    }

    @Test(dependsOnMethods = "CitizeShipUpdate")
    public void CitizeShipDelete() {

        given().spec(reqSpecf)
                .body(citizehShipID)
                .log().body()

                .when()
                .delete("/school-service/api/citizenships/"+citizehShipID)
                .then()
                .log().all()
                .statusCode(200)
        ;

    }


    @Test(dependsOnMethods = "CitizeShipDelete")
    public void CitizeShipDeleteNegative() {

        given().spec(reqSpecf)
                .body(citizehShipID)
                .log().body()

                .when()
                .delete("/school-service/api/citizenships/"+citizehShipID)
                .then()
                .log().all()
                .statusCode(400)
                .body("message", equalTo("Citizenship not found"));
        ;

    }
}
