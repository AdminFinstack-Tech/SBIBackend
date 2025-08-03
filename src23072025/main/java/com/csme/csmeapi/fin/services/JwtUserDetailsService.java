package com.csme.csmeapi.fin.services;

import java.util.ArrayList;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.csme.csmeapi.fin.config.FinUtil;

/**
 * Service for handling JWT user details.
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {
	
	FinUtil finUtil = new FinUtil();
	
	Logger logger = LogManager.getLogger("CSMEMobile");
	/**
     * Loads user details by username.
     *
     * @param username The username to load user details for.
     * @return UserDetails object for the user.
     * @throws UsernameNotFoundException If user is not found.
     */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("Loading user details for username: "+username);

        if ("javainuse".equals(username)) {
            logger.debug("User found." + username);
            return new User(username, "12345", new ArrayList<>());
        } else {
            logger.warn("User not found." + username);
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
	}

}