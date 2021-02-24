package top.luhancc.netty.starter.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;
import top.luhancc.netty.starter.servlet.NettyHttpServletRequest;

import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * 将Netty的HttpRequest转换为HttpServletRequest
 *
 * @author luHan
 * @create 2021/2/24 11:05
 * @since 1.0.0
 */
public class GenerateServletRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final static Logger log = LoggerFactory.getLogger(GenerateServletRequestHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) throws Exception {
        if (!fullHttpRequest.decoderResult().isSuccess()) {
            sendError(ctx, BAD_REQUEST);
            return;
        }
        NettyHttpServletRequest httpServletRequest = createServletRequest(fullHttpRequest);
        String param = fullHttpRequest.content().toString(CharsetUtil.UTF_8);
        log.info("request:{},param:{}", fullHttpRequest.uri(), param);

        ctx.fireChannelRead(httpServletRequest);
    }

    private NettyHttpServletRequest createServletRequest(FullHttpRequest fullHttpRequest) {
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(fullHttpRequest.uri()).build();

        NettyHttpServletRequest servletRequest = new NettyHttpServletRequest();
        servletRequest.setRequestURI(uriComponents.getPath());
        servletRequest.setPathInfo(uriComponents.getPath());
        servletRequest.setMethod(fullHttpRequest.method().name());

        if (uriComponents.getScheme() != null) {
            servletRequest.setScheme(uriComponents.getScheme());
        }
        if (uriComponents.getHost() != null) {
            servletRequest.setServerName(uriComponents.getHost());
        }
        if (uriComponents.getPort() != -1) {
            servletRequest.setServerPort(uriComponents.getPort());
        }

        for (String name : fullHttpRequest.headers().names()) {
            servletRequest.addHeader(name, fullHttpRequest.headers().get(name));
        }


        ByteBuf bbContent = fullHttpRequest.content();
        String s = bbContent.toString(CharsetUtil.UTF_8);
        servletRequest.setContent(s.getBytes(CharsetUtil.UTF_8));


        if (uriComponents.getQuery() != null) {
            String query = UriUtils.decode(uriComponents.getQuery(), "UTF-8");
            servletRequest.setQueryString(query);
        }

        for (Map.Entry<String, List<String>> entry : uriComponents.getQueryParams().entrySet()) {
            for (String value : entry.getValue()) {
                servletRequest.addParameter(
                        UriUtils.decode(entry.getKey(), "UTF-8"),
                        UriUtils.decode(value, "UTF-8"));
            }
        }

        return servletRequest;
    }

    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        ByteBuf content = Unpooled.copiedBuffer(
                "Failure: " + status.toString() + "\r\n",
                CharsetUtil.UTF_8);

        FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(
                HTTP_1_1,
                status,
                content
        );
        fullHttpResponse.headers().add("Access-Control-Allow-Origin", "*");
        fullHttpResponse.headers().add("Access-Control-Allow-Methods", "*");
        fullHttpResponse.headers().add("Access-Control-Max-Age", "100");
        fullHttpResponse.headers().add("Access-Control-Allow-Headers", "Content-Type");
        fullHttpResponse.headers().add("Access-Control-Allow-Credentials", "false");
        fullHttpResponse.headers().add(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");

        // Close the connection as soon as the error message is sent.
        ctx.writeAndFlush(fullHttpResponse).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getLocalizedMessage());
        if (ctx.channel().isActive()) {
            sendError(ctx, INTERNAL_SERVER_ERROR);
        }
    }
}
