package ko.or.kosa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ko.or.kosa.dao.MemberDAO;
import ko.or.kosa.entity.MemberVO;

//사용자 아이디를 이용하여 사용자 객체를 얻는 부분을 구현한 것
//해당 작업은 아이디 기준으로만 한다 

@Service
public class MemberService implements UserDetailsService {

	@Autowired
	private MemberDAO memberDAO;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
			
		MemberVO member = memberDAO.findByEmail(email);
		
		if (member == null) throw new UsernameNotFoundException("Not Found account.");
		
		memberDAO.loginCountInc(member);
		
		return member;
	}
}