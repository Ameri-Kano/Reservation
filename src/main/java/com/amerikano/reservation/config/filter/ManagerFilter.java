package com.amerikano.reservation.config.filter;

import com.amerikano.reservation.encryption.domain.UserType;
import com.amerikano.reservation.encryption.service.JwtAuthService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

/**
 * 점장만 이용할 수 있는 기능에 대한 토큰 체크 필터 (점장 정보 수정, 예약 관리 기능)
 */
@WebFilter(urlPatterns = "/manager/*")
@RequiredArgsConstructor
public class ManagerFilter implements Filter {

    private final JwtAuthService authService;
    private final String authHeader;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String tokenHeader = httpServletRequest.getHeader(authHeader);
        String token = tokenHeader.substring(7);

        // Authorization Header가 Bearer로 시작하는지 확인
        if (!tokenHeader.startsWith("Bearer ")) {
            throw new ServletException("올바르지 않은 접근입니다.");
        }
        // 토큰 기간이 만료된 경우(로그인 만료)
        if (!authService.validateToken(token, UserType.MANAGER)) {
            throw new ServletException("올바르지 않은 접근입니다.");
        }

        chain.doFilter(request, response);
    }
}
