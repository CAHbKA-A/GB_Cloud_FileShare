package services;

import ObjectCreatorClassLib.ObjectCreatorClass;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;



public class ClientHandler extends SimpleChannelInboundHandler<Object> {
    private String token;
    private String clientFolder = "CLIENT_FOLDER";
    private boolean handlerIsBusy;
    MessageProcessing messageProcessing = new MessageProcessing();

    public String getToken() {
        return token;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //авторизуемся
        sendObject(new ObjectCreatorClass("auth", "A", "A"), ctx);

    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ObjectCreatorClass objectCreatorClass = (ObjectCreatorClass) msg;
        String type = objectCreatorClass.getTypeOfMessage();
        System.out.println(type + " received");
        messageProcessing.messageProcessor(type, objectCreatorClass, ctx,this);
        System.out.println("<<<<>>>>");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

    }


    public void sendObject(ObjectCreatorClass o, ChannelHandlerContext ctx) {
        System.out.println("Sending: " + o.getTypeOfMessage());
        ctx.channel().writeAndFlush(o);
    }

}

