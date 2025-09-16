package likelion13th.shop.login.auth.utils;

import io.swagger.v3.oas.annotations.servers.Server;
import likelion13th.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Server
@RequiredArgsConstructor
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest){
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String providerId = oAuth2User.getAttributes().get("id").toString();

        @SuppressWarnings("unchecked")
        Map<String, Object> properties =
                (Map<String, Object>) oAuth2User.getAttributes().getOrDefault("properties", Collections.emptyMap());
        String nickname = properties.getOrDefault("nickname", "카카오사용자").toString();

        Map<String, Object> extendedAttributes = new HashMap<>(oAuth2User.getAttributes());
        extendedAttributes.put("provider_id", providerId);
        extendedAttributes.put("nickname", nickname);

        return new DefaultOAuth2User(
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
                extendedAttributes,
                "provider_id"
        );
    }
}
/*
 1) 왜 필요한가?
 - 소셜 로그인 제공자로부터 사용자 정보를 받아서 우리 서비스의 표준 형식에 맞게 가공하기 위해 필요합니다.
 - Spring Security에게 우리 서비스가 사용할 사용자의 고유 식별자가 무엇인지 명확하게 알려주는 역할을 합니다.
 2) 없으면/틀리면?
 - 없으면 OAuth2SuccessHandler과 같은 후처리 로직에서 일일이 처리하여 각기 다른 key로 사용자 정보를 꺼내야 해서 새로운 소셜 로그인을 추가할 때마다 모든 관련 코드를 수정해야 합니다.
 3) 핵심 설계 포인트(코드와 함께)
 - 소셜 로그인 제공자가 반환한 원본 데이터에서 우리 서비스가 필요로 하는 id와 정보를 추출합니다.
   String providerId = oAuth2User.getAttributes().get("id").toString();

        @SuppressWarnings("unchecked")
        Map<String, Object> properties =
                (Map<String, Object>) oAuth2User.getAttributes().getOrDefault("properties", Collections.emptyMap());
        String nickname = properties.getOrDefault("nickname", "카카오사용자").toString();
 - Spring Security용 사용자 식별자 지정
   return new DefaultOAuth2User(
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
                extendedAttributes,
                "provider_id"
 */
