package Services;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lib.ObjectCreatorClass;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;



public class ClientHandler extends SimpleChannelInboundHandler<Object> {
    private String token;
    private String clientFolder = "CLIENT_FOLDER";

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
        messageProcessor(type, objectCreatorClass, ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

    }


    void messageProcessor(String type, ObjectCreatorClass o, ChannelHandlerContext ctx) {
        if (type.equals("auth_res")) {
            if (o.getMessage().equals("Authorization success")) {
                this.token = o.getToken();
                System.out.println("Authorization success, we have token = " + this.token);

                workingProcess(ctx);
            } else
                System.out.println("Authorization failure");
        }
        ;

    }


    public void sendObject(ObjectCreatorClass o, ChannelHandlerContext ctx) {
        System.out.println("Sending: " + o.getTypeOfMessage());

            ctx.channel().writeAndFlush(o);


    }


    public void workingProcess(ChannelHandlerContext ctx) {

        /*Передача фала небольшого файл. работает*/
        ObjectCreatorClass file = new ObjectCreatorClass("file", clientFolder, "txt.txt");
        sendObject(file,ctx);



/*!!!!!!!тут не пашет)-**/
        //обираем список фалов и хэш клиентской папки, если не задана. если задана то можно будет сохранить все настройки сервера в фаил и оттуда брать. пока по дефолту
        /*создаем струтуру каталогов клиента*/
      //  ObjectCreatorClass tree = new ObjectCreatorClass("tree", clientFolder, "");
      //  System.out.println(tree);
      //  System.out.println("Всего файлов: " + tree.getTotalFiles() + "  Размер локльной папки:" + tree.getClientFolderSize() + "  HASH: " + tree.getClientFolderHash());


        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("2tree.dat"));
            oos.writeObject(new ObjectCreatorClass("auth", "A", "A"));
        } catch (IOException e) {
            e.printStackTrace();
        }



      //  sendObject(new ObjectCreatorClass("tree", clientFolder, ""), ctx);
       // sendObject(tree,ctx);


    }


}

