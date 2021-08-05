import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class DecoderServ extends ChannelInboundHandlerAdapter{

@Override
public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Cat cat = (Cat) msg;
        System.out.println(cat+" received");

        }

@Override
public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.channel().close();
        }


}
