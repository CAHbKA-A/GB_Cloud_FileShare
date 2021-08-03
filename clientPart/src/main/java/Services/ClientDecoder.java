package Services;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;

public class ClientDecoder extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
  //      System.out.println(msg);
//

        if (msg.startsWith("/authorization success")){
            System.out.println("authorization complit");
        }

        if (msg.startsWith("/Lets_go!")){
            System.out.println("File  -- > Server");
            ctx.pipeline().remove(this);
            ctx.pipeline().addLast(new ObjectEncoder(), new ObjectDecoder(ClassResolvers.cacheDisabled(null)));

            /*туплю нем огу поянять как передать*/

            /*после передачи вертаем пайплафны на место*/

            ctx.writeAndFlush("done!");
        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("New active channel");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client disconnected");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
