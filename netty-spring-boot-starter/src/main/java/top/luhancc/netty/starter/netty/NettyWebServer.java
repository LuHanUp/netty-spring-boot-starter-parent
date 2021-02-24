package top.luhancc.netty.starter.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.server.WebServerException;

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

    private int port;

    public NettyWebServer(int port, ServerBootstrap serverBootstrap, EventLoopGroup boosGroup, EventLoopGroup workerGroup) {
        this.serverBootstrap = serverBootstrap;
        this.boosGroup = boosGroup;
        this.workerGroup = workerGroup;
        this.port = port;

        initialize();
    }

    private void initialize() {
        // 绑定端口并启动
        ChannelFuture channelFuture = null;
        try {
            channelFuture = serverBootstrap.bind(port).sync();
            System.out.println("服务器准备好了...");
        } catch (InterruptedException e) {
            log.error("starting netty fail:", e);
        }
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
