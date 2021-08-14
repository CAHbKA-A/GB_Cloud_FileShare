package service;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Server {
    private final int PORT = 8899;
    private final String HOST = "localhost";
    private AuthenticationService authService;
    private static final Logger LOGGER = LogManager.getLogger(Server.class.getName());

    public void start() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel socketChannel) {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(
                                    new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 4, 0, 4),
                                    new LengthFieldPrepender(4),
                                    new ObjectEncoder(),
                                    new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                    new ServerHandler());


                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 12800000)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .bind(HOST, PORT).sync()
                    .channel().closeFuture().syncUninterruptibly();

        } catch (InterruptedException e) {
            LOGGER.error(e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
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
