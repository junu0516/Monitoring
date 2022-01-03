package kr.co.infotech.monitor.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import kr.co.infotech.monitor.service.auth.AuthUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private AuthUserDetailsService authUserDetailsService;

	@Autowired
	public SecurityConfig(AuthUserDetailsService authUserDetailsService){
		this.authUserDetailsService = authUserDetailsService;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.userDetailsService(authUserDetailsService);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		
		web.ignoring().antMatchers("/app-assets/**", "/assets/**","/com/**","/css/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable().authorizeRequests()
			.antMatchers("/monitoring/loginForm","/monitoring/failure","/api/**").permitAll()
			.anyRequest().permitAll()
			.and()
				.formLogin()
				.loginPage("/monitoring/loginForm")
				.usernameParameter("userId")
				.passwordParameter("userPassword")
				.loginProcessingUrl("/monitoring/login")
				.successHandler(new AuthenticationSuccessHandler() {
					@Override
					public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
							Authentication authentication) throws IOException, ServletException {
						response.sendRedirect("/monitoring/main");
					}
				})
				.failureHandler(new AuthenticationFailureHandler() {
					@Override
					public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
							AuthenticationException exception) throws IOException, ServletException {
						response.sendRedirect("/monitoring/failure");
					}
				})
				.permitAll()
			.and()
				.logout()
				.logoutUrl("/monitoring/logout")
				.logoutSuccessUrl("/monitoring/loginForm")
			.and()
				.exceptionHandling().accessDeniedPage("/monitoring/failure");
	}

	@Bean
	public PasswordEncoder encoder() {
		
		return new BCryptPasswordEncoder();
	}
		
}
