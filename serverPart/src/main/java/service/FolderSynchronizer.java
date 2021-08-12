package service;

/*приходится подключать как бибблиотеки, иначе не десериализуется*/

import FilePropertyLib.FileProperty;
import ObjectCreatorClassLib.ObjectCreatorClass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class FolderSynchronizer {

    public void compareTree(ObjectCreatorClass remoteTree) {
        // todo как то продумать что делать, если клиент зашел с разных ПК. если ервый раз, то все выкачиваем клиенту, есои не первый раз, то пока не ясно что считать актуальным каталогом
        /*сканируем папки и файлы на сервере*/
        String clientFolder = "CLIENT_FOLDER";
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ObjectCreatorClass localTree = new ObjectCreatorClass("tree", "SERVER_FOLDER/" + clientFolder, "");
        //  System.out.println("Remote folder Size = "+ remoteTree.getClientFolderSize()+"   Local Folder Size = "+localTree.getClientFolderSize());
        // System.out.println("Remote folder Hash = "+ remoteTree.getClientFolderHash()+"   Local Folder Size = "+localTree.getClientFolderHash());

        if (remoteTree.equals(localTree)) {
            System.out.println("Folders are same");
            return;
        }
        System.out.println("folders are different/ let's synchronize");

        /*строим струтуру каталогов*/
        List<String> remoteDirectoryList = remoteTree.getDirectoryList();
        for (String s : remoteDirectoryList) {
            //  System.out.println(s);
            Path path = Paths.get("SERVER_FOLDER/" + s);
            if (!Files.exists(path)) {
                try {
                    Files.createDirectory(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        System.out.println("Folders synchronized");

        /*сравниваем файлы*/
        List<FileProperty> localFileList = localTree.getFileList();
        List<FileProperty> remoteFileList = remoteTree.getFileList();
        List<FileProperty> differentList = new ArrayList<>();

        if (localFileList.equals(remoteFileList)) {
            System.out.println("Files are same");
            return;
        }
        System.out.println("files are different/ let's synchronize");


        /*составляем список фалов, которые клинет должен отправить. пока без удаления*/
        for (FileProperty remote : remoteFileList) {
            for (FileProperty local : localFileList) {
                //   System.out.println(local);
                //    System.out.println(remote);
                if (!remote.equals(local)) {
                    differentList.add(remote);
                    //  System.out.println("I got it "+ local.getName());
                }
            }
        }
        // System.out.println(differentList);

        /*просим клиента отпроавить недостающие файлы*/
        ObjectCreatorClass giveMeFiles = new ObjectCreatorClass("giveMeFiles", null, null);
    giveMeFiles.setTotalFiles(differentList.size());
    giveMeFiles.setFileList(differentList);
        /*список на удаление*/


    }
}
