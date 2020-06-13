package com.assalam.school.service;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class InvalidPasswordException extends WebApplicationException {

	private static final long serialVersionUID = -8780070336797215035L;

	public InvalidPasswordException() {
        super(Response.status(BAD_REQUEST).entity("Incorrect Password").build());
    }
}
