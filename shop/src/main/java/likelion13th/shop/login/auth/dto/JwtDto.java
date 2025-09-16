package likelion13th.shop.login.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
@Getter
@Setter
public class JwtDto {
    private String accessToken;
    private String refreshToken;

    public JwtDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
/*
 1) 왜 필요한가?
 - 로그인 성공 후, 서버가 고객에게 JWT를 전달하기 위해 필요합니다.
 - accessToken, refreshToken을 하나로 해서 데이터 구조를 명확하게 만듭니다.
 2) 없으면/틀리면?
 - 없으면 불안정한 데이터 구조로 토큰이 전달되기 때문에 토큰에 오타가 발생하면 고객의 데이터를 못 찾아서 인증에 오류가 발생할 수 있습니다.
 3) 핵심 설계 포인트(코드와 함께)
 - accessToken, refreshToken을 각각 문자열로 정의하여, 두 토큰을 하나로 하기 위함.
   private String accessToken;
   private String refreshToken;
 */
