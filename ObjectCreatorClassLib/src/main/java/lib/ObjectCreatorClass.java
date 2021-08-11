package lib;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class ObjectCreatorClass implements Serializable {

    private String TypeOfMessage;
    private String message;
    private String nameOfClent;
    private String token;
    private String scanPath;
    private String fileName;
    private long fileSize;
    private long clientFolderSize; //пригодяится для быстрой сверки каталогов
 //   private long clientFolderHash; //может пригодится для быстрой сверки каталого, если размеры совпадут или переопределить has /equals
    private int totalFiles; //Количество файлов
    /*список директорий, чтобы восстановить структуру каталогов*/
    private List<String> directoryList;
    /*список файлов, чтобы восстановить структуру каталогов*/
    private List<FileProperty> fileList;
    private byte[] fileBin;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectCreatorClass that = (ObjectCreatorClass) o;
        //todo имена каталогов разичаются. сделать выдергу из полного имени файла тольо имя самого файла
        return clientFolderSize == that.clientFolderSize && totalFiles == that.totalFiles  && Objects.equals(directoryList, that.directoryList) && Objects.equals(fileList, that.fileList); /*todo вспомнить как сравниваются Листы*/

    }

    @Override
    public int hashCode() {
        return Objects.hash(clientFolderSize, totalFiles, directoryList, fileList);
    }

    @Override
    public String toString() {
        return "ObjectCreatorClass{" +
                "TypeOfMessage='" + TypeOfMessage + '\'' +
                ", message='" + message + '\'' +
                ", nameOfClent='" + nameOfClent + '\'' +
                ", token='" + token + '\'' +
                ", scanPath='" + scanPath + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", clientFolderSize=" + clientFolderSize +
              //  ", clientFolderHash=" + clientFolderHash +
                ", totalFiles=" + totalFiles +
                ", directoryList=" + directoryList +
                ", fileList=" + fileList +
                ", fileBin=" + Arrays.toString(fileBin) +
                '}';
    }

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
        if (auth.equals("auth_res")) {
            this.TypeOfMessage = "auth_res";
            this.token = s1;
            this.message = s2;
        }

        //генерируем дерево (client->server)
        if (auth.equals("tree")) {
            this.TypeOfMessage = "tree";
            this.scanPath = s1;
            this.totalFiles = 0;
            this.clientFolderSize = 0;
            directoryList= new ArrayList<>();
            fileList= new ArrayList<>();
            walkingTree();
        }


        //генерируем дерево (client<->server)
        if (auth.equals("file")) {
            this.TypeOfMessage = "file";
            this.scanPath = s1;
            this.message = "file";
            this.totalFiles = 1;
            filePacking(s1+"/"+s2);

        }

    }


    public String getTypeOfMessage() {
        return TypeOfMessage;
    }

    public String getMessage() {
        return message;
    }

    public String getFileName() {
        return fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public byte[] getFileBin() {
        return fileBin;
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

    // public long getClientFolderHash() {
    //    return totalFiles * clientFolderSize * 32;
 //   }


    public String getScanPath() {
        return scanPath;
    }

    public List<String> getDirectoryList() {
        return directoryList;
    }

    public List<FileProperty> getFileList() {
        return fileList;
    }

    public int getTotalFiles() {
        return totalFiles;
    }

    /*обьект фаил, содержащий инфу о фале*/

    public void walkingTree() {



        try {
            Files.walkFileTree(Paths.get(scanPath), new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    directoryList.add(dir.toString());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {

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


    public void filePacking(String fileName) {

        this.fileName = fileName;
        File inputFile = new File(fileName);
        this.fileSize = inputFile.length();
        StringBuilder fileInfo = new StringBuilder(inputFile.getPath());
        this.message = fileInfo.toString();
        try {
            System.out.println(Paths.get(fileName));
            this.fileBin = Files.readAllBytes(Paths.get(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}


