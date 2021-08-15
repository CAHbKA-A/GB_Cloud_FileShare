package service;

import ObjectCreatorClassLib.ObjectCreatorClass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileProcessing {



    public static void saveAsFile(ObjectCreatorClass filePrepare) {

        try {
            File fileА = new File(String.valueOf("SERVER_FOLDER/" + filePrepare.getFileName()));
            FileOutputStream fos = new FileOutputStream(fileА);
            fos.write(filePrepare.getFileBin());//записали
            //исправляем дату послднего измененя (ставим как у клиента)
            fileА.setLastModified(filePrepare.getLastModify());
            //    System.out.println(filePrepare.getLastModify());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
