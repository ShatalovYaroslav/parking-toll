package org.myproject.parking.model.vehicle;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;


import java.io.IOException;


//according to https://stackoverflow.com/questions/24263904/deserializing-polymorphic-types-with-jackson
public class CustomDeserializer extends StdDeserializer<Vehicle> {

    protected CustomDeserializer() {
        super(Vehicle.class);
    }

    @Override
    public Vehicle deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        TreeNode node = p.readValueAsTree();
        TreeNode typeValue = node.get("type");

        // Select the concrete class based on a type
        String typeAsString = typeValue.toString().replace("\"", "");

        switch (VehicleType.valueOf(typeAsString)){
            case FIFTY_KW:
            return p.getCodec().treeToValue(node, BigElectricCar.class);
            case TWENTY_KW:
                return p.getCodec().treeToValue(node, SmallElectricCar.class);
            case GASOLINE:
                return p.getCodec().treeToValue(node, Sedan.class);
        }
        throw new RuntimeException("Was not able to parse Vehicle");
    }
}

