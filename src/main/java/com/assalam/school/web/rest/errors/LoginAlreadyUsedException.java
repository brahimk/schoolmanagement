package com.assalam.school.web.rest.errors;

public class LoginAlreadyUsedException extends BadRequestAlertException {

    /**
	 *
	 */
	private static final long serialVersionUID = 8047506643786593570L;

	public LoginAlreadyUsedException() {
        super("Login name already used!", "userManagement", "userexists");
    }
}
