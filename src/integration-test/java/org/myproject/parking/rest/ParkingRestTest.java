
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
public class ParkingRestTest extends AbstractRestTest {

    private String REST_SERVICE_URI;

    @Before
    public void configureRestAssured() {
        REST_SERVICE_URI = "http://localhost:" + serverPort + "/parking/";
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
                .body("spotRent.vehicle.type", is(testCar.getType().toString()))
                .body("spotRent.vehicle.licensePlate", is(testCar.getLicensePlate()))
                .body("spotRent.arrivalTime", notNullValue())
                .body("spotRent.leavingTime", nullValue())
                .body("free", is(false));

    }

    @Test
    public void testLeaveParking() {
        String licensePlate = "Number-Plate";
        Vehicle testCar = new Sedan(licensePlate);
        Integer parkingTestId = 1;

        //first: place Vehicle in parking
        given().body(testCar)
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .header("Content-Type", ContentType.JSON)
                .pathParam("parkId", parkingTestId)
                .when()
                .post(REST_SERVICE_URI + "{parkId}/place")
                .then()
                .assertThat()
                .statusCode(org.apache.http.HttpStatus.SC_OK);

        // Leave Parking and get Invoice
        given()
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .header("Content-Type", ContentType.JSON)
                .pathParam("parkId", parkingTestId)
                .queryParam("licensePlate", testCar.getLicensePlate())
                .when()
                .put(REST_SERVICE_URI + "{parkId}/leave")
                .then()
                .assertThat()
                .statusCode(org.apache.http.HttpStatus.SC_OK)
                .body("parkingName", is("Test parking"))
                .body("licensePlate", is(testCar.getLicensePlate()))
                .body("arrivalTime", notNullValue())
                .body("leavingTime", notNullValue())
                .body("cost", is(FIXED_PRICE_POLICY));
    }
}

