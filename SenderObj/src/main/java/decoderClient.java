import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class DecoderClient extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(new Cat( "mur"));
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

    }
}
