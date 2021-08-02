package service;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class Server {
    private final int PORT = 8899;
    private static List<ClientByIdHandler> clientList;
    private AuthenticationService authService;
    private static ExecutorService executorService;
    private static final Logger LOGGER = LogManager.getLogger(Server.class.getName());

    public void start()  {

        clientList = new ArrayList<>();
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(5);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap server = new ServerBootstrap();
            server
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {

                            ch.pipeline().addLast(new StringDecoder(), new StringEncoder(), new ServerDecoder()/*, new ObjectEncoder(), new ObjectDecoder(ClassResolvers.cacheDisabled(null))*/);

                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture channelFuture = server.bind(PORT).sync();
            LOGGER.info("Server started!");
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

        authService = new AuthenticationService();

    }


    static synchronized void subScribe(ClientByIdHandler client) {
        clientList.add(client);

    }

    static synchronized void unSubScribe(ClientByIdHandler client) {
        clientList.remove(client);
        LOGGER.info(client.getName() + " disconnected");

    }

    public AuthenticationService getAuthService() {
        return authService;
    }

//    public static boolean isAlreadyConnected(String nick) {
//        for (ClientHandler clientHandler : clientList) {
//            if (clientHandler.getName().equals(nick)) return true;
//        }
//        return false;
//    }


    public static String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(Calendar.getInstance().getTime());
    }


    public void addThread(Thread ch) {
        executorService.execute(ch);
    }
}
