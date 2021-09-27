package service;


import ObjectCreatorClassLib.ObjectCreatorClass;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

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
        if (type.equals("auth")) AuthenticationService.authentication(o.getMessage(), ctx);
        if (type.equals("tree")) {
            // System.out.println("i have a tree: "/*+ o.toString()*/);
            /*сравниваем каталоги*/
            FolderSynchronizer folderSynchronizer = new FolderSynchronizer();
            ObjectCreatorClass o1 = folderSynchronizer.compareTree(o, this,ctx);
            if (o1 != null) {
                sendObject(o1, ctx);
            } else {
                ObjectCreatorClass foldersSame = new ObjectCreatorClass("foldersAreSame", null, null);
                sendObject(foldersSame, ctx);

            }
        }

        if (type.equals("file")) {
            System.out.println("i have a file:" + o.getFileName() + " .Size:" + o.getFileSize());
            FileProcessing.saveAsFile(o);
        }
        if (type.equals("BigFileMessage")) {
            if (o.getMessage().equals("Start")) {
                System.out.println("Big FIle " + o.getFileName() + " will sending!!");
            }
            if (o.getMessage().equals("End")) {
                System.out.println("All "+o.getTotalFiles()+" parts of Big FIle " + o.getFileName() + " relieved! lets merge.");
                FileProcessing.saveAsBigFile(o.getFileName() ,o.getTotalFiles(), o.getLastModify());
            }

            if (o.getMessage().equals("part")) {
                System.out.println("Receiving  part");
            }
        }


    }


    public void sendObject(ObjectCreatorClass o, ChannelHandlerContext ctx) {
        if (o != null) {
            System.out.println("Sending: " + o.getTypeOfMessage());
            try {
                ctx.channel().writeAndFlush(o).sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}


