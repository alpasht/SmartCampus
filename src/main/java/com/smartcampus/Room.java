/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author imran
 */
public class Room  {
    private String id; // unique id
    private String name; 
    private int capacity ; // Maximum occupancy for safety regulations
    private List <String > sensorIds = new ArrayList <>(); 
    
    public Room(String id, String name, int capacity){
        this.id=id;
        this.name=name;
        this.capacity=capacity;
    }
}
