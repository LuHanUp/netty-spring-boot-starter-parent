package top.luhancc.netty.starter.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import top.luhancc.netty.starter.netty.core.NettyFilterConfigChain;
import top.luhancc.netty.starter.servlet.NettyHttpServletResponse;
import top.luhancc.netty.starter.servlet.NettyServletContext;

import javax.servlet.http.HttpServletRequest;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * filter过滤器执行处理器
 *
 * @author luHan
 * @create 2021/2/24 16:25
 * @since 1.0.0
 */
public class NettyFilterHandler extends SimpleChannelInboundHandler<HttpServletRequest> {
    private NettyServletContext servletContext;

    public NettyFilterHandler(NettyServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpServletRequest servletRequest) throws Exception {
        NettyHttpServletResponse servletResponse = new NettyHttpServletResponse();
        NettyFilterConfigChain filterConfigChain = servletContext.getFilterConfigChain();
        filterConfigChain.doFilter(servletRequest, servletResponse);

        if (filterConfigChain.isFilterAllExecute()) {
            ctx.fireChannelRead(servletRequest);
        } else {
            sendEmptyData(ctx, HttpResponseStatus.OK);
        }

        filterConfigChain.reset();
    }

    private static void sendEmptyData(ChannelHandlerContext ctx, HttpResponseStatus status) {
        ByteBuf content = Unpooled.copiedBuffer(new byte[]{});
        FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HTTP_1_1, status, content);
        fullHttpResponse.headers().add("Access-Control-Allow-Origin", "*");
        fullHttpResponse.headers().add("Access-Control-Allow-Methods", "*");
        fullHttpResponse.headers().add("Access-Control-Max-Age", "100");
        fullHttpResponse.headers().add("Access-Control-Allow-Headers", "Content-Type");
        fullHttpResponse.headers().add("Access-Control-Allow-Credentials", "false");
        fullHttpResponse.headers().add(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");

        // Close the connection as soon as the error message is sent.
        ctx.writeAndFlush(fullHttpResponse).addListener(ChannelFutureListener.CLOSE);
    }
}
