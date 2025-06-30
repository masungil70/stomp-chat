package ko.or.kosa.config.handler;

import java.io.IOException;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import ko.or.kosa.dao.MemberDAO;
import ko.or.kosa.entity.MemberVO;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired(required=true)
	private MemberDAO memberDAO;

	private String signingKey = "a29zYWR1em9uNWVycHpvbmVfc2VjdXJlX2tleV8hIyUm";
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication // 로그인한 사용자 정보가 있는 객체
	) throws IOException, ServletException {

		memberDAO.updateMemberLastLogin(authentication.getName());
		memberDAO.loginCountClear(authentication.getName());

		log.info("authentication ->" + authentication);
		
		//일반적인 경우 로그인 성공시 현재 세션을 제거하고 이동 경로는 sendRedirect로 이동한다
//		HttpSession session = request.getSession();
//		session.invalidate();
//		response.sendRedirect("/");

		//임시로 jwt token을 사용하여 stomp 통신시 전달하는 것을 확인하기 위해 작성한 코드임
		//***** 시작 
		MemberVO memberVO = (MemberVO) authentication.getPrincipal();

		//token 생성 
		SecretKey key = Keys.hmacShaKeyFor(signingKey.getBytes("UTF-8"));
		String jwt = Jwts.builder()
				.setClaims(Map.of("id", memberVO.getId(), 
						              "username" , memberVO.getUsername(), 
						              "email", memberVO.getEmail()))
				.signWith(key)
				.compact();

		log.info("jwt ->" + jwt);
		response.setHeader("Authorization", jwt);
		request.setAttribute("Authorization", jwt);

		RequestDispatcher dispatcher = request.getRequestDispatcher("/");
    dispatcher.forward(request, response);
    //***** 끝 
		//임시로 jwt token을 사용하여 stomp 통신시 전달하는 것을 확인하기 위해 작성한 코드임  
     
	}
}
