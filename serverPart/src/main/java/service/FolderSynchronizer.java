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

    public ObjectCreatorClass compareTree(ObjectCreatorClass treeOnClient) {
        // todo как то продумать что делать, если клиент зашел с разных ПК. если первый раз, то все выкачиваем клиенту, если не первый раз, то пока не ясно что считать актуальным каталогом
        /*сканируем папки и файлы на сервере*/
        String clientFolder = "CLIENT_FOLDER";
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //сканируем папку на сервере
        ObjectCreatorClass treeOnServer = new ObjectCreatorClass("tree", "SERVER_FOLDER/" + clientFolder, "");

        if (treeOnClient.equals(treeOnServer)) {
            System.out.println("Folders and Files are same");
            return null;
        }
        System.out.println("Let's synchronize the folders and folders");

        /*строим струтуру каталогов*/

        /*сравниваем папкм*/

        List<String> remoteDirectoryList = treeOnClient.getDirectoryList();//список папок у клиента
        List<String> serverDirectoryList = treeOnServer.getDirectoryList();//список папок на сервере
        List<String> deleteFolderList = new ArrayList<>(serverDirectoryList);//что лишнее на сервере

        /*Сравниваем папки*/
        for (String clientFolderOne : remoteDirectoryList) {
            for (String serverFolderOne : serverDirectoryList) {
                if (serverFolderOne.equals("SERVER_FOLDER\\" + clientFolderOne)) {
                    deleteFolderList.remove(serverFolderOne);//если у клиента есть такая я же папка как и на сервере,  (не будем удалять)
                } else {
                    //если такая папка есть у клиента, но нет на сервере, создаем
                    Path path = Paths.get("SERVER_FOLDER/" + clientFolderOne);
                    if (!Files.exists(path)) {
                        try {
                            Files.createDirectory(path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        // System.out.println("for delete " + deleteFolderList);

        /*удаляем папки, которых уже нет у клиента*/
        FileProcessing.deleteFolders(deleteFolderList);
        System.out.println("Folders synchronized");


        /*сравниваем файлы*/
        List<FileProperty> serverFileList = treeOnServer.getFileList();//список фалов на сервере
        List<FileProperty> clientFileList = treeOnClient.getFileList();//список файлов у клиента
        List<FileProperty> differentList = new ArrayList<>(clientFileList);//чего не хаватет на сервере
        List<FileProperty> deleteList = new ArrayList<>(serverFileList);//что лишнее на сервере

        if (serverFileList.equals(clientFileList)) {
            //  System.out.println("FolderSynchronizer say:Files are same");
            return null;
        }
        System.out.println("files are different/ let's synchronize");


        /*составляем список фалов, которые клинет должен отправить. */

        for (FileProperty clientFile : clientFileList) {
            for (FileProperty serverFile : serverFileList) {
                if (serverFile.equals(clientFile)) {
                    /*Берем список файлов у клиента.если такой фаил есть на сервере и у клиента, удаляем из списка. Его отправлять на сервер не надо.
                     в списке остануться только файлы, которых не хватает на сервере*/
                    differentList.remove(serverFile);

                    /*Берем список фалов на сервере. Если такой фаил есть на сервере и у клиента, удаляем из списка. .
                    // в списке остануться только файлы, которые лишние на сервере*/
                    deleteList.remove(serverFile);
                    //    System.out.println(serverFileList.contains(clientFile)); не подходит - разные пути расположения файлов
                }

            }
        }

        /*просим клиента отпроавить недостающие файлы*/

        ObjectCreatorClass giveMeFiles = new ObjectCreatorClass("giveMeFiles", differentList); //напихали в список
        giveMeFiles.setTotalFiles(differentList.size());
        giveMeFiles.setFileList(differentList);

        /*удаляем файлы, коотрых нет у клиента*/
        FileProcessing.deleteFiles(deleteList);
        System.out.println("Files synchronized");

        return giveMeFiles; //возвращаем список фалов которые клиент должен отправить


    }
}
