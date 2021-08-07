package Services;

import io.netty.channel.ChannelHandlerContext;

import io.netty.channel.SimpleChannelInboundHandler;
import lib.ObjectCreatorClass;


public class ClientHandler extends SimpleChannelInboundHandler<Object> {
private String token;

    public String getToken() {
        return token;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

     //   ctx.writeAndFlush(new ObjectCreatorClass( "auth","A","A"));
        sendObject(new ObjectCreatorClass( "auth","A","A"),ctx);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ObjectCreatorClass objectCreatorClass = (ObjectCreatorClass) msg;
        String type=objectCreatorClass.getTypeOfMessage();
        System.out.println(type+" received");
        messageProcessor(type,objectCreatorClass,ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

    }



    void messageProcessor(String type,ObjectCreatorClass o,ChannelHandlerContext ctx){
        if (type.equals("auth_res")){
            if (o.getMessage().equals("Authorization success"))
            {
                this.token = o.getToken();
                System.out.println("Authorization success, we have token = "+this.token);
            }
            else
                System.out.println("Authorization failure");
        };

    }



    public void sendObject(ObjectCreatorClass o, ChannelHandlerContext ctx) {
        System.out.println("Sending: " + o.getTypeOfMessage());
        try {
            ctx.channel().writeAndFlush(o).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}

