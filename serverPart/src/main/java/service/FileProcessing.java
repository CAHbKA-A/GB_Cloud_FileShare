package service;

import FilePropertyLib.FileProperty;
import ObjectCreatorClassLib.ObjectCreatorClass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileProcessing {


    public static void saveAsFile(ObjectCreatorClass filePrepare) {

        try {
            File fileA = new File(String.valueOf("SERVER_FOLDER/" + filePrepare.getFileName()));
            FileOutputStream fos = new FileOutputStream(fileA);
            fos.write(filePrepare.getFileBin());//записали
            //исправляем дату послднего измененя (ставим как у клиента)
            fileA.setLastModified(filePrepare.getLastModify());
            //    System.out.println(filePrepare.getLastModify());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void deleteFile(List<FileProperty> fileList) {
        if (fileList.size() > 0) {
            for (FileProperty file : fileList) {
                Path path = Paths.get(file.getPath());
                System.out.println(file.getPath() + " deleted!");
                try {
                    Files.delete(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            System.out.println("Files deleted!");
        }
    }

}
