package top.luhancc.netty.starter.auto;

import io.netty.handler.codec.http.HttpRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import top.luhancc.netty.starter.config.NettyWebServerFactoryConfiguration;

/**
 * @author luHan
 * @create 2021/2/23 15:58
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass(HttpRequest.class)
@EnableConfigurationProperties(ServerProperties.class)
@Import({NettyWebServerFactoryConfiguration.EmbeddedNetty.class})
@Order(value = 0)
public class NettyWebServerFactoryAutoConfiguration {
}
