/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 *
 * @author imran
 */
@Path("/sensors")
public class SensorResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sensor> getAllSensors(@QueryParam("type") String type) {
        if (type == null || type.isEmpty()) {
            return MockDatabase.SENSORS;
        }
        
        java.util.List<Sensor> filteredSensors = new java.util.ArrayList<>();
        for (Sensor sensor : MockDatabase.SENSORS) {
            if (type.equalsIgnoreCase(sensor.getType())) {
                filteredSensors.add(sensor);
            }
        }
        return filteredSensors;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSensor(Sensor sensor) {
        if (sensor == null || sensor.getId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid sensor data").build();
        }

        // Reject duplicate sensor IDs, but allow new IDs to be created.
        for (Sensor existing : MockDatabase.SENSORS) {
            if (existing.getId().equals(sensor.getId())) {
                return Response.status(Response.Status.CONFLICT).entity("Sensor with this ID already exists").build();
            }
        }
        
        // Business Logic: Verify that the roomId specified in the request body actually exists.
        boolean roomExists = false;
        for (Room room : MockDatabase.ROOMS) {
            if (room.getId().equals(sensor.getRoomId())) {
                roomExists = true;
                break;
            }
        }
        if (!roomExists) {
            throw new LinkedResourceNotFoundException("Room with specified ID does not exist");
        }

        MockDatabase.SENSORS.add(sensor);
        return Response.status(Response.Status.CREATED).entity(sensor).build();
    }

    @GET
    @Path("/{sensorId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSensorById(@PathParam("sensorId") String sensorId) {
        for (Sensor sensor : MockDatabase.SENSORS) {
            if (sensor.getId().equals(sensorId)) {
                return Response.ok(sensor).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Sensor not found").build();
    }


 @DELETE
    @Path("/{sensorId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSensor(@PathParam("sensorId") String sensorId) {
        Sensor sensorToDelete = null;
        for (Sensor sensor : MockDatabase.SENSORS) {
            if (sensor.getId().equals(sensorId)) {
                sensorToDelete = sensor;
                break;
            }
        }

        if (sensorToDelete == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Sensor not found").build();
        }

        MockDatabase.SENSORS.remove(sensorToDelete);
        MockDatabase.READINGS.remove(sensorId);
        return Response.ok().entity("Sensor deleted successfully").build();
    }
    
    @Path("/{sensorId}/readings")
    public SensorReadingResource getSensorReadingResource(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }
}
