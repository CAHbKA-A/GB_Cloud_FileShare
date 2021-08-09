package service;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lib.ObjectCreatorClass;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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
            compareTree(o);
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
            FileOutputStream fos = new FileOutputStream("SERVER_FOLDER/" + filePrepare.getFileName());
            fos.write(filePrepare.getFileBin());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void compareTree(ObjectCreatorClass remoteTree) {
 // todo как то продумать что делать, если клиент зашел с разных ПК. если ервый раз, то все выкачиваем клиенту, есои не первый раз, то пока не ясно что считать актуальным каталогом
        /*сканируем папки и файлы на сервере*/
        String clientFolder ="CLIENT_FOLDER";
        ObjectCreatorClass localTree = new ObjectCreatorClass("tree", "SERVER_FOLDER/"+clientFolder, "");
      //  System.out.println("Remote folder Size = "+ remoteTree.getClientFolderSize()+"   Local Folder Size = "+localTree.getClientFolderSize());
      // System.out.println("Remote folder Hash = "+ remoteTree.getClientFolderHash()+"   Local Folder Size = "+localTree.getClientFolderHash());

        if (remoteTree == localTree) {
            System.out.println("Folders same");
            return;
        }
        System.out.println("folders are different/ let's synchronize");

        /*срвниваем струтуру каталогов*/
        List<String> remoteDirectoryList = remoteTree.getDirectoryList();
        for (String s : remoteDirectoryList) {
            System.out.println(s);
            Path path = Paths.get("SERVER_FOLDER/"+s);
            if (!Files.exists(path)) {
                try {
                    Files.createDirectory(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }


    }
}
