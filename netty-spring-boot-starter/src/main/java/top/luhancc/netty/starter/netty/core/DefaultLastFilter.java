package top.luhancc.netty.starter.netty.core;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**
 * 一个空的没有任何实现的Filter
 *
 * @author luHan
 * @create 2021/2/24 17:04
 * @since 1.0.0
 */
public class DefaultLastFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
