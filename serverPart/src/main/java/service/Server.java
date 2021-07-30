package service;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final int PORT = 8899;
    private static List<ClientHandler> clientList;
    private AuthenticationService authService;
    private static ExecutorService executorService;
    private static final Logger LOGGER = LogManager.getLogger(Server.class.getName());

    public Server() {

        clientList = new ArrayList<>();
        LOGGER.info("Server starting...");
        authService = new AuthenticationService();
        // executorService = Executors.newFixedThreadPool(70);//с ограничением одновременных пользователей/ Пандемия. больше 70 не собираемся.
        executorService = Executors.newCachedThreadPool();//без огрничений
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            LOGGER.info("Server ready.");
            while (true) {
                Socket socket = serverSocket.accept();
                LOGGER.info("Client Connecting..");
                new ClientHandler(this, socket);
            }
        } catch (IOException e) {
           LOGGER.error("ServerSocket Error:" + e);
        } finally {
            executorService.shutdown();
        }
    }


    static synchronized void subScribe(ClientHandler client) {
        clientList.add(client);

    }

    static synchronized void unSubScribe(ClientHandler client) {
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
