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
import services.ClientHandler;

import javax.swing.*;

import java.awt.*;
import java.net.Socket;

public class ClientConsole extends JFrame {
    private Socket socket;
    private final int PORT = 8899;
    private final String HOST = "localhost";
   // private ChannelFuture channelFuture;
    private JTextField loginField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JPanel panelNorth;

    public static void main(String[] args) throws InterruptedException {
        ClientConsole clientConsole = new ClientConsole();
    }

    private ClientConsole() throws InterruptedException {

        //GUI
         GUIClient();


    }

    private void GUIClient() {
        setBounds(200, 200, 500, 120);
        setTitle("CloudGB");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);


        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        loginField = new JTextField("A");
        panel.add(loginField, BorderLayout.NORTH);

        passwordField = new JPasswordField("A");
        panel.add(passwordField, BorderLayout.CENTER);

        loginButton = new JButton("Sing in");
        panel.add(loginButton, BorderLayout.SOUTH);
        loginButton.addActionListener(e -> {
            setVisible(false);
            connection(loginField.getText(), passwordField.getText());

        });
        add(panel);
        setVisible(true);
    }


   // public void connection() {
    public void connection(String login, String password) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            new Bootstrap().group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel socketChannel) {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(
                                /*  new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4),
                                    new LengthFieldPrepender(4),*/
                                    new ObjectEncoder(),
                                    new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                   // new ClientHandler(login,password);
                                    new ClientHandler(login,password));
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

    // todo реконнект
    public void disconnet(NioEventLoopGroup group) {
        group.shutdownGracefully();
    }


}



