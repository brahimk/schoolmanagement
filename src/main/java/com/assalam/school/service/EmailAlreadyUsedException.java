package com.assalam.school.service;

public class EmailAlreadyUsedException extends RuntimeException {

	private static final long serialVersionUID = -1673772830973872224L;

	public EmailAlreadyUsedException() {
        super("Email is already in use!");
    }
}
