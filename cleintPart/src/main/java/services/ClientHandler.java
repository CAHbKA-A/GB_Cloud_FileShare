package services;

import ObjectCreatorClassLib.ObjectCreatorClass;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


public class ClientHandler extends SimpleChannelInboundHandler<Object> {
    private String token;
    private String clientFolder = "CLIENT_FOLDER";
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
        messageProcessing.messageProcessor(type, objectCreatorClass, ctx);
        //  messageProcessor(type, objectCreatorClass, ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

    }


    public void sendObject(ObjectCreatorClass o, ChannelHandlerContext ctx) {
        System.out.println("Sending: " + o.getTypeOfMessage());
        ctx.channel().writeAndFlush(o);
    }


    public void workingProcess(ChannelHandlerContext ctx) {

        //обираем список фалов и хэш клиентской папки, если не задана. если задана то можно будет сохранить все настройки сервера в фаил и оттуда брать. пока по дефолту
        /*создаем струтуру каталогов клиента*/

        ObjectCreatorClass tree = new ObjectCreatorClass("tree", clientFolder, "");
        sendObject(tree, ctx);

        //мониторим папки
        //Thread fileWatcherThread  =;
    }

}

