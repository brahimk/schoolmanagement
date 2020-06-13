package com.assalam.school.service;

public class UsernameAlreadyUsedException extends RuntimeException {

	private static final long serialVersionUID = 5489186203677388269L;

	public UsernameAlreadyUsedException() {
        super("Login name already used!");
    }
}
