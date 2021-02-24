package top.luhancc.netty.starter.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.server.WebServerException;
import top.luhancc.netty.starter.servlet.NettyServletContext;

import javax.servlet.ServletContext;
import java.net.InetSocketAddress;

/**
 * @author luHan
 * @create 2021/2/23 16:12
 * @since 1.0.0
 */
public class NettyWebServer implements WebServer {
    private static final Logger log = LoggerFactory.getLogger(NettyWebServer.class);

    private ServerBootstrap serverBootstrap;
    private EventLoopGroup boosGroup;
    private EventLoopGroup workerGroup;

    private volatile boolean started;

    private NettyServletContext servletContext;

    private int port;

    public NettyWebServer(int port, ServerBootstrap serverBootstrap, EventLoopGroup boosGroup, EventLoopGroup workerGroup, NettyServletContext servletContext) {
        this.serverBootstrap = serverBootstrap;
        this.boosGroup = boosGroup;
        this.workerGroup = workerGroup;
        this.port = port;
        this.servletContext = servletContext;

        initialize();
    }

    private void initialize() {
        // filter start
        startFilter();

        // 绑定端口并启动
        ChannelFuture channelFuture = null;
        try {
            channelFuture = serverBootstrap.bind(new InetSocketAddress(port)).sync();
            log.info("Netty started on port(s): {} (http) with context path ''", port);
        } catch (InterruptedException e) {
            log.error("starting netty fail:", e);
        }
    }

    private void startFilter() {
        servletContext.filterStart();
    }

    @Override
    public void start() throws WebServerException {
        if (this.started) {
            return;
        }
        this.started = true;
    }

    @Override
    public void stop() throws WebServerException {
        boosGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    @Override
    public int getPort() {
        return this.port;
    }
}
