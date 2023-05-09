package com.suns;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

//MyHandler1---MyHandler2
public class MyHandler1 extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //保证把这个事件 传播给下一个handler
        //反之 不写这个代码 不传播
        //  1. ctx.fireChannelActive();
        //  2. super.channelActive(ctx);
    }
}
