package likelion13th.shop.login.auth.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import likelion13th.shop.domain.Address;
import likelion13th.shop.domain.User;
import likelion13th.shop.login.auth.dto.JwtDto;
import likelion13th.shop.login.auth.jwt.CustomUserDetails;
import likelion13th.shop.login.auth.service.JpaUserDetailsManager;
import likelion13th.shop.login.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JpaUserDetailsManager jpaUserDetailsManager; // Security 사용자 저장/조회 담당
    private final UserService userService;                     // JWT 발급 및 RefreshToken 저장 로직

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        // 1) 인증 결과에서 OAuth2User 추출
        //    - SecurityConfig에서 OAuth2 인증이 성공하면 Authentication의 principal이 OAuth2User가 됨
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

        // 2) 카카오에서 확장해 둔 attributes에서 provider_id, nickname 가져오기
        String providerId = (String) oAuth2User.getAttribute("provider_id");
        String nickname   = (String) oAuth2User.getAttribute("nickname");

        // 3) 민감정보 로그 마스킹 (운영 로그에 식별자 전체 노출 금지)
        String maskedPid  = (providerId != null && providerId.length() > 4) ? providerId.substring(0, 4) + "***" : "***";
        String maskedNick = (nickname != null && !nickname.isBlank()) ? "*(hidden)*" : "(none)";
        log.info("OAuth2 Success - providerId(masked)={}, nickname={}", maskedPid, maskedNick);

        // 4) 신규 회원 여부 확인 후, 없으면 생성
        //    - JpaUserDetailsManager.userExists(providerId)는 "사용자명" 기준으로 확인함
        //    - 우리 서비스는 providerId를 사용자명(username)으로 사용
        if (!jpaUserDetailsManager.userExists(providerId)) {
            // 4-1) 최소 필수값으로 User 엔티티 생성
            User newUser = User.builder()
                    .providerId(providerId)      // 고유 식별자
                    .usernickname(nickname)      // 프로필 닉네임
                    .deletable(true)             // 정책상 기본 true
                    .build();

            // 4-2) 예시 주소 세팅 (실서비스에서는 실제 입력 화면/동의 절차에서 받도록 해야 함)
            //      주의: 개인정보를 로그로 출력하거나 쿼리스트링으로 노출하지 않도록 관리
            newUser.setAddress(new Address("10540", "경기도 고양시 덕양구 항공대학로 76", "한국항공대학교"));
            log.info("// UserEntity address 확인: {}", newUser.getAddress().getAddress()); // ✅ Address 값이 null인지 확인
            // 🟡 2-2. Security 인증 등록
            CustomUserDetails userDetails = new CustomUserDetails(newUser);
            jpaUserDetailsManager.createUser(userDetails);
            log.info("// 신규 회원 등록 완료 (provider_id={})", providerId);
        } else {
            log.info("기존 회원 로그인 - providerId(masked)={}", maskedPid);
        }

        // 5) JWT 발급 및 Refresh 저장
        //    - userService.jwtMakeSave(providerId): Access/Refresh 발급 + Refresh 저장을 한 번에 처리
        JwtDto jwt = userService.jwtMakeSave(providerId);
        log.info("JWT 발급 완료 - providerId(masked)={}", maskedPid);

        // 4️⃣ 프론트에서 전달한 redirect_uri 파라미터 읽기
        String frontendRedirectUri = request.getParameter("redirect_uri");
        // ▶︎ 보안 상, 미리 허용해 둔 URI 리스트에 있는지 검증
        List<String> authorizedUris = List.of(
                "https://likelionshop.netlify.app",
                "http://localhost:3000"
        );
        if (frontendRedirectUri == null || !authorizedUris.contains(frontendRedirectUri)) {
            frontendRedirectUri = "https://likelionshop.netlify.app"; // 기본값
        }

        // accessToken 쿼리 파라미터로 붙여서 리다이렉트
        String redirectUrl = UriComponentsBuilder
                .fromUriString(frontendRedirectUri)
                .queryParam("accessToken", jwt.getAccessToken())
                .build()
                .toUriString();

        // 7) 프론트로 리다이렉트할 URL 구성
        //    - 현재 코드는 accessToken을 쿼리 파라미터로 전달
        //    - 보안 권장: 가능하면 HttpOnly Secure 쿠키(서버 설정)로 전달하는 방식을 고려

        log.info("Redirecting to authorized frontend host: {}", frontendRedirectUri);

        // 8) 리다이렉트
        response.sendRedirect(redirectUrl);
    }
}
