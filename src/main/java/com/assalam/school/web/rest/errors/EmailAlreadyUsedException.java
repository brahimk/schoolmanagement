package com.assalam.school.web.rest.errors;

public class EmailAlreadyUsedException extends BadRequestAlertException {

    /**
	 *
	 */
	private static final long serialVersionUID = 3270476999933571639L;

	public EmailAlreadyUsedException() {
        super("Email is already in use!", "userManagement", "emailexists");
    }
}
