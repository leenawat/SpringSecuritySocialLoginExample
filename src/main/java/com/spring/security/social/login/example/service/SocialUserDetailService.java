package com.spring.security.social.login.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialAuthenticationException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Service;

import com.spring.security.social.login.example.dto.LocalUser;
import com.spring.security.social.login.example.dto.SocialUser;

/**
 * @author <a href="mailto:sunil.pulugula@wavemaker.com">Sunil Kumar</a>
 * @since 21/3/16
 */
@Service("socialUserDetailService")
public class SocialUserDetailService implements SocialUserDetailsService {
	
	private static final Logger logger = LoggerFactory.getLogger(SocialUserDetailService.class);

    @Autowired
    @Qualifier(value = "localUserDetailService")
    private UserDetailsService userDetailService;

    @Override
    public SocialUserDetails loadUserByUserId(final String userId) throws UsernameNotFoundException, DataAccessException {
		logger.info(null);
        LocalUser user = (LocalUser) userDetailService.loadUserByUsername(userId);
        if (user == null) {
        	logger.info("user == null");
            throw new SocialAuthenticationException("No local user mapped with social user " + userId);
        }
        return new SocialUser(user.getUserId(),user.getUsername(), user.getPassword(), user.isEnabled(), user.isAccountNonExpired(), user.isCredentialsNonExpired(), user.isAccountNonLocked(), user.getAuthorities());
    }
}
