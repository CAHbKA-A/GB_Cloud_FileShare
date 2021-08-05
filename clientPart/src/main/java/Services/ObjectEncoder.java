package Services;

import io.netty.channel.ChannelHandlerContext;

import io.netty.channel.SimpleChannelInboundHandler;


public class ObjectEncoder extends SimpleChannelInboundHandler<Object> {



        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            FileTreeCreator tree = new FileTreeCreator("CLIENT_FOLDER");
            ctx.writeAndFlush(tree);
        }


        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

    }
}

