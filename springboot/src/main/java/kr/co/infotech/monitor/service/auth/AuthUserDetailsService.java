package kr.co.infotech.monitor.service.auth;


import kr.co.infotech.monitor.dao.CommonDao;
import kr.co.infotech.monitor.dto.auth.AuthUserDetails;
import kr.co.infotech.monitor.dto.auth.MemberDto;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthUserDetailsService implements UserDetailsService {

	private PasswordEncoder encoder;
	private CommonDao commonDao;

	@Autowired
	public AuthUserDetailsService(CommonDao commonDao){
		this.commonDao = commonDao;
	}

	@SneakyThrows
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

		AuthUserDetails userDetails = new AuthUserDetails();
		MemberDto member = (MemberDto) commonDao.ds01SR("selectUserInfo",userId);

		userDetails.setUsername(member.getMemberId());
		userDetails.setPassword(member.getMemberPwd());

		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_"+member.getMemberRole()));
		userDetails.setAuthorities(authorities);
		userDetails.setEnabled(true);

		return userDetails;
	}

}
