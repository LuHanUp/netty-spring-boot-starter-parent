package top.luhancc.netty.starter.netty.core;

import javax.servlet.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author luHan
 * @create 2021/2/24 16:36
 * @since 1.0.0
 */
public class NettyFilterConfigChain implements FilterChain {
    private List<NettyFilterConfig> filterConfigs = new ArrayList<>();
    private int pos = 0;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        if (pos < filterConfigs.size()) {
            NettyFilterConfig filterConfig = filterConfigs.get(pos++);
            Filter filter = filterConfig.getFilter();
            filter.doFilter(request, response, this);
        }
    }

    /**
     * filter是否都执行过了
     *
     * @return
     */
    public boolean isFilterAllExecute() {
        return pos == filterConfigs.size();
    }

    public void reset() {
        this.pos = 0;
    }

    public void addFirstFilterConfig(NettyFilterConfig filterConfig) {
        filterConfigs.add(0, filterConfig);
    }

    public void addLastFilterConfig(NettyFilterConfig emptyFilterConfig) {
        filterConfigs.add(filterConfigs.size(), emptyFilterConfig);
    }
}
