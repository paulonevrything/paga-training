package com.apposit.training.video.rental.model;

import com.apposit.training.video.rental.data.auditing.Audited;

import com.apposit.training.video.rental.data.AbstractIntegerIdObject;

@Audited
public class User extends AbstractIntegerIdObject {

	private static final long serialVersionUID = 0L;

	private String firstName;
	private String lastName;
	private String username;

	public User(int id) {
		super(id);
	}

	public User() {
		super();
	}

	@Override
	public boolean isReadOnly() {

		return false;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
