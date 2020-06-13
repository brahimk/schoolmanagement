package com.assalam.school.security;

import javax.ws.rs.NotAuthorizedException;

public class UserNotActivatedException extends NotAuthorizedException {

	private static final long serialVersionUID = -6970934662669989281L;

	public UserNotActivatedException(String message) {
        super(message);
    }
}
