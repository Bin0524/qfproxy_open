package com.qunar.qfproxy.interceptor;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * CorsFilter
 *
 * @author binz.zhang
 * @date 2018/12/3
 */
@Component
public class CorsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse reqs = (HttpServletResponse) servletResponse;

        reqs.addHeader("Access-Control-Allow-Origin", "*");
        reqs.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        reqs.addHeader("Access-Control-Allow-Headers", "Content-Type");
        reqs.addHeader("Access-Control-Max-Age", "1800");//30 min
        if (req.getMethod().equals("OPTIONS")) {
            reqs.setStatus(200);
        }else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}


