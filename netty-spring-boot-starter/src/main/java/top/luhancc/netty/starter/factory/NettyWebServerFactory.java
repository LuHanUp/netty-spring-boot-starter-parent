package top.luhancc.netty.starter.factory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.AbstractServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.web.servlet.DispatcherServlet;
import top.luhancc.netty.starter.handler.HttpChannelInitializer;
import top.luhancc.netty.starter.netty.NettyWebServer;
import top.luhancc.netty.starter.servlet.NettyServletConfig;
import top.luhancc.netty.starter.servlet.NettyServletContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * @author luHan
 * @create 2021/2/23 16:05
 * @since 1.0.0
 */
public class NettyWebServerFactory extends AbstractServletWebServerFactory
        implements ServletWebServerFactory, BeanFactoryPostProcessor {
    private static final Logger log = LoggerFactory.getLogger(NettyWebServerFactory.class);

    private ServletContext servletContext;
    private DispatcherServlet dispatcherServlet;

    @Override
    public WebServer getWebServer(ServletContextInitializer... initializers) {
        NettyServletContext servletContext = (NettyServletContext) getServletContext();
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boosGroup, workerGroup)
                // 设置NioServerSocketChannel作为服务器的通道实现
                .channel(NioServerSocketChannel.class)
                // 设置PipeLine的处理器，就是给workerGroup的EventLoop对应的PipeLine设置处理器
                .childHandler(new HttpChannelInitializer(dispatcherServlet, servletContext));

        ServletContextInitializer[] initializersToUse = mergeInitializers(initializers);
        try {
            for (ServletContextInitializer servletContextInitializer : initializersToUse) {
                servletContextInitializer.onStartup(servletContext);
            }
            NettyServletConfig servletConfig = new NettyServletConfig(servletContext);
            dispatcherServlet.init(servletConfig);

            return new NettyWebServer(getPort(), serverBootstrap, boosGroup, workerGroup, servletContext);
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
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.dispatcherServlet = beanFactory.getBean(DispatcherServlet.class);
    }
}
