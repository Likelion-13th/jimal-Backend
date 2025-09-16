package likelion13th.shop.login.auth.service;

import likelion13th.shop.global.exception.GeneralException;
import likelion13th.shop.domain.User;
import likelion13th.shop.global.api.ErrorCode;
import likelion13th.shop.login.auth.jwt.CustomUserDetails;
import likelion13th.shop.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JpaUserDetailsManager implements UserDetailsManager {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public JpaUserDetailsManager(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String providerId) throws UsernameNotFoundException{
        User user = userRepository.findByProviderId(providerId)
                .orElseThrow(() -> {
                    log.warn("사용자 정보 없음!");
                    throw new GeneralException(ErrorCode.USER_NOT_FOUND);
                });

        return CustomUserDetails.fromEntity(user);
    }

    @Override
    public void createUser(UserDetails user){
        if(userExists(user.getUsername())){
            throw new GeneralException(ErrorCode.ALREADY_USED_NICKNAME);
        }

        try{
            User newUser = ((CustomUserDetails) user).toEntity();

            userRepository.save(newUser);
            log.info("사용자 생성 완료");
        } catch(ClassCastException e){
            log.error("UserDetails -> CustomUserDetails로 변환 실패!");
            throw new GeneralException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean userExists(String providerId){
        return userRepository.existsByProviderId(providerId);
    }

    /**
     * // 사용자 정보 업데이트 (현재 미구현)
     * // - 소셜 로그인 시 서버에서 직접 갱신할 데이터 범위가 명확해진 뒤 구현 권장
     */
    @Override
    public void updateUser(UserDetails user) {
        log.error("사용자 정보 업데이트는 지원되지 않음 (provider_id): {}", user.getUsername());
        throw new UnsupportedOperationException("사용자 업데이트 기능은 아직 지원되지 않습니다.");
    }

    /**
     * // 사용자 삭제 (현재 미구현)
     * // - 실제 삭제 대신 '탈퇴 플래그'로 관리하는 소프트 삭제 전략을 권장
     */
    @Override
    public void deleteUser(String providerId) {
        log.error("사용자 삭제는 지원되지 않음 (provider_id): {}", providerId);
        throw new UnsupportedOperationException("사용자 삭제 기능은 아직 지원되지 않습니다.");
    }

    /**
     * // 비밀번호 변경 (소셜 로그인은 비밀번호를 사용하지 않음)
     * // - 자체 회원 가입/로그인 기능을 추가할 때 구현
     */
    @Override
    public void changePassword(String oldPassword, String newPassword) {
        log.error("비밀번호 변경은 지원되지 않음.");
        throw new UnsupportedOperationException("비밀번호 변경 기능은 아직 지원되지 않습니다.");
    }

}
/*
 1) 왜 필요한가?
 - Spring Security가 DB와 통신하여 사용자 정보를 조회하고 새로 생성하는 로직을 처리하기 위해 필요합니다.
 - 로그인 시 loadUserByUsername을 통해 DB에서 사용자 정보를 조회하고, createUser를 통해 DB에 신규 사용자를 저장합니다.
 2) 없으면/틀리면?
 - 없으면 Spring Security는 DB에 저장된 사용자 정보를 접근할 수 없어서 DB를 이용한 모든 로그인 및 회원가입이 동작하지 않게 됩니다.
 - userExists()가 틀리면 신규 사용자와 기존 사용자를 구분하지 못해서 인증 절차가 꼬일 수 있습니다.
 3) 핵심 설계 포인트(코드와 함께)
 - 소셜 로그인에서 필요 없는 기능에 대해 UnsupportedOperationException을 발생시켜 해당 기능이 의도적으로 지원되지 않음을 알립니다.
   @Override
    public void changePassword(String oldPassword, String newPassword) {
        log.error("비밀번호 변경은 지원되지 않음.");
        throw new UnsupportedOperationException("비밀번호 변경 기능은 아직 지원되지 않습니다.");
    }
 */
