package Services;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;


public class ObjectCreatorClass implements Serializable {

    private String TypeOfMessage;
    private String message;
    private String nameOfClent;
    private String token;
    private String scanPath;
    private long clientFolderSize; //пригодяится для быстрой сверки каталогов
    private long clientFolderHash; //может пригодится для быстрой сверки каталого, если размеры совпадут или переопределить has /equals
    private int totalFiles; //Количество файлов
    /*список директорий, чтобы восстановить структуру каталогов*/
    private ArrayList<Path> directoryList;
    /*список файлов, чтобы восстановить структуру каталогов*/
    private ArrayList<FileProperty> fileList;


    //генерируем сообщения
    public ObjectCreatorClass(String auth, String s1, String s2) {
        //todo переделать на caseOf

        //генерируем сообщения для авторизации (client->server)
        if (auth.equals("auth")) {
            //TODO шифруем пароль.
            this.TypeOfMessage = "auth";
            this.token = "0";
            this.message = "/auth " + s1 + " " + s2;
        }

        //генерируем сообщения о результате авторизации (server->client)
        if (auth.equals("auth_res"))
        {
            this.TypeOfMessage = "auth_res";
            this.token = s1;
            this.message =s2;
        }

    }




    public String getTypeOfMessage() {
        return TypeOfMessage;
    }

    public String getMessage() {
        return message;
    }

    public String getNameOfClent() {
        return nameOfClent;
    }

    public String getToken() {
        return token;
    }

    public long getClientFolderSize() {
        return clientFolderSize;
    }

    public long getClientFolderHash() {
        return totalFiles * clientFolderSize * 32;
    }

    public int getTotalFiles() {
        return totalFiles;
    }

    /*обьект фаил, содержащий инфу о фале*/
    class FileProperty {
        private String name;
        private Path path;
        private long size;
        private long lastModify;
        private double hashId;


        public FileProperty(String name, Path path, long size, long lastModify, double hashId) {
            this.name = name;
            this.path = path;
            this.size = size;
            this.lastModify = lastModify;
            this.hashId = hashId;

        }

        @Override
        public String toString() {
            return "FileProperty{" +
                    "name='" + name + '\'' +
                    ", path=" + path +
                    ", size=" + size +
                    ", lastModify=" + lastModify +
                    ", hashId=" + hashId +
                    '}';
        }
    }

    ;


    public ObjectCreatorClass(String scanPath) {
        this.scanPath = scanPath;
        this.totalFiles = 0;
        this.clientFolderSize = 0;
    }

    public void walkingTree() {

        directoryList = new ArrayList<>();
        fileList = new ArrayList<>();

        try {
            Files.walkFileTree(Paths.get(scanPath), new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    //System.out.println(dir);
                    directoryList.add(dir);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    //  System.out.println(file);
                    File fileА = new File(String.valueOf(file));
                    fileList.add(new FileProperty(fileА.getName(), file, fileА.length(), fileА.lastModified(), fileА.length() + fileА.lastModified()));
                    clientFolderSize += fileА.length(); //считаем размер всего каталога с файлами
                    totalFiles++; //считаем файлы
                    //     System.out.println(fileА.length());
                    //  System.out.println(fileА.getName());
                    //  System.out.println(fileА.lastModified());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    // System.out.println("visitFileFailed: " + file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) {

                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}