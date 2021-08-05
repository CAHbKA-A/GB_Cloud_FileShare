import Services.ClientDecoder;
import Services.FileTreeCreator;
import Services.ObjectEncoder;
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

    public static void main(String[] args) throws InterruptedException {
        ClientConsole clientConsole = new ClientConsole();
    }

    private ClientConsole() throws InterruptedException {

        //TODO  GUIClient(); //TODO ТУТ будет GUI авторизации + выбор папки*

        // соединились.
        connection();
        //авторизация
        singIn("B", "B");
        clientFolder = "CLIENT_FOLDER";
        //обираем список фалов и хэш клиентской папки, если не задана. если задана то можно будет сохранить все настройки сервера в фаил и оттуда брать. пока по дефолту
        FileTreeCreator ClientFolderObj = new  FileTreeCreator(clientFolder);

        //TODO и отправка на сервер. сначала хэша и размера дирректории, если изменилось, то переача списка фалов

        //TODO тут будет запущена синхра

        //без паузы сообщения сливаются в одно.  разобраться, устранить. можетдобавить символ конца отправляемх байтов
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*передаем структуру папки*/
        sendMessage("/ready2transferTree"); //- предупреждаем,чтобы перестроил пйплайн на прием обьекта

        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*отправляем само дерев*///тут пока затык надо перестроить пайплайн
        channelFuture.channel().writeAndFlush(ClientFolderObj).sync();

        sendMessage("Done!");


    }

    /*создаем струтуру каталогов клиента*/
    private FileTreeCreator FolderTreeObject(String clientFolder) {
        /*сканируем клиентскую папку*/
        FileTreeCreator ftc = new FileTreeCreator(clientFolder);
        ftc.walkingTree();
        System.out.println("Всего файлов: " + ftc.getTotalFiles() + "  Размер локльной папки:" + ftc.getClientFolderSize() + "  HASH: " + ftc.getClientFolderHash());

        //  System.out.println(ftc.getFileList());
        //  System.out.println(ftc.getDirectoryList());
        /*сериализуем для передачи на сервер в виде файла(обьекта)*/
        return ftc;
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


}



