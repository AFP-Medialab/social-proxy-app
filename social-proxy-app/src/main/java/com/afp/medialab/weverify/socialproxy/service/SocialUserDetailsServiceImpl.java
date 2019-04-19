package com.afp.medialab.weverify.socialproxy.service;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class SocialUserDetailsServiceImpl implements SocialUserDetailsService {

	public SocialUserDetails loadUserByUserId(String userName) throws UsernameNotFoundException, DataAccessException {
		System.out.println("SocialUserDetailsServiceImpl.loadUserByUserId=" + userName);
		UserDetails userDetails = new SocialUserDetailsImpl(userName);
		return (SocialUserDetailsImpl) userDetails;
	}

}
