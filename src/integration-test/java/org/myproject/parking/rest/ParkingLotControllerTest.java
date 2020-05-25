
package org.myproject.parking.rest;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.myproject.parking.Application;
import org.myproject.parking.model.ParkingLotMetadata;
import org.myproject.parking.pricing.PolicyType;
import org.myproject.parking.util.ParkingLotStartupFixture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;


@ActiveProfiles("test")
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
@WebIntegrationTest(randomPort = true)
public class ParkingLotControllerTest extends AbstractRestTest {

    private String REST_SERVICE_URI;

    @Before
    public void configureRestAssured() {
        REST_SERVICE_URI = "http://localhost:" + serverPort + "/parking/lot/";
        RestAssured.port = serverPort;
    }
    @Autowired
    ParkingLotStartupFixture parkingLotStartupFixture;

    @Test
    public void createParkingLot() {
        ParkingLotMetadata parkingLotMetadata = parkingLotStartupFixture.createParking();

        Response response = given().body(parkingLotMetadata)
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .header("Content-Type", ContentType.JSON)
                .when()
                .post(REST_SERVICE_URI);

        response.then()
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .body("name", is(parkingLotMetadata.getName()))
                .body("pricingConfig.policyType", is(PolicyType.FIXED_PLUS.toString()))
                .body("spots", hasSize(7))
                .body("spots[0].free", is(true));
    }

    @Test
    public void createParkingLotWrongPriceParametersException() {
        ParkingLotMetadata parkingLotMetadata = parkingLotStartupFixture.createParking();
        parkingLotMetadata.setPricingParameters(new HashMap<>());

        Response response = given().body(parkingLotMetadata)
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .header("Content-Type", ContentType.JSON)
                .when()
                .post(REST_SERVICE_URI);

        //check if the special pricing parameters are not present for PolicyType.FIXED_PLUS
        // it should be returned the response with exception
        response.then()
                .assertThat()
                .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void createParkingLotWrongPricePolicyTypeParametersException() {
        ParkingLotMetadata parkingLotMetadata = parkingLotStartupFixture.createParking();
        parkingLotMetadata.setPolicyType("WrongPricePolicyType");

        Response response = given().body(parkingLotMetadata)
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .header("Content-Type", ContentType.JSON)
                .when()
                .post(REST_SERVICE_URI);

        //check if the PolicyType has no Price Policy implementation it should be returned the response with exception
        response.then()
                .assertThat()
                .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void getParkingLot() {
        ParkingLotMetadata parkingLotMetadata = parkingLotStartupFixture.createParking();

        //add parking lot
        Response response = given().body(parkingLotMetadata)
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .header("Content-Type", ContentType.JSON)
                .when()
                .post(REST_SERVICE_URI);

        int parkingLotId = response.then()
                .extract()
                .path("parkingLotId");

        //get parking lot
        given()
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .header("Content-Type", ContentType.JSON)
                .pathParam("parkingLotId", parkingLotId)
                .when()
                .get(REST_SERVICE_URI + "{parkingLotId}/")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("name", is(parkingLotMetadata.getName()))
                .body("pricingConfig.policyType", is(PolicyType.FIXED_PLUS.toString()))
                .body("spots", hasSize(7))
                .body("spots[0].free", is(true));
    }

    @Test
    public void removeParkingLot() {
        ParkingLotMetadata parkingLotMetadata = parkingLotStartupFixture.createParking();

        //add parking lot
        Response response = given().body(parkingLotMetadata)
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .header("Content-Type", ContentType.JSON)
                .when()
                .post(REST_SERVICE_URI);

        int parkingLotId = response.then()
                .extract()
                .path("parkingLotId");

        //get parking lot
        given()
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .header("Content-Type", ContentType.JSON)
                .pathParam("parkingLotId", parkingLotId)
                .when()
                .delete(REST_SERVICE_URI+ "{parkingLotId}/")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("name", is(parkingLotMetadata.getName()))
                .body("pricingConfig.policyType", is(PolicyType.FIXED_PLUS.toString()))
                .body("spots", hasSize(7))
                .body("spots[0].free", is(true));

        //get parking lot
        given()
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .header("Content-Type", ContentType.JSON)
                .pathParam("parkingLotId", parkingLotId)
                .when()
                .get(REST_SERVICE_URI+ "{parkingLotId}/")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

}

