package service;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Server {
    private final int PORT = 8899;
  //  private static List<Client_old> clientList;
    private AuthenticationService authService;
    private static final Logger LOGGER = LogManager.getLogger(Server.class.getName());

    public void start() {

 //       clientList = new ArrayList<>();
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

                            ch.pipeline().addLast(new StringDecoder(), new StringEncoder(), new ServerHandler()/*, new ObjectEncoder(), new ObjectDecoder(ClassResolvers.cacheDisabled(null))*/);

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
