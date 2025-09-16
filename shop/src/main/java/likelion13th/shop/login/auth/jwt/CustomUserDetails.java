package likelion13th.shop.login.auth.jwt;

import likelion13th.shop.domain.Address;
import likelion13th.shop.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CustomUserDetails implements UserDetails {
    private Long userId;
    private String providerId;
    private String usernickname;
    private Address address;

    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user) {
        this.userId = user.getId();
        this.providerId = user.getProviderId();
        this.usernickname = user.getUsernickname();
        this.address = user.getAddress();
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public CustomUserDetails(String providerId, String password,  Collection<? extends GrantedAuthority> authorities) {
        this.providerId = providerId;
        this.userId = null;
        this.usernickname = null;
        this.authorities = authorities;
        this.address = null;
    }

    public static CustomUserDetails fromEntity(User entity) {
        return CustomUserDetails.builder()
                .userId(entity.getId())
                .providerId(entity.getProviderId())
                .usernickname(entity.getUsernickname())
                .address(entity.getAddress())
                .build();

    }

    public User toEntity(){
        return User.builder()
                .id(this.userId)
                .providerId(this.providerId)
                .usernickname(this.usernickname)
                .address(this.address)
                .build();
    }

    @Override
    public String getUsername() {
        return this.providerId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.authorities != null && this.authorities.isEmpty()) {
            return this.authorities;
        }
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        // 소셜 로그인은 비밀번호를 사용하지 않음
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        // 계정 만료 정책 사용 시 실제 값으로 교체
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 잠금 정책 사용 시 실제 값으로 교체
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 자격 증명(비밀번호) 만료 정책 사용 시 실제 값으로 교체
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 활성/비활성 정책 사용 시 실제 값으로 교체 (예: 탈퇴/정지 사용자)
        return true;
    }

}
/*
 1) 왜 필요한가?
 - Spring Security 인증 과정에서 표준 형식에 맞추기 위해 필요합니다.
 - User 엔터티를 Spring Security가 이해할 수 있게 변환해주는 역할을 합니다.
 - 인증이 완료된 후, 전역에서 User id, 닉네임, 주소 등 추가 정보에 접근할 수 있도록 데이터를 담아두는 역할을 합니다.
 2) 없으면/틀려면?
 - 없으면 Spring Security가 DB에 있는 User 정보를 어떻게 처리할지 알 수 없습니다. 따라서 커스텀 로그인 구현이 불가능합니다.
 - getUsername()이 틀리면 Spring Security는 사용자 id를 찾을 수 없어 JWT 토큰 검증 시 사용자를 매칭할 수 없게 되어 인증에 실패합니다.
 - getAuthorities()이 틀리면 사용자 권한이 잘못되어 권한 제한이 될 수 있습니다.
 3) 핵심 설계 포인트(코드와 함께)
 - 서비스의 사용자 정보를 Spring Security가 인식하고 사용할 수 있게끔 'UserDetails' 인터페이스를 구현합니다.
   public class CustomUserDetails implements UserDetails
 - Security용 식별자인 Username 지정
   @Override
    public String getUsername() {
        return this.providerId;
    }
 - DB에 별도의 Role 테이블이 없으면 모든 사용자는 기본적으로 'ROLE_USER'권한을 갖도록 고정합니다.
   @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.authorities != null && this.authorities.isEmpty()) {
            return this.authorities;
        }
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }
 */

