package com.pro.pair.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.pro.pair.handler.LoginFailureHandler;
import com.pro.pair.handler.LoginSuccessHandler;
import com.pro.pair.member.model.service.MemberService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor // 생성자 주입을 자동으로 설정
public class SecurityConfig {

	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;
	private final LoginSuccessHandler loginSuccessHandler;
	private final LoginFailureHandler loginFailureHandler;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf((csrfConfig) -> csrfConfig.disable())
				.authorizeHttpRequests(
						auto -> auto.requestMatchers("/", "/member/**").permitAll()
						.requestMatchers("/admin/**").permitAll()
						.requestMatchers("/member/mypage").hasAnyRole("MEMBER", "ADMIN")
						.anyRequest().permitAll())	//기본 로그인 페이지 나오지 않도록 설정 
				//로그인 
				.formLogin(formLogin -> formLogin.loginPage("/member/signin").defaultSuccessUrl("/")
						.successHandler(loginSuccessHandler)	//로그인 성공시 
						.failureHandler(loginFailureHandler)	//로그인 실패시 
						.usernameParameter("username")
						.passwordParameter("password"))
				//로그아웃 
				.logout(logout -> logout
					    .logoutUrl("/member/signout") // 로그아웃 URL
					    .deleteCookies("JSESSIONID")
					    .invalidateHttpSession(true)
					    .logoutSuccessUrl("/")
					)
				//
				.exceptionHandling(ext->ext.accessDeniedPage("/common/denied"))
				//
				.headers(headerConfig->headerConfig.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

		return http.build();

	}

	public AuthenticationManager authenticationManager(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(memberService).passwordEncoder(passwordEncoder);
		return auth.build();
	}

//
//	@Bean
//	public UserDetailsService userDetailsService() {
//		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//		
//		manager.createUser(User.withUsername("user1").password("1234").roles("user").build());
//
//		return manager;
//	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/css/**", "/js/**", "/image/**");
	}
}
