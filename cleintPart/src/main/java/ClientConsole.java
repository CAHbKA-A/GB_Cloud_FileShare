import io.netty.bootstrap.Bootstrap;
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
import services.ConfigClass;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClientConsole extends JFrame {
    private Socket socket;
    private final int PORT = 8899;
    private final String HOST = "localhost";
    private JTextField loginField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private TextField folderPath;

    public static void main(String[] args) throws InterruptedException {
        ClientConsole clientConsole = new ClientConsole();
        //проверяем это первый запуск или нет/ просто по наличию конфига.
        File fileConfig = new File("Config.bin");
        System.out.println("First start?" + !fileConfig.exists());
         if (!fileConfig.exists()){
        ConfigClass configClass = new ConfigClass(null, "CLIENT FOLDER", null);
        configClass.save(configClass);
          }
    }

    private ClientConsole() throws InterruptedException {

        //GUI
        GUIClient();


    }

    private void GUIClient() {
        setBounds(200, 200, 500, 170);
        setTitle("CloudGB");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        loginField = new JTextField("A");
        panel.add(loginField);

        passwordField = new JPasswordField("A");
        panel.add(passwordField);

        folderPath = new TextField("CLIENT FOLDER");
        panel.add(folderPath);

        Button buttonGetFolder = new Button("ClientFolder");
        panel.add(buttonGetFolder);

        buttonGetFolder.addActionListener(e -> {
            folderPath.getText();
            JFileChooser folderChooser = new JFileChooser();
            folderChooser.setCurrentDirectory(new File("."));
            folderChooser.setDialogTitle("Какую папку будем синхронизировать?");

            folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            folderChooser.setAcceptAllFileFilterUsed(false);
            if (folderChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                folderPath.setText(String.valueOf(folderChooser.getSelectedFile()));

            }

        });

        loginButton = new JButton("Sing in");
        panel.add(loginButton);
        loginButton.addActionListener(e -> {
            ConfigClass.reconfig(loginField.getText(), folderPath.getText(), null);
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
                                    new ClientHandler(login, password));
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



