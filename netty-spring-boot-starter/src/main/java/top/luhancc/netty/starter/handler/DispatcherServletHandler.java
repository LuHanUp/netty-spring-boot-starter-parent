package top.luhancc.netty.starter.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import org.springframework.http.HttpMethod;
import top.luhancc.netty.starter.servlet.NettyHttpServletRequest;
import top.luhancc.netty.starter.servlet.NettyHttpServletResponse;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Enumeration;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * 将HttpServletRequest请求处理交给DispatcherServlet处理
 *
 * @author luHan
 * @create 2021/2/24 11:11
 * @since 1.0.0
 */
public class DispatcherServletHandler extends SimpleChannelInboundHandler<HttpServletRequest> {
    private HttpServlet dispatcherServlet;

    public DispatcherServletHandler(HttpServlet servlet) {
        this.dispatcherServlet = servlet;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpServletRequest servletRequest) throws Exception {
        NettyHttpServletResponse servletResponse = new NettyHttpServletResponse();
        servletResponse.setCharacterEncoding(CharsetUtil.UTF_8.displayName());
        dispatcherServlet.service(servletRequest, servletResponse);

        if (HttpResponseStatus.OK.code() != servletResponse.getStatus()) {
            // 重新发送一个/error请求
            HttpServletRequest errorServletRequest = createErrorServletRequest(servletRequest, servletResponse);
            servletResponse = new NettyHttpServletResponse();
            dispatcherServlet.service(errorServletRequest, servletResponse);
        }

        HttpResponseStatus status = HttpResponseStatus.valueOf(servletResponse.getStatus());
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status,
                Unpooled.copiedBuffer(servletResponse.getContentAsByteArray()));

        for (String name : servletResponse.getHeaderNames()) {
            response.headers().add(name, servletResponse.getHeader(name));
        }
        addDefaultHeaders(response);

        // Write the initial line and the header and data.
        ChannelFuture writeFuture = ctx.writeAndFlush(response);
        writeFuture.addListener(ChannelFutureListener.CLOSE);
    }

    private void addDefaultHeaders(FullHttpResponse response) {
        response.headers().add(HttpHeaderNames.DATE, new Date());
        response.headers().add(HttpHeaderNames.CONNECTION, "keep-alive");
        if (!response.headers().contains(HttpHeaderNames.CONTENT_LENGTH)) {
            response.headers().add(HttpHeaderNames.TRANSFER_ENCODING, "chunked");
        }
    }

    private HttpServletRequest createErrorServletRequest(HttpServletRequest httpServletRequest, HttpServletResponse servletResponse) {
        NettyHttpServletRequest servletRequest = new NettyHttpServletRequest();
        servletRequest.setRequestURI("/error");
        servletRequest.setPathInfo(null);
        servletRequest.setServletPath("/error");
        servletRequest.setMethod(HttpMethod.GET.name());

        if (httpServletRequest.getScheme() != null) {
            servletRequest.setScheme(httpServletRequest.getScheme());
        }
        if (httpServletRequest.getRemoteHost() != null) {
            servletRequest.setServerName(httpServletRequest.getRemoteHost());
        }
        if (httpServletRequest.getLocalPort() != -1) {
            servletRequest.setServerPort(httpServletRequest.getLocalPort());
        }
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            servletRequest.addHeader(name, httpServletRequest.getHeader(name));
        }
        servletRequest.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, servletResponse.getStatus());
        servletRequest.setAttribute(RequestDispatcher.ERROR_REQUEST_URI, httpServletRequest.getRequestURI());
        return servletRequest;
    }
}
