package service;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Server {
    private final int PORT = 8899;
    private final String HOST = "localhost";
    //  private static List<Client_old> clientList;
    private AuthenticationService authService;
    private static final Logger LOGGER = LogManager.getLogger(Server.class.getName());

    public void start() {


        EventLoopGroup group = new NioEventLoopGroup();
        try {
            new ServerBootstrap().group(group)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel socketChannel) {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new ObjectEncoder(), new ObjectDecoder(ClassResolvers.cacheDisabled(null)), new ServerHandler());
                        }
                    })

                    .bind(HOST, PORT).sync()
                    .channel().closeFuture().syncUninterruptibly();
            LOGGER.info("Server started");
        } catch (InterruptedException e) {
            LOGGER.error(e);
        } finally {
            group.shutdownGracefully();
        }
    }


//
//
//    static synchronized void subScribe(Client_old client) {
//        clientList.add(client);
//
//    }
//
//    static synchronized void unSubScribe(Client_old client) {
//        clientList.remove(client);
//        LOGGER.info(client.getName() + " disconnected");
//
//    }


}
