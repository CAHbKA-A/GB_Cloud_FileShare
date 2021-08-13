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

    public void compareTree(ObjectCreatorClass treeOnClient) {
        // todo как то продумать что делать, если клиент зашел с разных ПК. если ервый раз, то все выкачиваем клиенту, есои не первый раз, то пока не ясно что считать актуальным каталогом
        /*сканируем папки и файлы на сервере*/
        String clientFolder = "CLIENT_FOLDER";
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ObjectCreatorClass treeOnServer = new ObjectCreatorClass("tree", "SERVER_FOLDER/" + clientFolder, "");
        //  System.out.println("Remote folder Size = "+ treeOnClient.getClientFolderSize()+"   Local Folder Size = "+treeOnServer.getClientFolderSize());
        // System.out.println("Remote folder Hash = "+ treeOnClient.getClientFolderHash()+"   Local Folder Size = "+treeOnServer.getClientFolderHash());

        if (treeOnClient.equals(treeOnServer)) {
            System.out.println("Folders are same");
            return;
        }
        System.out.println("folders are different/ let's synchronize");

        /*строим струтуру каталогов*/
        List<String> remoteDirectoryList = treeOnClient.getDirectoryList();
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
        List<FileProperty> serverFileList = treeOnServer.getFileList();
        List<FileProperty> clientFileList = treeOnClient.getFileList();
        List<FileProperty> differentList = new ArrayList<>(clientFileList);

        if (serverFileList.equals(clientFileList)) {
            System.out.println("Files are same");
            return;
        }
        System.out.println("files are different/ let's synchronize");


        /*составляем список фалов, которые клинет должен отправить. пока без удаления*/

        for (FileProperty clientFile : clientFileList) {
            for (FileProperty serverFile : serverFileList) {
                if (serverFile.equals(clientFile)) differentList.remove(serverFile);//если такой есть, удаляем из списка
                //    System.out.println(serverFileList.contains(clientFile)); не подходит - разны пути
            }
        }

        /*проверим список недотающих*/
//        for (FileProperty fileProperty : differentList) {
//            System.out.println(fileProperty.getName())
//        }


        /*просим клиента отпроавить недостающие файлы*/
        ObjectCreatorClass giveMeFiles = new ObjectCreatorClass("giveMeFiles", null, null);
        giveMeFiles.setTotalFiles(differentList.size());
        giveMeFiles.setFileList(differentList);
        /*список на удаление*/


    }
}
