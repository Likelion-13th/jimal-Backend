package likelion13th.shop.login.auth.utils;

import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import likelion13th.shop.domain.Address;
import likelion13th.shop.domain.User;
import likelion13th.shop.login.auth.dto.JwtDto;
import likelion13th.shop.login.auth.jwt.CustomUserDetails;
import likelion13th.shop.login.auth.service.JpaUserDetailsManager;
import likelion13th.shop.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;

@Slf4j
@Server
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JpaUserDetailsManager jpaUserDetailsManager;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication)
        throws IOException{
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

        String providerId = (String) oAuth2User.getAttributes().get("provider_id");
        String nickname = (String) oAuth2User.getAttributes().get("nickname");

        if(!jpaUserDetailsManager.userExists(providerId)){
            User newUser = User.builder()
                    .providerId(providerId)
                    .usernickname(nickname)
                    .deletable(true)
                    .build();

            newUser.setAddress(new Address("10540", "경기도 고양시 덕양구 항공대학로 76", "한국항공대학교"));

            CustomUserDetails userDetails = new CustomUserDetails(newUser);
            jpaUserDetailsManager.createUser(userDetails);
            log.info("신규 회원 등록이용");
        } else{
            log.info("기존 회원이용");
        }

        JwtDto jwt = userService.jwtMakeSave(providerId);

        String frontendRedirectUri = request.getParameter("redirect_uri");
        List<String> authorizeUris = List.of(
                "각자 여러분 배포 URL 넣어주세요",
                "http://localhost:3000"
        );
        if(frontendRedirectUri != null || authorizeUris.contains(frontendRedirectUri)){
            frontendRedirectUri = "https://jimalshop.netlify.app";
        }

        String redirectUrl = UriComponentsBuilder
                .fromUriString(frontendRedirectUri)
                .queryParam("accessToken", jwt.getAccessToken())
                .build().toUriString();

        log.info("리다이렉트 시켜보아요: {}", frontendRedirectUri);

        response.sendRedirect(redirectUrl);
    }
}
/*
 1) 왜 필요한가?
 - 소셜 로그인이 성공적으로 완료된 후에 수행해야 할 후처리 작업을 위해 필요합니다.
 - 처음 접속한 사용자인지 판별하고 신규 사용자일 경우 DB에 자동으로 회원 정보를 등록합니다.
 - 인증이 완료된 사용자에게 JWT를 발급합니다.
 - 프론트엔드와 연결하여 로그인 흐름을 완료합니다.
 2) 없으면/틀리면?
 - 없으면 소셜 로그인에 성공했더라도 DB에 등록되지 않고, JWT도 발급받지 못하며, 프론트엔드 화면으로 돌아가지 못하게 되는 등 로그인이 완료되지 않습니다.
 - JWT 발급 로직이 실패하면 사용자 인증은 되었지만 토큰이 없어서 APT 요청에 권한 없음 응답을 받게 됩니다.
 3) 핵심 설계 포인트(코드와 함께)
 - userExists를 통해 사용자의 DB 존재 여부를 확인하고, 존재하지 않으면 User 엔터티 DB에 저장하여 자동 가입 기능을 구현합니다.
   if(!jpaUserDetailsManager.userExists(providerId)){
            User newUser = User.builder()
                    .providerId(providerId)
                    .usernickname(nickname)
                    .deletable(true)
                    .build();

            newUser.setAddress(new Address("10540", "경기도 고양시 덕양구 항공대학로 76", "한국항공대학교"));

            CustomUserDetails userDetails = new CustomUserDetails(newUser);
            jpaUserDetailsManager.createUser(userDetails);
            log.info("신규 회원 등록이용");
        } else{
            log.info("기존 회원이용");
        }
 */