package com.example.iso8583poc.server;

import com.example.iso8583poc.service.SocketDataRequestHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpInitializer extends ChannelInitializer<SocketChannel> {

  private final SocketDataRequestHandler socketDataRequestHandler;

  public HttpInitializer(SocketDataRequestHandler socketDataRequestHandler) {
    this.socketDataRequestHandler = socketDataRequestHandler;
  }


  @Override
  protected void initChannel(SocketChannel socketChannel) throws Exception {
    ChannelPipeline pipeline = socketChannel.pipeline();
    pipeline.addLast("httpServerCodec", new HttpServerCodec());
    pipeline.addLast("websocketHandler", new WebSocketHandler(socketDataRequestHandler));
  }
}