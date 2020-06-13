package com.assalam.school.web.rest.errors;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class EmailNotFoundException extends WebApplicationException {

    /**
	 *
	 */
	private static final long serialVersionUID = -544534355817268617L;

	public EmailNotFoundException() {
        super(Response.status(BAD_REQUEST).entity("Email address not registered").build());
    }
}
