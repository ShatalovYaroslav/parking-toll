
package org.myproject.parking.rest;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.myproject.parking.Application;
import org.myproject.parking.model.vehicle.BigElectricCar;
import org.myproject.parking.model.vehicle.Sedan;
import org.myproject.parking.model.vehicle.Vehicle;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.myproject.parking.IntegrationTestConfig.FIXED_PRICE_POLICY;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;


/**
 * Created by Yaroslav on 5/20/2020.
 * integration testing for REST API using RestAssured
 */

@ActiveProfiles("test")
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
@WebIntegrationTest(randomPort = true)
public class ParkingControllerTest extends AbstractRestTest {

    private String REST_SERVICE_URI;

    @Before
    public void configureRestAssured() {
        REST_SERVICE_URI = "http://localhost:" + serverPort + "/parking/lot/";
        RestAssured.port = serverPort;
    }

    @Test
    public void testPlaceVehicle() {
        String licensePlate = "my-Number-Plate";
        Vehicle testCar = new BigElectricCar(licensePlate);

        Integer parkingTestId = 1;

        Response response = given().body(testCar)
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .header("Content-Type", ContentType.JSON)
                .pathParam("parkId", parkingTestId)
                .when()
                .post(REST_SERVICE_URI + "{parkId}/place");

        response.then()
                .assertThat()
                .statusCode(org.apache.http.HttpStatus.SC_OK)
                .body("spotRent.vehiclePlate", is(testCar.getLicensePlate()))
                .body("spotRent.arrivalTime", notNullValue())
                .body("spotRent.leavingTime", nullValue())
                .body("free", is(false));

        int spotIdWithVehicle = response.then()
                .extract()
                .path("spotId");

        //check that parking spot has a placed vehicle
        given().body(testCar)
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .header("Content-Type", ContentType.JSON)
                .pathParam("parkingLotId", parkingTestId)
                .pathParam("parkingSpotId", spotIdWithVehicle)
                .when()
                .get(REST_SERVICE_URI + "{parkingLotId}/spot/{parkingSpotId}")
                .then()
                .assertThat()
                .statusCode(org.apache.http.HttpStatus.SC_OK)
                .body("spotRent", notNullValue())
                .body("spotRent.vehiclePlate", is(testCar.getLicensePlate()))
                .body("spotRent.arrivalTime", notNullValue())
                .body("free", is(false));

    }

    @Test
    public void testLeaveParking() {
        String licensePlate = "Number-Plate";
        Vehicle testCar = new Sedan(licensePlate);
        Integer parkingTestId = 1;

        //first: place Vehicle in parking
        Response response = given().body(testCar)
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .header("Content-Type", ContentType.JSON)
                .pathParam("parkId", parkingTestId)
                .when()
                .post(REST_SERVICE_URI + "{parkId}/place");

        response.then()
                .assertThat()
                .statusCode(org.apache.http.HttpStatus.SC_OK);

        int spotIdWithVehicle = response.then()
                .extract()
                .path("spotId");

        // Leave Parking and get Invoice
        given().body(testCar)
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .header("Content-Type", ContentType.JSON)
                .pathParam("parkId", parkingTestId)
                .when()
                .put(REST_SERVICE_URI + "{parkId}/leave")
                .then()
                .assertThat()
                .statusCode(org.apache.http.HttpStatus.SC_OK)
                .body("parkingName", is("Test parking"))
                .body("licensePlate", is(testCar.getLicensePlate()))
                .body("arrivalTime", notNullValue())
                .body("leavingTime", notNullValue())
                .body("invoiceId", notNullValue())
                .body("cost", is(FIXED_PRICE_POLICY));

        //check this spot is free after vehicle left
        given().body(testCar)
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .header("Content-Type", ContentType.JSON)
                .pathParam("parkingLotId", parkingTestId)
                .pathParam("parkingSpotId", spotIdWithVehicle)
                .when()
                .get(REST_SERVICE_URI + "{parkingLotId}/spot/{parkingSpotId}")
                .then()
                .assertThat()
                .statusCode(org.apache.http.HttpStatus.SC_OK)
                .body("spotRent", nullValue())
                .body("free", is(true));
    }

    @Test
    public void testGetParkingSpot() {
        String licensePlate = "my-Number-Plate";
        Vehicle testCar = new BigElectricCar(licensePlate);

        Integer parkingTestId = 1;
        Integer parkingSpotId = 1;

        given().body(testCar)
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .header("Content-Type", ContentType.JSON)
                .pathParam("parkingLotId", parkingTestId)
                .pathParam("parkingSpotId", parkingSpotId)
                .when()
                .get(REST_SERVICE_URI + "{parkingLotId}/spot/{parkingSpotId}")
                .then()
                .assertThat()
                .statusCode(org.apache.http.HttpStatus.SC_OK)
                .body("spotRent", nullValue())
                .body("free", is(true));
    }
}

