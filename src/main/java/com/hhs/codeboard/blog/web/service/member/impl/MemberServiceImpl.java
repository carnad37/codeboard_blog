package com.hhs.codeboard.blog.web.service.member.impl;

import com.hhs.codeboard.blog.enumeration.SecurityAuthType;
import com.hhs.codeboard.blog.data.entity.member.entity.MemberEntity;
import com.hhs.codeboard.blog.data.repository.MemberDAO;
import com.hhs.codeboard.blog.web.service.member.MemberDto;
import com.hhs.codeboard.blog.web.service.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberDAO memberDAO;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
		Optional<MemberEntity> memberEntityWrapper = memberDAO.findByEmail(memberId);
		MemberEntity memberVO = memberEntityWrapper.orElseThrow(() -> new UsernameNotFoundException("가입정보가 없습니다"));
		//권한 리스트
		List<GrantedAuthority> authorities = new ArrayList<>();

		if (SecurityAuthType.ADMIN.matchTypeCode(memberVO.getUserType())) {
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
			authorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
		} else if (SecurityAuthType.NORMAL.matchTypeCode(memberVO.getUserType())) {
			authorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
		} else if (SecurityAuthType.PRE_MEMBER.matchTypeCode(memberVO.getUserType())) {
			throw new UsernameNotFoundException("가입승인 대기중입니다");
		}
		
		return new MemberDto(memberVO, authorities);
	}

	@Override
	public String insertUser(MemberEntity memberVO) {
        memberVO.setRegDate(LocalDateTime.now());
		memberVO.setUserType(SecurityAuthType.NORMAL.getTitle());
        // 비밀번호 암호화
        memberVO.setPassword(passwordEncoder.encode(memberVO.getPassword()));
        return memberDAO.save(memberVO).getEmail();
	}

}
