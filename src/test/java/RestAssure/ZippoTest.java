package RestAssure;

import Model.Location;
import Model.Place;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ZippoTest {

    @Test
    public void test() {

        given()
                // hazirlik isleri; (token, send body, parametler)

                .when()
                // endpoint(yani URl, metod)

                .then();
        // Test - assertion
    }

    @Test
    public void statusCodeTest() {

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()   // donen body json data-si  ,  log.all() hamisini gosterir
                .statusCode(200);  // donus codu 200 mu demek

    }

    @Test
    public void contenetTypTest() {

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()   // donen body json data-si  ,  log.all() hamisini gosterir

                .statusCode(200)  // donus codu 200 mu demek

                .contentType(ContentType.JSON);  // donus JSON mu

    }

    @Test
    public void checkCountryInResponseTest() {

//        PM                            RestAssured
//        body.country                  body("country")
//        body.'post code'              body("post code")
//        body.places[0].'place name'   body("places[0].'place name'")
//        body.places.'place name'      body("places.'place name'")
//        bütün place nameleri bir arraylist olarak verir


        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()   // donen body json data-si  ,  log.all() hamisini gosterir

                .statusCode(200)  // donus codu 200 mu demek

                .body("country", equalTo("United States"));
        // body-nin country deyiskeni buna beraber mi
        // pm.response.json.id--> body.id POSTMAN da bele olur

    }

    @Test
    public void checkStateInResponseBody() {

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()   // donen body json data-si  ,  log.all() hamisini gosterir

                .statusCode(200)  // donus codu 200 mu demek

                .body("places[0].state", equalTo("California"));  // donus JSON mu

    }

    @Test
    public void checkHasItem() {

        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")
                .then()
                .log().body()   // donen body json data-si  ,  log.all() hamisini gosterir

                .statusCode(200)  // donus codu 200 mu demek

                .body("places.'place name'", hasItem("Karakuyu Köyü"));
        // butun place name-lerin herhansi birinde "Karakuyu Köyü" varmi

    }

    @Test
    public void bodyArrayHasSizeTest() {

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()   // donen body json data-si  ,  log.all() hamisini gosterir

                .statusCode(200)  // donus codu 200 mu demek

                .body("places", hasSize(1));  //

    }

    @Test
    public void combininigTest() {

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()   // donen body json data-si  ,  log.all() hamisini gosterir

                .statusCode(200)  // donus codu 200 mu demek

                .body("places", hasSize(1))  // size- i 1 mi

                .body("places[0].state", equalTo("California"))
                .body("places.'place name'", hasItem("Beverly Hills"));

    }

    @Test
    public void pathParentTest() {

        given()
                .pathParam("country", "us")
                .pathParam("postCode", 90210)
                .log().uri()// requist Link
                .when()
                .get("http://api.zippopotam.us/{country}/{postCode}")
                .then()
                .statusCode(200)
                .log().body()
        ;
    }

    @Test
    public void queryParamTest() {

        given()
                .param("page", 1)

                .log().uri()
                .when()
                .get("https://gorest.co.in/public/v1/users")
                .then()
                .statusCode(200)
                .log().body();

    }

    @Test

    public void queryParamTest2() {

        // https://gorest.co.in/public/v1/users?page=3
        // bu linkteki 1 den 10 kadar sayfaları çağırdığınızda response daki donen page degerlerinin
        // çağrılan page nosu ile aynı olup olmadığını kontrol ediniz.

        for (int i = 1; i <= 10; i++) {

            given()
                    .param("page", i)

                    .log().uri()
                    .when()
                    .get("https://gorest.co.in/public/v1/users")
                    .then()
                    .statusCode(200)
                    .log().body()
                    .body("meta.pagination.page", equalTo(i));

        }
    }


    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;

    @BeforeClass

    public void Setup() {

        baseURI = "https://gorest.co.in/public/v1";

        requestSpecification = new RequestSpecBuilder()
                .log(LogDetail.URI)
                .setContentType(ContentType.JSON)
                .build();
        responseSpecification = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .log(LogDetail.BODY)
                .build();

    }

    @Test
    public void requestResponseSpecificationn() {
        // https://gorest.co.in/public/v1/users?page=3

        given()
                .param("page", 1)  // ?page=1  şeklinde linke ekleniyor
                .spec(requestSpecification)

                .when()
                .get("/users")  // ?page=1

                .then()
                .spec(responseSpecification)
        ;
    }

    @Test
    public void extractingJsonPath() {

        String countryName = given().when()
                .get("http://api.zippopotam.us/us/90210")
                .then().extract().path("country");
        System.out.println("countryName = " + countryName);
        Assert.assertEquals(countryName, "United States");
    }

    @Test
    public void extractingJsonPath2() {

        //Place name yazdirin

        String placeName = given().when()
                .get("http://api.zippopotam.us/us/90210")
                .then().extract().path("places[0].'place name'");
        System.out.println("placeName = " + placeName);
        Assert.assertEquals(placeName, "Beverly Hills");

    }

    @Test
    public void extractingJsonPath3() {

        // https://gorest.co.in/public/v1/users  dönen değerdeki limit bilgisini yazdırınız.

        int limit =
                given().when().get("https://gorest.co.in/public/v1/users")
                        .then().statusCode(200)
                        .extract().path("meta.pagination.limit");

        System.out.println("limit = " + limit);

    }

    @Test
    public void extractingJsonPath4() {

        // https://gorest.co.in/public/v1/users  dönen değerdeki id-leri yazdırınız.

        List<Integer> ids =
                given().when().get("https://gorest.co.in/public/v1/users")
                        .then().statusCode(200)
                        .extract().path("data.id"); // butun id-leri ver

        System.out.println("ids = " + ids);
    }

    @Test
    public void extractingJsonPath5() {

        // https://gorest.co.in/public/v1/users  dönen değerdeki name-leri yazdırınız.

        List<String> names =
                given().when().get("https://gorest.co.in/public/v1/users")
                        .then()
                        .statusCode(200)
                        .extract().path("data.name");
        System.out.println("names = " + names);

    }

    @Test
    public void extractingJsonPathResponsAll() {

        // https://gorest.co.in/public/v1/users  dönen değerdeki name-leri yazdırınız.

        Response returnDatas =
                given().when().get("https://gorest.co.in/public/v1/users")
                        .then()
                        .statusCode(200)
                        .log().body()
                        .extract().response(); // donen butun datalar

        List<Integer> ids = returnDatas.path("data.id");
        List<String> names = returnDatas.path("data.name");
        int limit = returnDatas.path("meta.pagination.limit");

        System.out.println("limit = " + limit);
        System.out.println("names = " + names);
        System.out.println("ids = " + ids);

        Assert.assertTrue(names.contains("Gayatri Kaur DVM"));
        Assert.assertTrue(ids.contains(1222303));
        Assert.assertEquals(limit, 10, "Test neticesi xetali");
    }

    @Test

    public void extractJsonAll_POJO() { // POJO : JSON nesnesi : locationNesnesi

        Location location =
                given().when().get("http://api.zippopotam.us/us/90210")
                        .then()
                        //.log().body()
                        .extract().body().as(Location.class)// Location sablonuna
                ;

        System.out.println("location.getCountry() = " + location.getCountry());

        for (Place place : location.getPlaces())
            System.out.println("place = " + place);

        System.out.println(location.getPlaces().get(0).getPlacename());


    }

    @Test
    public void extractPOJO_Question() {
        // aşağıdaki endpointte(link)  Dörtağaç Köyü ait diğer bilgileri yazdırınız

        Location adana =
                given()
                        .when()
                        .get("http://api.zippopotam.us/tr/01000")

                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().body().as(Location.class)
                ;

        for (Place place : adana.getPlaces())
            if (place.getPlacename().equals("Karakuyu Köyü"))
            {
                System.out.println("place = " + place);
            }

    }

}