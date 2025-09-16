package likelion13th.shop.login.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import likelion13th.shop.login.auth.dto.JwtDto;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.parsers.ReturnTypeParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {
    private final Key secretkey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;
    private final ReturnTypeParser returnTypeParser;

    public TokenProvider(
            @Value("${JWT_SECRET") String secretkey,
            @Value("${JWT_EXPIRATION") long accessTokenExpiration,
            @Value("${JWT_REFRESH_EXPIRATION") long refreshTokenExpiration, ReturnTypeParser returnTypeParser)
    {
       this.secretkey = Keys.hmacShaKeyFor(secretkey.getBytes());
       this.accessTokenExpiration = accessTokenExpiration;
       this.refreshTokenExpiration = refreshTokenExpiration;
       this.returnTypeParser = returnTypeParser;
    }

    public JwtDto generateToken(UserDetails userDetails) {
                log.info("JWT 생성: 사용자 {}", userDetails.getUsername());

                String userId = userDetails.getUsername();

                String authorities = userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(","));

                String accessToken = createToken(userId, authorities, accessTokenExpiration);

                String refreshToken = createToken(userId, null, refreshTokenExpiration);

                log.info("JWT 생성 완료: 사용자 {}", userDetails.getUsername());
                return new JwtDto(accessToken, refreshToken);
    }

    private String createToken(String providerId, String authorities, long expirationTime) {
                JwtBuilder builder = Jwts.builder()
                        .setSubject(providerId)
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                        .signWith(secretkey, SignatureAlgorithm.HS256);

                if (authorities != null) {
                    builder.claim("authorities", authorities);
                }

                return builder.compact().toString();

    }

    public boolean validateToken(String token) {
                try{
                    Jwts.parserBuilder()
                            .setSigningKey(secretkey)
                            .build()
                            .parseClaimsJws(token);
                    return true;
                }catch (JwtException e){
                    return false;
                }
    }

    public Claims parseToken(String token) {
                try{
                    return Jwts.parserBuilder()
                            .setSigningKey(secretkey)
                            .build()
                            .parseClaimsJws(token).getBody();
                }catch (ExpiredJwtException e){
                    log.warn("토큰 만료");
                    throw e;
                }catch (JwtException e){
                    log.warn("JWT 파싱 실패");
                    throw new RuntimeException(e);
                }
    }

    public Collection<? extends GrantedAuthority> getAuthorities(Claims claims) {
                String authoritiesString = claims.get("authorities", String.class);
                if (authoritiesString != null || authoritiesString.isEmpty()) {
                    log.warn("권한 정보가 없다 - 기본 ROLE_USER 부여");
                            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
                }
                return Arrays.stream(authoritiesString.split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
    }

    public Claims parseClaimsAllowExpired(String token) {
                try{
                    return Jwts.parserBuilder()
                            .setSigningKey(secretkey)
                            .build()
                            .parseClaimsJws(token).getBody();
                }catch (ExpiredJwtException e){
                    return e.getClaims();
                }
    }
}
/*
 1) 왜 필요한가?
 - JWT 생성, 검증, 정보 추출 등 토큰과 관련된 것을 관리하기 위해 필요합니다.
 - 인증에 성공한 사용자에게 access Token, refresh Token을 발급하는 역할을 합니다.
 - 고객이 API 요청 시 JWT가 유효한지 검증하고, 토큰에 담긴 사용자 정보를 찾아서 Spring Security가 사용할 수 있도록 제공합니다.
 2) 없으면/틀리면?
 - 없으면 JWT 관련 코드가 흩어져서 코드 중복, 유지보수 어려움 등 문제가 발생합니다.
 - secretkey가 틀리면 보안에 취약해집니다.
 - generateToken()이 틀리면 필수 정보가 누락된 토큰이 발급되어 보안 문제가 발생합니다.
 3) 핵심 설계 포인트(코드와 함께)
 - 토큰 검증 시 발생할 수 있는 모든 예외 처리
   public boolean validateToken(String token) {
                try{
                    Jwts.parserBuilder()
                            .setSigningKey(secretkey)
                            .build()
                            .parseClaimsJws(token);
                    return true;
                }catch (JwtException e){
                    return false;
                }
    }
 */