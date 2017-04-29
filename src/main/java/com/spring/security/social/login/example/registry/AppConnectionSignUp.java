package com.spring.security.social.login.example.registry;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.facebook.api.Facebook;

import com.spring.security.social.login.example.dto.LocalUser;
import com.spring.security.social.login.example.dto.SocialProvider;
import com.spring.security.social.login.example.dto.UserRegistrationForm;
import com.spring.security.social.login.example.service.UserService;
import com.spring.security.social.login.example.util.SecurityUtil;

/**
 * If no local user associated with the given connection then connection signup
 * will create a new local user from the given connection.
 *
 * @author <a href="mailto:sunil.pulugula@wavemaker.com">Sunil Kumar</a>
 * @since 27/3/16
 */
public class AppConnectionSignUp implements ConnectionSignUp {

	
	private static final Logger logger = LoggerFactory.getLogger(AppConnectionSignUp.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	@Qualifier(value = "localUserDetailService")
	private UserDetailsService userDetailService;

	@Override
	public String execute(final Connection<?> connection) {
		logger.info("");
		 Facebook facebook = (Facebook) connection.getApi();
	        String [] fields = { "id", "email",  "first_name", "last_name" };
	        FacabookUser facabookUser = facebook.fetchObject("me", FacabookUser.class, fields);
	        UserRegistrationForm userDetails = toUserRegistrationObject(connection.getKey().getProviderUserId(),
				SecurityUtil.toSocialProvider(connection.getKey().getProviderId()), facabookUser);
			LocalUser userLocal = (LocalUser) userDetailService.loadUserByUsername(connection.getKey().getProviderUserId());
			if(userLocal == null){
				logger.info("");
				LocalUser user = (LocalUser) userService.registerNewUser(userDetails);
				return user.getUserId();
			}else{
				logger.info("");
				return userLocal.getUserId();
			}
	}

	private UserRegistrationForm toUserRegistrationObject(final String userId, final SocialProvider socialProvider,
			FacabookUser facebookUser) {
		logger.info("userid : "+ userId);
		logger.info("facebookUser.getUserId() : "+ facebookUser.getId());
		logger.info("");
		String password = randomAlphabetic(8);
		System.out.println("password : " + password);
		return UserRegistrationForm.getBuilder()
				.addUserId(userId)
				.addFirstName(facebookUser.getFirst_name())
				.addEmail(facebookUser.getEmail())
				.addPassword(password)
				.addSocialProvider(socialProvider)
				.build();
	}

}



class FacabookUser{
	private Long id;
	private String email;
	private String first_name;
	private String last_name;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	
}