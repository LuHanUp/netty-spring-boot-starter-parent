package top.luhancc.netty.starter.netty.core;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import java.util.Enumeration;

/**
 * @author luHan
 * @create 2021/2/24 16:13
 * @since 1.0.0
 */
public class NettyFilterConfig implements FilterConfig {
    private Filter filter;
    private String filterName;
    private ServletContext servletContext;

    public NettyFilterConfig(Filter filter, String filterName, ServletContext servletContext) {
        this.filter = filter;
        this.filterName = filterName;
        this.servletContext = servletContext;
    }

    @Override
    public String getFilterName() {
        return filterName;
    }

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public String getInitParameter(String name) {
        return servletContext.getInitParameter(name);
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return servletContext.getInitParameterNames();
    }

    public Filter getFilter() {
        return this.filter;
    }
}
