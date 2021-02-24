package top.luhancc.netty.starter.servlet;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.*;

/**
 * @author luHan
 * @create 2021/2/24 09:44
 * @since 1.0.0
 */
public class NettyServletConfig implements ServletConfig {

    private final ServletContext servletContext;

    private final String servletName;

    private final Map<String, String> initParameters = new LinkedHashMap<>();


    /**
     * Create a new NettyServletConfig with a default {@link NettyServletContext}.
     */
    public NettyServletConfig() {
        this(null, "");
    }

    /**
     * Create a new NettyServletConfig with a default {@link NettyServletContext}.
     *
     * @param servletName the name of the servlet
     */
    public NettyServletConfig(String servletName) {
        this(null, servletName);
    }

    /**
     * Create a new NettyServletConfig.
     *
     * @param servletContext the ServletContext that the servlet runs in
     */
    public NettyServletConfig(@Nullable ServletContext servletContext) {
        this(servletContext, "");
    }

    /**
     * Create a new NettyServletConfig.
     *
     * @param servletContext the ServletContext that the servlet runs in
     * @param servletName    the name of the servlet
     */
    public NettyServletConfig(@Nullable ServletContext servletContext, String servletName) {
        this.servletContext = (servletContext != null ? servletContext : new NettyServletContext());
        this.servletName = servletName;
    }


    @Override
    public String getServletName() {
        return this.servletName;
    }

    @Override
    public ServletContext getServletContext() {
        return this.servletContext;
    }

    public void addInitParameter(String name, String value) {
        Assert.notNull(name, "Parameter name must not be null");
        this.initParameters.put(name, value);
    }

    @Override
    public String getInitParameter(String name) {
        Assert.notNull(name, "Parameter name must not be null");
        return this.initParameters.get(name);
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return Collections.enumeration(this.initParameters.keySet());
    }

}

