import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
/*класс для упаковки фалйа в объект.
потом это объкт будем передавать по сети и на приемнике перобразовывать в фаил*/

public class FilePrepare implements Serializable {
    //private static final long serialVersionUID = 1L;
    private long fileSize;
    private String fileName;
    private String fileInfo;
    private byte[] fileBin;


    /*для проверки*/
    @Override
    public String toString() {
        return "FilePrepare{" +
                ", fileSize=" + fileSize +
                ", fileName='" + fileName + '\'' +
                ", fileInfo='" + fileInfo + '\'' +
                ", fileBin=" + Arrays.toString(fileBin) +
                '}';
    }

    public FilePrepare(String fileName) throws IOException {
        this.fileName = fileName;
        File inputFile = new File(fileName);
        this.fileSize = inputFile.length();
        StringBuilder fileInfo = new StringBuilder("");
        fileInfo.append("//")
                .append(fileSize)
                .append("//")
                .append(inputFile.getPath())
                .append("//")
                .append(inputFile.getName())
                .append("//");
        this.fileInfo = fileInfo.toString();
        /*запаковываем фаил в обьект*/
        this.fileBin = Files.readAllBytes(Paths.get(fileName));

    }

    public long getFileSize() {
        return fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileInfo() {
        return fileInfo;
    }

    public byte[] getFileBin() {
        return fileBin;
    }

//    public ByteBuffer createByteBuffer(String fileName) throws IOException {
//
//        File inputFile = new File(fileName);
//
//        FileInputStream fis = new FileInputStream(inputFile);
//        System.out.println("Prepare to send File: " + fileName + "/  :  " + fis.available() + " bytes");
//
//        ByteBuffer byteBuffer;// = ByteBuffer.allocate(bufferSize);
//        bufferSize = fis.available();
//        byte[] buffer = new byte[bufferSize];
//        fis.read(buffer);
//        System.out.println();
//        byteBuffer = ByteBuffer.wrap(buffer);
//        fis.close();
//        return byteBuffer;
//    }


}
