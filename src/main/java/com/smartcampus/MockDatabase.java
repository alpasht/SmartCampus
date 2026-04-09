package com.smartcampus;

import com.smartcampus.Room;
import com.smartcampus.Sensor;
import com.smartcampus.SensorReading;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MockDatabase {
    public static final List<Room> ROOMS = new CopyOnWriteArrayList<>();
    public static final List<Sensor> SENSORS = new CopyOnWriteArrayList<>();
    public static final List<SensorReading> MODULES = new CopyOnWriteArrayList<>();

    static {
        // Initialise Rooms
        ROOMS.add(new Room("LIB-501","Library",40 ));
        ROOMS.add(new Room("DORM-303","Dormitory",3));

        // Initialise Sensors
        SENSORS.add(new Sensor("TEMP-001","Temperature","ACTIVE",23, "LIB-501"));
        SENSORS.add(new Sensor("TEMP-002","Temperature","ACTIVE",17, "DORM-303"));

        // Initialise SensorReadings
        MODULES.add(new SensorReading("6e19c698-3e92-4981-92e5-9539371d0584", 532, 20));
        MODULES.add(new SensorReading("2da2c2e4-1e87-40fa-b865-357bde9559d6", 489, 19));
    }
}