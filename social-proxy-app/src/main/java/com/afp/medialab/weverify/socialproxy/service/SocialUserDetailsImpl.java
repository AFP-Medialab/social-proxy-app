package com.afp.medialab.weverify.socialproxy.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.social.security.SocialUserDetails;

public class SocialUserDetailsImpl implements SocialUserDetails {
	
	private List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();

	public SocialUserDetailsImpl(String userName) {
		super();
		this.userName = userName;
		GrantedAuthority grant = new SimpleGrantedAuthority("all");
		this.list.add(grant);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userName;

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return list;
	}

	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getUsername() {
		return this.userName;
	}

	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public String getUserId() {
		// TODO Auto-generated method stub
		return null;
	}

}
