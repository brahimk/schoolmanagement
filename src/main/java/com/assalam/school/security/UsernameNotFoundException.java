package com.assalam.school.security;

import javax.ws.rs.NotAuthorizedException;

public class UsernameNotFoundException extends NotAuthorizedException {

	private static final long serialVersionUID = -6763000982838245302L;

	public UsernameNotFoundException(String message) {
        super(message);
    }
}
