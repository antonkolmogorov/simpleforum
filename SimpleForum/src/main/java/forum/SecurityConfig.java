package forum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import forum.dao.ForumUserDetailService;
import forum.restful.MessageController;
import forum.restful.RegisterController;
import forum.restful.TopicController;

@Configuration
@EnableConfigurationProperties
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private ForumUserDetailService userDetailsService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.httpBasic().and()
			.requestMatchers().antMatchers(RegisterController.HANDSHAKE,
										   TopicController.MAPPING + TopicController.COMPOSE,
										   TopicController.MAPPING + TopicController.DELETE,
										   MessageController.MAPPING + MessageController.COMPOSE,
										   MessageController.MAPPING + MessageController.DELETE).and()
				.authorizeRequests()
				.anyRequest()
				.authenticated();
	}
}