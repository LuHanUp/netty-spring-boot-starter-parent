package top.luhancc.netty.starter.config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.luhancc.netty.starter.factory.NettyWebServerFactory;

/**
 * @author luHan
 * @create 2021/2/23 16:02
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
public class NettyWebServerFactoryConfiguration {

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({NioServerSocketChannel.class, EventLoopGroup.class, ServerBootstrap.class})
    public static class EmbeddedNetty {
        @Bean
        NettyWebServerFactory nettyWebServerFactory() {
            NettyWebServerFactory factory = new NettyWebServerFactory();
            return factory;
        }
    }
}
