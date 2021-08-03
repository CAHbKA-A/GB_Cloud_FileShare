package Services;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

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