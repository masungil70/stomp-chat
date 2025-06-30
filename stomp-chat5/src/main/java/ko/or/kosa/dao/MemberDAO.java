package ko.or.kosa.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import ko.or.kosa.entity.MemberVO;

@Mapper
@Repository("memberDAO")
public interface MemberDAO {

	public int updateMemberLastLogin(String email);
	public MemberVO findByEmail(String email);
	public void insert(MemberVO member);
	public void loginCountInc(MemberVO member);
	public void loginCountClear(String email);
	
}

