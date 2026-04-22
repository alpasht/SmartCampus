package com.smartcampus;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class LinkedResourceNotFoundExceptionMapper implements ExceptionMapper<LinkedResourceNotFoundException> {
    @Override
    public Response toResponse(LinkedResourceNotFoundException exception) {
        return Response.status(422) // Unprocessable Entity
                .entity(new ErrorResponse(exception.getMessage()))
                .type("application/json")
                .build();
    }
}
