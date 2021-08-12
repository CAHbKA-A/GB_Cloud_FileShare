package service;


import ObjectCreatorClassLib.ObjectCreatorClass;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ServerHandler extends SimpleChannelInboundHandler<Object> {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        ObjectCreatorClass objectCreatorClass = (ObjectCreatorClass) msg;
        String type = objectCreatorClass.getTypeOfMessage();
        System.out.println(type + " received");
        messageProcessor(type, objectCreatorClass, ctx);
        System.out.println("<<<<>>>>");


    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object o) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.channel().close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client disconnected");
    }


    void messageProcessor(String type, ObjectCreatorClass o, ChannelHandlerContext ctx) {
        if (type.equals("auth")) authentication(o.getMessage(), ctx);
        if (type.equals("tree")) {
            System.out.println("i have a tree: "/*+ o.toString()*/);
            /*сравниваем каталоги*/
            FolderSynchronizer folderSynchronizer = new FolderSynchronizer();
            folderSynchronizer.compareTree(o);
        }

        if (type.equals("file")) {
            System.out.println("i have a file:" + o.getFileName() + " .Size:" + o.getFileSize());
            saveAsFile(o);
        }

    }


    void authentication(String inputMessage, ChannelHandlerContext ctx) {

        String[] parts = inputMessage.split(" ");
        if (parts.length == 3) {
            String nick = (AuthenticationService.authenticationAlgorithm(parts[1], parts[2]));
            if (!nick.equals("")) {
                String token = "12";// todo генератор токенов с записю в бд
                ObjectCreatorClass o_aut = new ObjectCreatorClass("auth_res", token, "Authorization success");
                sendObject(o_aut, ctx);
                System.out.println(nick + " : authorization success");
            } else {
                sendObject(new ObjectCreatorClass("auth_res", "0", "/Wrong login or password"), ctx);
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


    private static void saveAsFile(ObjectCreatorClass filePrepare) {

        try {
            File fileА = new File(String.valueOf("SERVER_FOLDER/" + filePrepare.getFileName()));
            FileOutputStream fos = new FileOutputStream(fileА);
            fos.write(filePrepare.getFileBin());//записали
            //исправляем дату послднего измененя (ставим как у клиента)
            fileА.setLastModified(filePrepare.getLastModify());
            System.out.println(filePrepare.getLastModify());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}


