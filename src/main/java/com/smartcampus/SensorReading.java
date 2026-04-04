/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus;

/**
 *
 * @author imran
 */
public class SensorReading{
    private String id; // Unique reading event ID (UUID recommended )
    private long timestamp ; // Epoch time (ms) when the reading was captured
    private double value; // The actual metric value recorded bythe hardware

    public SensorReading(String id, long timestamp, double value){
    this.id=id;
    this.timestamp=timestamp;
    this.value=value;
}
    
}
