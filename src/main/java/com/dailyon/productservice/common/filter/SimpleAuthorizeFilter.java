package com.dailyon.productservice.common.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebFilter("/admin/**")
@Component
public class SimpleAuthorizeFilter implements Filter {
    private final String headerName = "role";  // TODO : 기환님 gateway 한 다음 확인
    private final String admin = "ADMIN";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(((HttpServletRequest) request).getHeader(headerName) == null || !((HttpServletRequest) request).getHeader(headerName).equals(admin)) {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "권한이 없는 사용자입니다");
            return;
        }
        chain.doFilter(request, response);
    }
}
