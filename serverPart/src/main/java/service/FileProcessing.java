package service;

import FilePropertyLib.FileProperty;
import ObjectCreatorClassLib.ObjectCreatorClass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
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

    public static void deleteFiles(List<FileProperty> fileList) {
        if (fileList.size() > 0) {
            for (FileProperty file : fileList) {
                Path path = Paths.get(file.getPath());
                //    System.out.println(file.getPath() + " deleted!");
                try {
                    Files.deleteIfExists(path);
                } catch (IOException e) {
                    //  e.printStackTrace();
                }

            }
            System.out.println("Files deleted!");
        }
    }

    public static void deleteFolders(List<String> deleteFolderList) {
        if (deleteFolderList.size() > 0) {
            for (String sFolder : deleteFolderList) {
                Path path = Paths.get(sFolder);
                // System.out.println("deleting"+path);
                deleteSubFolders(path);
            }
        }
    }


    public static void deleteSubFolders(Path rootPath) {

        try {
            Files.walkFileTree(rootPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                        throws IOException {
                    //  System.out.println("delete file: " + file.toString());
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws
                        IOException {
                    Files.delete(dir);
                    //  System.out.println("delete dir: " + dir.toString());
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            //  e.printStackTrace();
        }


    }


}
