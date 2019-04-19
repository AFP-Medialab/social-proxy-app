package com.afp.medialab.weverify.socialproxy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

import com.afp.medialab.weverify.socialproxy.service.ConnectionSignUpImpl;

@Configuration
@EnableSocial
@PropertySource("classpath:social-cfg.properties")
public class SocialConfig implements SocialConfigurer {

	private boolean autoSignUp = false;

	public void addConnectionFactories(ConnectionFactoryConfigurer cfConfig, Environment env) {
		try {
			this.autoSignUp = Boolean.parseBoolean(env.getProperty("social.auto-signup"));
		} catch (Exception e) {
			this.autoSignUp = false;
		}

		TwitterConnectionFactory tfactory = new TwitterConnectionFactory(env.getProperty("twitter.consumer.key"),
				env.getProperty("twitter.consumer.secret"));
		cfConfig.addConnectionFactory(tfactory);
	}

	public UserIdSource getUserIdSource() {
		return new AuthenticationNameUserIdSource();
	}

	public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
		InMemoryUsersConnectionRepository usersConnectionRepository = new InMemoryUsersConnectionRepository(
				connectionFactoryLocator);

		if (autoSignUp) {
			ConnectionSignUp connectionSignUp = new ConnectionSignUpImpl();
			usersConnectionRepository.setConnectionSignUp(connectionSignUp);
		} else {
			usersConnectionRepository.setConnectionSignUp(null);
		}
		return usersConnectionRepository;
	}

	@Bean
	public ConnectController connectController(ConnectionFactoryLocator connectionFactoryLocator,
			ConnectionRepository connectionRepository) {
		return new ConnectController(connectionFactoryLocator, connectionRepository);
	}

}
