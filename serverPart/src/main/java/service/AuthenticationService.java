package service;
//TODO шифрование пароля


import ObjectCreatorClassLib.ObjectCreatorClass;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class AuthenticationService {
    private static final Logger LOGGER = LogManager.getLogger(Server.class.getName());

    public static String authenticationAlgorithm(String login, String pass) {
        return DataBaseService.authentication(login, pass);
    }


    static void authentication(String inputMessage, ChannelHandlerContext ctx) {
        ServerHandler serverHandler = new ServerHandler();

    String[] parts = inputMessage.split(" ");
        if(parts.length ==3)

    {
        String nick = (AuthenticationService.authenticationAlgorithm(parts[1], parts[2]));
        if (!nick.equals("")) {
            String token = "12";// todo генератор токенов с записю в бд
            ObjectCreatorClass o_aut = new ObjectCreatorClass("auth_res", token, "Authorization success");


           serverHandler.sendObject(o_aut, ctx);
            LOGGER.info(nick + " : authorization success");
        } else {
            serverHandler.sendObject(new ObjectCreatorClass("auth_res", "0", "/Wrong login or password"), ctx);
            LOGGER.info(" Wrong login or password.");

        }
    }
}
}