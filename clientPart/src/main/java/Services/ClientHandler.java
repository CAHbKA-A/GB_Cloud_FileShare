package Services;

import io.netty.channel.ChannelHandlerContext;

import io.netty.channel.SimpleChannelInboundHandler;
import lib.ObjectCreatorClass;


public class ClientHandler extends SimpleChannelInboundHandler<Object> {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
       // ctx.writeAndFlush(new Cat( "mur"));
        ctx.writeAndFlush(new ObjectCreatorClass( "auth"));

    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

    }

}

