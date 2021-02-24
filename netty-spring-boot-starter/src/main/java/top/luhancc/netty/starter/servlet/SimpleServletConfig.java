package top.luhancc.netty.starter.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;

/**
 * @author luHan
 * @create 2021/2/24 09:44
 * @since 1.0.0
 */
public class SimpleServletConfig implements ServletConfig {
    private ServletContext servletContext;

    @Override
    public String getServletName() {
        return servletContext.getServletContextName();
    }

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public String getInitParameter(String name) {
        return null;
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return Collections.enumeration(new HashSet<>());
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
