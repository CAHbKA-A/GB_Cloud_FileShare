import Services.ClientDecoder;
import Services.FileTreeCreator;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class ClientConsole {
    private Socket socket;
    private final int PORT = 8899;
    private final String HOST = "localhost";
    private ChannelFuture channelFuture;
    private String clientFolder;

    public static void main(String[] args) {
        ClientConsole clientConsole = new ClientConsole();
    }

    private ClientConsole() {

        //TODO  GUIClient(); //TODO ТУТ будет GUI авторизации + выбор папки*

        // соединились.
        connection();
        //авторизация
        singIn("B", "B");
        clientFolder = "CLIENT_FOLDER";
        //обираем список фалов и хэш клиентской папки, если не задана. если задана то можно будет сохранить все настройки сервера в фаил и оттуда брать. пока по дефолту

        FolderTreeObject(clientFolder);


        //TODO и отправка на сервер. сначала хэша и размера дирректории, если изменилось, то переача списка фалов

        //TODO тут будет запущена синхра

        //без паузы сообщения сливаются в одно.  разобраться, устранить. можетдобавить символ конца отправляемх байтов
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*передаем фаил*/
        sendMessage("/ready2transferFile");
//тут пока затык
        // sendMessage();

    }

    private void FolderTreeObject(String clientFolder) {
        /*сканируем клиентскую папку*/
        FileTreeCreator ftc = new FileTreeCreator(clientFolder);
        ftc.walkingTree();
         System.out.println("Всего файлов: "+ ftc.getTotalFiles()+"  Размер локльной папки:"+ ftc.getClientFolderSize() + "  HASH: "+ftc.getClientFolderHash());

       //  System.out.println(ftc.getFileList());
      //  System.out.println(ftc.getDirectoryList());
        /*сериализуем для передачи на сервер в виде файла(обьекта)*/
    }


    private void singIn(String login, String passwodr) {
        //TODO шифруем пароль.
        sendMessage("/auth " + login + " " + passwodr);

    }


    public void connection() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap client = new Bootstrap();
            client.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new StringEncoder(), new StringDecoder(), new ClientDecoder()/*, new ObjectEncoder(), new ObjectDecoder(ClassResolvers.cacheDisabled(null))*/);

                        }
                    });

            channelFuture = client.connect("localhost", PORT).sync();

            System.out.println("Client started");
            //     channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        finally {
//            group.shutdownGracefully();
//        }
    }


    public void sendMessage(String message) {
        System.out.println("Sending: " + message);
        try {
            channelFuture.channel().writeAndFlush(message).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void disconnet(NioEventLoopGroup group) {
        group.shutdownGracefully();
    }


//    public static void sendFileAsObject(String fileName) {
//
//        try {
//
//            FileObjectLibClass filePrepare = new FileObjectLibClass(fileName);
//
//            sleep(1000);
//            Socket s = new Socket("localhost", 8880);
//
//            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
//            out.writeObject(filePrepare);
//            out.flush();
//            System.out.println("done");
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//
//        }
//    }


}



