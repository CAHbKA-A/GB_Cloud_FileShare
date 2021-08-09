

import Services.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lib.ObjectCreatorClass;


import java.net.Socket;

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







        //  clientFolder = "CLIENT_FOLDER";
        //обираем список фалов и хэш клиентской папки, если не задана. если задана то можно будет сохранить все настройки сервера в фаил и оттуда брать. пока по дефолту
        //  ObjectCreator ClientFolderObj = new ObjectCreator(clientFolder);

        //TODO и отправка на сервер. сначала хэша и размера дирректории, если изменилось, то переача списка фалов

        //TODO тут будет запущена синхра


    }


//    public void sendObject(ObjectCreator o) {
//        System.out.println("Sending: " + o);
//
//
//            channelFuture.channel().writeAndFlush(o);
//
//    }






    public void connection() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            new Bootstrap().group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel socketChannel) {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new ObjectEncoder(), new ObjectDecoder(ClassResolvers.cacheDisabled(null)), new ClientHandler());

                        }
                    })
                    .connect(HOST, PORT).sync()
                    .channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        finally {
//            group.shutdownGracefully();
//        }
    }


    public void disconnet(NioEventLoopGroup group) {
        group.shutdownGracefully();
    }




}



