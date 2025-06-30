//package ko.or.kosa.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//import ko.or.kosa.config.handler.AuthFailureHandler;
//import ko.or.kosa.config.handler.AuthSuccessHandler;
//import ko.or.kosa.service.MemberService;
//
//@Configuration
//@EnableWebSecurity // 시큐리티 필터 등록
//@EnableGlobalMethodSecurity(prePostEnabled = true) 
//// 특정 페이지에 특정 권한이 있는 유저만 접근을 허용할 경우 권한 및 
//// 인증을 미리 체크하겠다는 설정을 활성화한다.
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//	
//	@Autowired
//	private MemberService memberService;
//	@Autowired
//	private AuthSuccessHandler authSuccessHandler;
//	@Autowired
//	private AuthFailureHandler authFailureHandler;
//	
//	// BCryptPasswordEncoder는 Spring Security에서 제공하는 비밀번호 암호화 객체 (BCrypt라는 해시 함수를 이용하여 패스워드를 암호화 한다.)
//	// 회원 비밀번호 등록시 해당 메서드를 이용하여 암호화해야 로그인 처리시 동일한 해시로 비교한다.
//	// 의존성 주입을 위한 함수를 Bean 객체로 리턴할 수 있게 함수를 구현한다 
//	@Bean
//	public BCryptPasswordEncoder encryptPassword() {
//		return new BCryptPasswordEncoder();
//	}
//	
//	// 시큐리티가 로그인 과정에서 password를 가로챌때 해당 해쉬로 암호화해서 비교한다.
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		                                       //아래 부분은 의존성 주입 함수를 호출함        
//		auth.userDetailsService(memberService).passwordEncoder(encryptPassword());
//	}
//	
//  @Override
//  @Bean
//  public AuthenticationManager authenticationManagerBean() throws Exception {
//      return super.authenticationManagerBean();  // 반드시 선언
//  }
//  
//	//url 설정 
//	@Override
//    protected void configure(HttpSecurity http) throws Exception {
//		/*
//		 csrf 토큰 활성화시 사용
//		 쿠키를 생성할 때 HttpOnly 태그를 사용하면 클라이언트 스크립트가 보호된 쿠키에 액세스하는 위험을 줄일 수 있으므로 쿠키의 보안을 강화할 수 있다.
//		*/
//		//http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//		
//    http.csrf().disable()	// csrf 토큰을 비활성화
//      .authorizeRequests() // 요청 URL에 따라 접근 권한을 설정
//			.antMatchers("/", "/loginForm.do", "/js/**","/css/**","/image/**").permitAll() // 해당 경로들은 접근을 허용
//			.anyRequest() // 다른 모든 요청은
//			.authenticated() // 인증된 유저만 접근을 허용
//		.and()
//			.formLogin() // 로그인 폼은
//			.loginPage("/loginForm.do") // 해당 주소로 로그인 페이지를 호출한다.
//			.loginProcessingUrl("/login.do") // 해당 URL로 요청이 오면 스프링 시큐리티가 가로채서 로그인처리를 한다. -> loadUserByName
//			.successHandler(authSuccessHandler) // 성공시 요청을 처리할 핸들러
//			.failureHandler(authFailureHandler) // 실패시 요청을 처리할 핸들러
//		.and()
//			.logout()
//			.logoutRequestMatcher(new AntPathRequestMatcher("/logout.do")) // 로그아웃 URL
//		  .logoutSuccessUrl("/loginForm.do") // 성공시 리턴 URL
//		  .invalidateHttpSession(true) // 인증정보를 지우하고 세션을 무효화
//		  .deleteCookies("JSESSIONID") // JSESSIONID 쿠키 삭제
//			.permitAll()
//		.and()
//			.sessionManagement()
//      .maximumSessions(1) // 세션 최대 허용 수 1, -1인 경우 무제한 세션 허용
//      .maxSessionsPreventsLogin(false) // true면 중복 로그인을 막고, false면 이전 로그인의 세션을 해제
//      .expiredUrl("/loginForm.do?error=true&exception=Have been attempted to login from a new place. or session expired");  // 세션이 만료된 경우 이동 할 페이지를 지정
//    
//    
//    }
//	
//}
