package service;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ServerDecoder extends SimpleChannelInboundHandler<String> {
    private AuthenticationService authService;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(msg);
        /*если запрос авторизации.   /auth логин пароль*/
        if (msg.startsWith("/auth")){
            System.out.println("authorization");
            /*алгоритм авторизации*/
            authentication(msg,ctx);

        }
        /*если пришел запрос на отправку файла*/
        if (msg.startsWith("/ready2transferFile")){
            System.out.println("Client ready to transfer file");
            /*готовим приемник*/

          ctx.pipeline().remove(this);

          ctx.pipeline().addLast(new ObjectEncoder(), new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
            ctx.writeAndFlush("/Lets_go!");




        }

    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("New active channel");
    //    authService = new AuthenticationService();

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




    void authentication(String inputMessage,ChannelHandlerContext ctx) {
        if (inputMessage.startsWith("/auth")) {
            String[] parts = inputMessage.split(" ");
            if (parts.length == 3) {

                String nick = (AuthenticationService.authenticationAlgorithm(parts[1], parts[2]));
                if (!nick.equals("")) {
                    System.out.println(nick+ " : authorization success");
                    /*добавляем в список активных клиентов*/
                    ClientByIdHandler client= new ClientByIdHandler(  nick, parts[1]);
                     Server.subScribe(client);

                    ctx.writeAndFlush("/authorization success");
                } else {
                    ctx.writeAndFlush("/Wrong login or password.");
                    System.out.println(" Wrong login or password.");

                }
            }
        }
    }

}
