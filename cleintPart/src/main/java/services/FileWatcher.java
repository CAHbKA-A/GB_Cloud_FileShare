package services;

import ObjectCreatorClassLib.ObjectCreatorClass;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;


class FileWatcher extends Thread {

    public FileWatcher(String clientFolder, ChannelHandlerContext ctx,ClientHandler clientHandler) {
        this.clientFolder = clientFolder;
        this.ctx = ctx;
        this.clientHandler =clientHandler;
    }

    private String clientFolder;
    private ChannelHandlerContext ctx;
    private ClientHandler clientHandler;

    /*будем запускать между синхонихациями каталогов*/
    @Override
    public void start() {

        System.out.println("Watcher started");
        WatchService watchService = null;


        try {
            watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(clientFolder);
            path.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
            while (true) {


                WatchKey take = watchService.take();
                for (WatchEvent event : take.pollEvents()) {

                    System.out.println("Event kind:" + event.kind() + " filename: " + event.context());
                    sleep(10000);//отсрачиваем запуск синхры
                    /*сканируем папку заново*/
                    ObjectCreatorClass tree = new ObjectCreatorClass("tree", "CLIENT_FOLDER", "");
                    /*отправляем обновленное дерево*/
                    ClientHandler clientHandler = new ClientHandler("","");
                  clientHandler.sendObject(tree, ctx);
                    break;

                    //


                }
                take.reset();
                break;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }


}