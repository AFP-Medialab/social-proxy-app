package com.afp.medialab.weverify.socialproxy.utils;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.security.SocialUserDetails;

import com.afp.medialab.weverify.socialproxy.service.SocialUserDetailsImpl;

public class SecurityUtil {

	public static void logInUser(String userName) {
		SocialUserDetails userDetails = new SocialUserDetailsImpl(userName);
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
	
	public static void logOutUser(Authentication authentication) {
		authentication.setAuthenticated(false);
		((UsernamePasswordAuthenticationToken)authentication).eraseCredentials();
		SecurityContextHolder.getContext().setAuthentication(null);
	}
}
