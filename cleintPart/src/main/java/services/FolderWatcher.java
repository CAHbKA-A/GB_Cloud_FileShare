package services;

import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;

class FileWatcher extends Thread {

/*будем запускать между синхонихациями каталогов*/
    @Override
    public void start() {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get("CLIENT_FOLDER");
            path.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
            while (true) {
                WatchKey take = watchService.take();
                for (WatchEvent event : take.pollEvents()) {
                    System.out.println("Event kind:" + event.kind() + " filename: " + event.context());
                }
                take.reset();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}