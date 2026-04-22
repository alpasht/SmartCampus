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
public class RoomResource {
    private GenericDAO<Room> roomDAO = new GenericDAO<>(MockDatabase.ROOMS);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Room> getAllRooms() {
        return roomDAO.getAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRoom(Room room) {
        if (room == null || room.getId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid room data").build();
        }
        roomDAO.add(room);
        return Response.status(Response.Status.CREATED).entity(room).build();
    }

    @GET
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomById(@PathParam("roomId") String roomId) {
        Room room = roomDAO.getById(roomId);
        if (room != null) {
            return Response.ok(room).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Room not found").build();
        }
    }

    @DELETE
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        // Business Logic: A room cannot be deleted if it still has active sensors assigned to it.
        for (Sensor sensor : MockDatabase.SENSORS) {
            if (roomId.equals(sensor.getRoomId())) {
                throw new RoomNotEmptyException("Cannot delete room: the room is currently occupied by active hardware.");
            }
        }

        Room room = roomDAO.getById(roomId);
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Room not found").build();
        }

        roomDAO.delete(roomId);
        return Response.ok().entity("Room deleted successfully").build();
    }
}
