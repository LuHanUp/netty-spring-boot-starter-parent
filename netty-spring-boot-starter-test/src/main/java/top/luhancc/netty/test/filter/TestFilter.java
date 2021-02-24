package top.luhancc.netty.test.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author luHan
 * @create 2021/2/24 15:29
 * @since 1.0.0
 */
@Component
public class TestFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("进过了TestFilter。。。。doFilter");
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("进过了TestFilter。。。。init");
    }

    @Override
    public void destroy() {
        System.out.println("进过了TestFilter。。。。destroy");
    }
}
