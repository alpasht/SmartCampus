/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus;

/**
 *
 * @author imran
 */
public class Sensor  {

    private String id; // Unique id 
    private String type; // Category , e.g., " Temperature ", "Occupancy ", "CO2"
    private String status; // Current state: "ACTIVE ", " MAINTENANCE ", or " OFFLINE "
    private double currentValue; // The most recent measurement recorded
    private String roomId; // Foreign key linking to the Room where the sensor is located .
    
    public Sensor(String id, String type, String status, double currentValue, String roomId){
        this.id=id;
        this.type=type;
        this.status=status;
        this.currentValue=currentValue;
        this.roomId=roomId;
    }
}
