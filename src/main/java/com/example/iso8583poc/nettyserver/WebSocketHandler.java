package com.example.iso8583poc.nettyserver;

import com.example.iso8583poc.service.SocketDataRequestHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebSocketHandler extends ChannelInboundHandlerAdapter {

    private final SocketDataRequestHandler socketDataRequestHandler;
    WebSocketServerHandshaker handshaker;

    public WebSocketHandler(SocketDataRequestHandler socketDataRequestHandler) {
        this.socketDataRequestHandler = socketDataRequestHandler;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest httpRequest) {

            log.debug("Http Request Received");

            HttpHeaders headers = httpRequest.headers();
            log.debug("Connection : " + headers.get("Connection"));
            log.debug("Upgrade : " + headers.get("Upgrade"));

            if ("Upgrade".equalsIgnoreCase(headers.get(HttpHeaderNames.CONNECTION))
                && "WebSocket".equalsIgnoreCase(headers.get(HttpHeaderNames.UPGRADE))) {
                log.debug("WebSocketHandler added to the pipeline");
                log.debug("Opened Channel : " + ctx.channel());
                log.debug("Handshaking....");
                // Do the Handshake to upgrade connection from HTTP to WebSocket protocol
                handleHandshake(ctx, httpRequest);
                log.debug("Handshake is done");
            }
        } else {
            log.info("Incoming request is not HttpRequest");
        }
        if (msg instanceof WebSocketFrame) {
            log.debug("This is a WebSocket frame");
            log.debug("Client Channel : " + ctx.channel());
            /**
             * There are different types of websocketframe to read different types of data like byte,
             * text. For this time being we only need text to read json
             */
            if (msg instanceof TextWebSocketFrame) {
                log.debug("TextWebSocketFrame Received : {}", ((TextWebSocketFrame) msg).text());

                var response = socketDataRequestHandler.handleRequest(((TextWebSocketFrame) msg).text());
                ctx.channel().writeAndFlush(new TextWebSocketFrame(response));
            } else {
                System.out.println("Unsupported WebSocketFrame");
            }
        }
    }

    /* Do the handshaking for WebSocket request */
    protected void handleHandshake(ChannelHandlerContext ctx, HttpRequest req) {
        WebSocketServerHandshakerFactory wsFactory =
            new WebSocketServerHandshakerFactory(getWebSocketURL(req), null, true);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
    }

    protected String getWebSocketURL(HttpRequest req) {
        log.debug("Req URI : " + req.uri());
        String url = "ws://" + req.headers().get("Host") + req.uri();
        log.debug("Constructed URL : " + url);
        return url;
    }
}