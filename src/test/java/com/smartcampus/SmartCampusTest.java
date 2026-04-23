package com.smartcampus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SmartCampusTest {

    private RoomResource roomResource;
    private SensorResource sensorResource;

    @BeforeEach
    public void setup() {
        MockDatabase.ROOMS.clear();
        MockDatabase.SENSORS.clear();
        
        roomResource = new RoomResource();
        sensorResource = new SensorResource();
        
        MockDatabase.ROOMS.add(new Room("R1", "Room 1", 10));
        MockDatabase.SENSORS.add(new Sensor("S1", "Temperature", "ACTIVE", 20.0, "R1"));
    }

    @Test
    public void testDeleteRoomWithSensors() {
        // Should block deletion if room has sensors
        assertThrows(RoomNotEmptyException.class, () -> {
            roomResource.deleteRoom("R1");
        });
    }

    @Test
    public void testDeleteRoomIdempotency() {
        // First, remove sensor to allow deletion
        MockDatabase.SENSORS.clear();
        
        // First delete
        Response response1 = roomResource.deleteRoom("R1");
        assertEquals(Response.Status.OK.getStatusCode(), response1.getStatus());
        
        // Second delete of the same room
        Response response2 = roomResource.deleteRoom("R1");
        // Current implementation returns 404
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response2.getStatus());
    }

    @Test
    public void testCreateSensorRoomCheck() {
        Sensor newSensor = new Sensor("S2", "CO2", "ACTIVE", 400.0, "NON-EXISTENT");
        assertThrows(LinkedResourceNotFoundException.class, () -> {
            sensorResource.createSensor(newSensor);
        });
    }

    @Test
    public void testSensorFilter() {
        MockDatabase.SENSORS.add(new Sensor("S2", "CO2", "ACTIVE", 400.0, "R1"));
        
        List<Sensor> all = sensorResource.getAllSensors(null);
        assertEquals(2, all.size());
        
        List<Sensor> co2Sensors = sensorResource.getAllSensors("CO2");
        assertEquals(1, co2Sensors.size());
        assertEquals("S2", co2Sensors.get(0).getId());
        
        List<Sensor> tempSensors = sensorResource.getAllSensors("temperature");
        assertEquals(1, tempSensors.size());
        assertEquals("S1", tempSensors.get(0).getId());
    }

 @Test
    public void testDeleteSensorRemovesSensorAndReadings() {
        MockDatabase.READINGS.put("S1", new java.util.concurrent.CopyOnWriteArrayList<>());

        Response response = sensorResource.deleteSensor("S1");
        Response fetchResponse = sensorResource.getSensorById("S1");

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), fetchResponse.getStatus());
        assertEquals("Sensor not found", fetchResponse.getEntity());
        assertFalse(MockDatabase.READINGS.containsKey("S1"));
    }

    @Test
    public void testDeleteSensorNotFound() {
        Response response = sensorResource.deleteSensor("UNKNOWN");
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
    
}
