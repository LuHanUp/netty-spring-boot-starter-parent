package top.luhancc.netty.starter.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.luhancc.netty.starter.netty.core.DefaultLastFilter;
import top.luhancc.netty.starter.netty.core.NettyFilterConfig;
import top.luhancc.netty.starter.netty.core.NettyFilterConfigChain;
import top.luhancc.netty.starter.netty.core.NettyFilterRegistration;

import javax.servlet.*;
import javax.servlet.descriptor.JspConfigDescriptor;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author luHan
 * @create 2021/2/23 18:48
 * @since 1.0.0
 */
public class NettyServletContext implements ServletContext {
    private static final Logger log = LoggerFactory.getLogger(NettyServletContext.class);


    private final Map<String, String> parameters = new ConcurrentHashMap<>();
    protected Map<String, Object> attributes = new ConcurrentHashMap<>();
    protected Map<String, Filter> filters = new ConcurrentHashMap<>();

    private NettyFilterConfigChain filterConfigChain = new NettyFilterConfigChain();

    @Override
    public String getContextPath() {
        return "/";
    }

    @Override
    public ServletContext getContext(String s) {
        System.out.println("NettyServletContext.s:" + s);
        return null;
    }

    @Override
    public int getMajorVersion() {
        return 0;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public int getEffectiveMajorVersion() {
        return 0;
    }

    @Override
    public int getEffectiveMinorVersion() {
        return 0;
    }

    @Override
    public String getMimeType(String s) {
        return null;
    }

    @Override
    public Set<String> getResourcePaths(String s) {
        return null;
    }

    @Override
    public URL getResource(String s) throws MalformedURLException {
        return null;
    }

    @Override
    public InputStream getResourceAsStream(String s) {
        return null;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String s) {
        return null;
    }

    @Override
    public RequestDispatcher getNamedDispatcher(String s) {
        return null;
    }

    @Override
    public Servlet getServlet(String s) throws ServletException {
        return null;
    }

    @Override
    public Enumeration<Servlet> getServlets() {
        return null;
    }

    @Override
    public Enumeration<String> getServletNames() {
        return null;
    }

    @Override
    public void log(String s) {

    }

    @Override
    public void log(Exception e, String s) {

    }

    @Override
    public void log(String s, Throwable throwable) {

    }

    @Override
    public String getRealPath(String s) {
        return null;
    }

    @Override
    public String getServerInfo() {
        return null;
    }

    @Override
    public String getInitParameter(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        Set<String> names = new HashSet<>(parameters.keySet());
        return Collections.enumeration(names);
    }

    @Override
    public boolean setInitParameter(String s, String s1) {
        return false;
    }

    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        Set<String> names = new HashSet<>(attributes.keySet());
        return Collections.enumeration(names);
    }

    @Override
    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    @Override
    public void removeAttribute(String s) {

    }

    @Override
    public String getServletContextName() {
        return null;
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String s, String s1) {
        return null;
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String s, Servlet servlet) {
        return null;
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String s, Class<? extends Servlet> aClass) {
        return null;
    }

    @Override
    public ServletRegistration.Dynamic addJspFile(String s, String s1) {
        return null;
    }

    @Override
    public <T extends Servlet> T createServlet(Class<T> aClass) throws ServletException {
        return null;
    }

    @Override
    public ServletRegistration getServletRegistration(String s) {
        return null;
    }

    @Override
    public Map<String, ? extends ServletRegistration> getServletRegistrations() {
        return null;
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, String filterClass) {
        return addFilter(filterName, filterClass, null);
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, Filter filter) {
        return addFilter(filterName, filter.getClass().getName(), filter);
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, Class<? extends Filter> filterClass) {
        return addFilter(filterName, filterClass.getName(), null);
    }

    @Override
    public <T extends Filter> T createFilter(Class<T> aClass) throws ServletException {
        return null;
    }

    @Override
    public FilterRegistration getFilterRegistration(String s) {
        return null;
    }

    @Override
    public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
        return null;
    }

    @Override
    public SessionCookieConfig getSessionCookieConfig() {
        return null;
    }

    @Override
    public void setSessionTrackingModes(Set<SessionTrackingMode> set) {

    }

    @Override
    public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
        return null;
    }

    @Override
    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
        return null;
    }

    @Override
    public void addListener(String s) {

    }

    @Override
    public <T extends EventListener> void addListener(T t) {

    }

    @Override
    public void addListener(Class<? extends EventListener> aClass) {

    }

    @Override
    public <T extends EventListener> T createListener(Class<T> aClass) throws ServletException {
        return null;
    }

    @Override
    public JspConfigDescriptor getJspConfigDescriptor() {
        return null;
    }

    @Override
    public ClassLoader getClassLoader() {
        return null;
    }

    @Override
    public void declareRoles(String... strings) {

    }

    @Override
    public String getVirtualServerName() {
        return null;
    }

    @Override
    public int getSessionTimeout() {
        return 0;
    }

    @Override
    public void setSessionTimeout(int i) {

    }

    @Override
    public String getRequestCharacterEncoding() {
        return null;
    }

    @Override
    public void setRequestCharacterEncoding(String s) {

    }

    @Override
    public String getResponseCharacterEncoding() {
        return null;
    }

    @Override
    public void setResponseCharacterEncoding(String s) {

    }

    private Map<String, NettyFilterConfig> filterConfigs = new ConcurrentHashMap<>();

    private FilterRegistration.Dynamic addFilter(String filterName, String filterClass, Filter filter) {
        filters.put(filterName, filter);
        filterConfigs.put(filterName, new NettyFilterConfig(filter, filterName, this));
        return new NettyFilterRegistration();
    }

    /**
     * 初始化filter
     */
    public void filterStart() {
        for (Map.Entry<String, Filter> entry : filters.entrySet()) {
            NettyFilterConfig filterConfig = filterConfigs.get(entry.getKey());
            try {
                entry.getValue().init(filterConfig);

                filterConfigChain.addFirstFilterConfig(filterConfig);
            } catch (ServletException e) {
                log.error("init filter fail ", e);
            }
        }

        // 添加没有任何操作的空实现Filter
        DefaultLastFilter emptyFilter = new DefaultLastFilter();
        filters.put("netty_defaultLastFilter", emptyFilter);
        NettyFilterConfig emptyFilterConfig = new NettyFilterConfig(emptyFilter, "netty_defaultLastFilter", this);
        filterConfigs.put("netty_defaultLastFilter", emptyFilterConfig);
        filterConfigChain.addLastFilterConfig(emptyFilterConfig);
    }

    public NettyFilterConfigChain getFilterConfigChain() {
        return this.filterConfigChain;
    }
}
