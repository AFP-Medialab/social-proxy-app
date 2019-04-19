package com.afp.medialab.weverify.socialproxy.service;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;

public class ConnectionSignUpImpl implements ConnectionSignUp {

	public String execute(Connection<?> connection) {
		UserProfile userProfile = connection.fetchUserProfile();
		String username = userProfile.getUsername();
		return username;
	}

}
