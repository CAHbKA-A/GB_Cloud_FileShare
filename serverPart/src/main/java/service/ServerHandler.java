package service;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lib.ObjectCreatorClass;

public class ServerHandler extends SimpleChannelInboundHandler<Object> {



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        ObjectCreatorClass objectCreatorClass = (ObjectCreatorClass) msg;
        String type=objectCreatorClass.getTypeOfMessage();
        System.out.println(type+" received");
        messageProcessor(type,objectCreatorClass,ctx);



    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.channel().close();
    }



 void messageProcessor(String type,ObjectCreatorClass o,ChannelHandlerContext ctx){
        if (type.equals("auth")) authentication(o.getMessage(),ctx);

 }



    void authentication(String inputMessage, ChannelHandlerContext ctx) {

            String[] parts = inputMessage.split(" ");
            if (parts.length == 3) {
                String nick = (AuthenticationService.authenticationAlgorithm(parts[1], parts[2]));
                if (!nick.equals("")) {
                    String token="12";// todo генератор токенов с записю в бд
                    ObjectCreatorClass o_aut = new ObjectCreatorClass("auth_res",token, "Authorization success");
                   sendObject(o_aut,ctx);
                     System.out.println(nick + " : authorization success");
                } else {
                    sendObject(new ObjectCreatorClass("auth_res","0", "/Wrong login or password"),ctx);
                   // ctx.writeAndFlush("/Wrong login or password.");
                    System.out.println(" Wrong login or password.");

            }
        }
    }



    public void sendObject(ObjectCreatorClass o, ChannelHandlerContext ctx) {
        System.out.println("Sending: " + o.getTypeOfMessage());
        try {
            ctx.channel().writeAndFlush(o).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }










//    private static void saveAsFile(FileObjectLibClass filePrepare) throws FileNotFoundException {
//        FileOutputStream fos = new FileOutputStream("SERVER_FOLDER_" + filePrepare.getFileName());
//        try {
//
//            fos.write(filePrepare.getFileBin());
//            System.out.println(filePrepare.getFileBin());
//            fos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
}
