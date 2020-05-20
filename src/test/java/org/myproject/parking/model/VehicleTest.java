package org.myproject.parking.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.myproject.parking.model.vehicle.BigElectricCar;
import org.myproject.parking.model.vehicle.Sedan;
import org.myproject.parking.model.vehicle.Vehicle;
import org.myproject.parking.model.vehicle.VehicleType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class VehicleTest {

    @Test
    public void testVehicleJsonSerialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        Sedan sedan = new Sedan("F1-Plate");
        BigElectricCar bigElectricCar = new BigElectricCar("Electric-plate");

        List<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(sedan);
        vehicles.add(bigElectricCar);

        String jsonDataString = mapper.writeValueAsString(vehicles);
        assertThat(jsonDataString, containsString("\"type\":\"gasoline\",\"license_plate\":\"F1-Plate\""));
        assertThat(jsonDataString, containsString("\"type\":\"fifty_kw\",\"license_plate\":\"Electric-plate\""));
    }

    @Test
    public void testAbstractVehicleDeserializingToProperSubClass()
            throws IOException {
        String json =
                "{\n" +
                        "  \"license_plate\": \"somePlate\",\n" +
                        "  \"type\": \"gasoline\"\n" +
                        "}";
        ObjectMapper mapper = new ObjectMapper();

        Vehicle car = mapper.reader().forType(Vehicle.class).readValue(json);
        assertThat(VehicleType.GASOLINE, is(car.getType()));
    }

    @Test
    public void checkVehicleType() throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        VehicleType s1 = mapper.readValue("\"GaSoline\"", VehicleType.class);
        VehicleType s2 = mapper.readValue("\"twenty_KW\"", VehicleType.class);
        assertThat(VehicleType.GASOLINE, is(s1));
        assertThat(VehicleType.TWENTY_KW, is(s2));
    }
}
