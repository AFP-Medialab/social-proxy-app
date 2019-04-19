package com.afp.medialab.weverify.socialproxy.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.security.SocialUserDetails;

public class CustomLogout implements LogoutSuccessHandler {

	@Autowired
	private UsersConnectionRepository connectionRepository;

	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		if (authentication != null && authentication.getPrincipal() != null) {
			SocialUserDetails userDetails = (SocialUserDetails) authentication.getPrincipal();
			String userName = userDetails.getUsername();
			ConnectionRepository conn = connectionRepository.createConnectionRepository(userName);
			conn.removeConnections("twitter");
			request.getSession().invalidate();
		}
		response.setStatus(HttpServletResponse.SC_OK);
        //redirect to login
        response.sendRedirect("/login");
	}

}
