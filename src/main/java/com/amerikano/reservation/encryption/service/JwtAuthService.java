package com.amerikano.reservation.encryption.service;

import com.amerikano.reservation.encryption.domain.UserInfo;
import com.amerikano.reservation.encryption.domain.UserType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * 로그인 처리를 위한 JWT 토큰 서비스
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthService {

    private final String SECRET_KEY = "Reservation_SecretKey_ALSO32Byte";
    private final long TOKEN_VALID_TIME = 1000L * 60 * 60 * 24; // 만료 시간 : 1일

    /**
     * 로그인 인증 정보를 담은 JWT Token 생성
     * @Param: 유저 고유 아이디, 이메일, 타입(점장 or 고객)
     * @Return: JWT Token String
     */
    public String createToken(Long id, String email, UserType userType) {
        Claims claims = Jwts.claims()
            .setId(id.toString())
            .setSubject(email);

        claims.put("roles", userType);
        Date nowTime = new Date();
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(nowTime)
            .setExpiration(new Date(nowTime.getTime() + TOKEN_VALID_TIME))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * 토큰에서 유저 정보(UserInfo) 가져오기
     * @Param: JWT Token String
     * @Return: UserInfo(id, email)
     */
    public UserInfo getUserInfoFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8))
            .build()
            .parseClaimsJws(token)
            .getBody();

        return new UserInfo(
            Long.valueOf(claims.getId()), claims.getSubject()
        );
    }

    /**
     * 토큰을 분석하여 유효 여부를 확인
     * @Param: JWT Token String
     * @Return: 토큰의 유효 여부
     */
    public boolean validateToken(String token, UserType userType) {
        try {
            Jws<Claims> parsedClaimsJws = Jwts.parserBuilder().setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token);
            Claims claims = parsedClaimsJws.getBody();

            if (!userType.name().equals(claims.get("roles"))) {
                return false;
            }

            return !parsedClaimsJws.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * JWT 토큰에서 고유 id 값 얻어오기
     * @Param: JWT Token String
     * @Return: 토큰에 포함된 유저(점장, 손님) 고유 id
     */
    public Long getIdFromToken(String token) {
        return getUserInfoFromToken(token.substring(7)).getId();
    }
}
