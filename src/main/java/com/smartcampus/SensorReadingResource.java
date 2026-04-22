package com.smartcampus;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class SensorReadingResource {
    private String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SensorReading> getHistory() {
        return MockDatabase.READINGS.getOrDefault(sensorId, new ArrayList<>());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReading(SensorReading reading) {
        if (reading == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid reading data").build();
        }

        // Check if sensor exists and its status
        Sensor sensor = null;
        for (Sensor s : MockDatabase.SENSORS) {
            if (s.getId().equals(sensorId)) {
                sensor = s;
                break;
            }
        }

        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Sensor not found").build();
        }

        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException("Sensor is currently in MAINTENANCE and cannot accept new readings.");
        }

        // Initialize readings list if it doesn't exist
        MockDatabase.READINGS.putIfAbsent(sensorId, new CopyOnWriteArrayList<>());
        MockDatabase.READINGS.get(sensorId).add(reading);

        // Side Effect: Update currentValue on the sensor
        sensor.setCurrentValue(reading.getValue());

        return Response.status(Response.Status.CREATED).entity(reading).build();
    }
}
