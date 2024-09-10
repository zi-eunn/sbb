package com.mysite.sbb.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {
    //UserDetailsService -> 스프링 시큐리티가 제공하는 인터페이스
    //아래의 메서드를 구현하도록 강제하는 인터페이스
    private final UserRepository userRepository;

    //loadUserByUsername : 사용자명(username)으로 스프링 시큐리티의 사용자(User) 객체를 조회하여 리턴하는 메서드
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<SiteUser> _siteUser = this.userRepository.findByusername(username);
        //SiteUser로 객체를 조회하고
        if (_siteUser.isEmpty()) {
            //사용자명에 해당하는 데이터가 없을 경우세는 UsernameNotFoundException발생
            throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
        }
        SiteUser siteUser = _siteUser.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        if ("admin".equals(username)) {
            //사용자 명이 admin인 경우 ADMIN권한 ROLE_ADMIN을 부여하고
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        } else {
            //이외의 경우에는 USER 권한 ROLE_USER 부여
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }
        //User 객체를 생성에 반환하는데
        //이 객체를 스프링 시큐리티에서 사용하며 User 생성자에는 사용자명, 비밀번호, 권한 리스트가 전달됨
        return new User(siteUser.getUsername(), siteUser.getPassword(), authorities);
        //참고로 : 스프링 시큐리티는 loadUserByUsername 메서드에 의해 리턴된 User 객체의 비밀번호가
        //사용자로부터 입력받은 비밀번호와 일치하는지를 검사하는 기능을 내부에 가짐
    }
}
