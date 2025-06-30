package ko.or.kosa.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberVO implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String email;
	private String pwd;
	private String oauth;
	private String roles;
	private String accountExpired; 
	private String accountLocked;
	private int loginCount;
	private LocalDateTime lastLoginTime;
	
	@Override
	public String getPassword() {
		return this.getPwd();
	}
	
	@Override
	public String getUsername() {
		return this.getEmail();
	}
	
	 //계정이 갖고있는 권한 목록은 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        
        Collection<GrantedAuthority> collectors = new ArrayList<>();
        collectors.add(() -> {
            return "계정별 등록할 권한";
        });
        
        //collectors.add(new SimpleGrantedAuthority("user"));
        
        return collectors;
    }
    
	//계정이 만료되지 않았는지 리턴 (true: 만료 안됨)
    @Override
    public boolean isAccountNonExpired() {
    	return "N".equalsIgnoreCase(accountExpired);
        //return true;
    }

    //계정이 잠겨있는지 않았는지 리턴. (true: 잠기지 않음)
    @Override
    public boolean isAccountNonLocked() {
    	return "N".equalsIgnoreCase(accountLocked);
        //return true;
    }

    //비밀번호가 만료되지 않았는지 리턴한다. (true: 만료 안됨)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정이 활성화(사용가능)인지 리턴 (true: 활성화)
    @Override
    public boolean isEnabled() {
        return true;
    }
    
  	// ENUM으로 안하고 ,로 해서 구분해서 ROLE을 입력 -> 그걸 파싱!!
  	// 예제 ROLL 값 : "ROLE_USER","ROLE_MANAGER","ROLE_ADMIN"  
  	public List<String> getRoleList() {
  	    if (this.roles.length() > 0) {
  	        return Arrays.asList(this.roles.split(","));
  	    }
  	    return new ArrayList<>();
  	}

}
