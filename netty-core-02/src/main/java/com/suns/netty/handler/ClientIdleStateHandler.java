package com.suns.netty.handler;

import com.suns.netty.message.PingMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class ClientIdleStateHandler extends ChannelInboundHandlerAdapter {
    AtomicBoolean SERVER_ERROR;
    public ClientIdleStateHandler(AtomicBoolean SERVER_ERROR) {
        this.SERVER_ERROR = SERVER_ERROR;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
        if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
            ctx.writeAndFlush(new PingMessage("client"));
        } else if (idleStateEvent.state() == IdleState.READER_IDLE) {
            log.debug("服务器端 已经8秒没有响应数据了。。。");
            log.debug("关闭channel");
            ctx.channel().close();
            SERVER_ERROR.set(true);
            /*
            close.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                     //进行重连
                }
            });
            close.addListener(promise->{

            });
            */
        }

    }
}
