package top.luhancc.netty.starter.factory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.AbstractServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import top.luhancc.netty.starter.handler.HttpChannelInitializer;
import top.luhancc.netty.starter.netty.NettyWebServer;
import top.luhancc.netty.starter.servlet.NettyServletContext;
import top.luhancc.netty.starter.servlet.NettyServletConfig;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * @author luHan
 * @create 2021/2/23 16:05
 * @since 1.0.0
 */
public class NettyWebServerFactory extends AbstractServletWebServerFactory
        implements ResourceLoaderAware, ServletWebServerFactory, ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(NettyWebServerFactory.class);

    private ResourceLoader resourceLoader;
    private ServletContext servletContext;
    private ApplicationContext applicationContext;

    @Override
    public WebServer getWebServer(ServletContextInitializer... initializers) {
        Object dispatcherServlet = applicationContext.getBean("dispatcherServlet");

        if (dispatcherServlet == null) {
            throw new RuntimeException("dispatcherServlet 为空");
        }
        ServletContext servletContext = getServletContext();
        HttpServlet servlet = (HttpServlet) dispatcherServlet;
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boosGroup, workerGroup)
                // 设置NioServerSocketChannel作为服务器的通道实现
                .channel(NioServerSocketChannel.class)
                // 设置PipeLine的处理器，就是给workerGroup的EventLoop对应的PipeLine设置处理器
                .childHandler(new HttpChannelInitializer(servlet, servletContext));

        ServletContextInitializer[] initializersToUse = mergeInitializers(initializers);
        try {
            for (ServletContextInitializer servletContextInitializer : initializersToUse) {
                servletContextInitializer.onStartup(servletContext);
            }
            NettyServletConfig servletConfig = new NettyServletConfig(servletContext);
            servlet.init(servletConfig);

            return new NettyWebServer(getPort(), serverBootstrap, boosGroup, workerGroup);
        } catch (ServletException e) {
            log.error("context init fail:", e);
            return null;
        }
    }

    private ServletContext getServletContext() {
        if (servletContext == null) {
            servletContext = new NettyServletContext();
        }
        return servletContext;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
