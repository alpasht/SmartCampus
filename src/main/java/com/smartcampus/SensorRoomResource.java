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
@Path("/rooms")
public class SensorRoomResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Room> getAllRooms() {
        return MockDatabase.ROOMS;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRoom(Room room) {
        if (room == null || room.getId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid room data").build();
        }
        MockDatabase.ROOMS.add(room);
        return Response.status(Response.Status.CREATED).entity(room).build();
    }

    @GET
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomById(@PathParam("roomId") String roomId) {
        for (Room room : MockDatabase.ROOMS) {
            if (room.getId().equals(roomId)) {
                return Response.ok(room).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Room not found").build();
    }
}
