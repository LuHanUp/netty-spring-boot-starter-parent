package top.luhancc.netty.starter.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

/**
 * @author luHan
 * @create 2021/2/23 16:21
 * @since 1.0.0
 */
public class HttpChannelInitializer extends ChannelInitializer<SocketChannel> {
    private HttpServlet servlet;
    private ServletContext servletContext;

    public HttpChannelInitializer(HttpServlet servlet, ServletContext servletContext) {
        this.servlet = servlet;
        this.servletContext = servletContext;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // HttpServerCodec是Netty提供的处理http的编解码器
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("httpServerCodec", new HttpServerCodec());
        pipeline.addLast("httpObjectAggregator", new HttpObjectAggregator(65536));
        pipeline.addLast("generateServletRequestHandler", new GenerateServletRequestHandler());
//        pipeline.addLast("MyHttpServerHandler", new ServletNettyHandler(servlet, servletContext));
        pipeline.addLast("MyHttpServerHandler", new DispatcherServletHandler(servlet));
    }
}
