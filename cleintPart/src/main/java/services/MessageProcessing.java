package services;

/*обработака сообщений полученных от сервера*/
import FilePropertyLib.FileProperty;
import ObjectCreatorClassLib.ObjectCreatorClass;
import io.netty.channel.ChannelHandlerContext;

public class MessageProcessing {

    void messageProcessor(String type, ObjectCreatorClass o, ChannelHandlerContext ctx) {
        if (type.equals("auth_res")) {
            if (o.getMessage().equals("Authorization success")) {
              //  this.token = o.getToken();
                System.out.println("Authorization success, we have token = " + o.getToken());
                ClientHandler clientHandler = new ClientHandler();
                clientHandler.workingProcess(ctx);
            } else
                System.out.println("Authorization failure");
        }


        if (type.equals("giveMeFiles")) {
            System.out.println("We must send some files to server");
            //  System.out.println(o.getFileList());
            for (FileProperty o1 :o.getFileList() ) {
                //   System.out.println(o1.getPath());
                ObjectCreatorClass file = new ObjectCreatorClass("file", "", o1.getPath());
                ClientHandler clientHandler = new ClientHandler();
                clientHandler .sendObject(file,ctx);
            }
        }
    }
}
