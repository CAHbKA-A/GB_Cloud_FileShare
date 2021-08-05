package service;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class ServerHandler extends SimpleChannelInboundHandler<String> {
    private static String clientName; //чей поток
    private static boolean isAutorized = false; //авторизован ли


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(msg);
        /*если запрос авторизации.   /auth логин пароль*/
        //todo передалать на case of
        if (msg.startsWith("/auth")) {
            System.out.println("authorization");
            /*алгоритм авторизации*/
            authentication(msg, ctx);

        }
        // todo прверка авторизован ли клиент
        /*если пришел запрос на отправку файла*/
        if (msg.startsWith("/ready2transferTree")) {
            System.out.println("Client ready to transfer Tree");

            /*готовим приемник*/
            /*ТУТ Затык*/
            ctx.writeAndFlush("/Lets_go!");
            ctx.pipeline().addFirst(new ObjectDecoder());



            /*может надо вызват внешний метод и оттуда переконфигурировать пайплайны и оттуда же отправиь фаил"*/

        }

    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("New active channel");

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client disconnected");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


    void authentication(String inputMessage, ChannelHandlerContext ctx) {
        if (inputMessage.startsWith("/auth")) {
            String[] parts = inputMessage.split(" ");
            if (parts.length == 3) {

                String nick = (AuthenticationService.authenticationAlgorithm(parts[1], parts[2]));
                if (!nick.equals("")) {
                    System.out.println(nick + " : authorization success");
                    isAutorized = true;
                    /*добавляем в список активных клиентов*/
                    // Client client = new Client(nick, parts[1]);
                    //  Server.subScribe(client); //лучше добавть в БД Connected\Disconnected

                    ctx.writeAndFlush("/authorization success");
                } else {
                    ctx.writeAndFlush("/Wrong login or password.");
                    System.out.println(" Wrong login or password.");


                }
            }
        }
    }

}

//class Client {
//    private NioEventLoopGroup workerGroup;
//    private ServerBootstrap server;
//    private String name;
//    private String login;
//    private String idKey;
//    //  private boolean isAuthorized;
//
//    public Client(String name, String login) {
//        //  this.workerGroup = workerGroup;
//        //  this.server = server;
//        this.name = name;
//        this.login = login;
//        this.idKey = login + name;//todo генератор id по ключу НА СЕССИЮ
//        //  isAuthorized = false;
//
//    }
//    public String getIdKey() {
//        return idKey;
//    }
//    public void setIdKey(String idKey) {
//        this.idKey = idKey;
//    }
//    public String getName() {
//        return name;
//    }
//    public void setName(String name) {
//        this.name = name;
//    }
//
//
//}
