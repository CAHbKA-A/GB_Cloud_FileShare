package service;
/*приннимаем обьект и сохраняем его в фаил*/

import lib.FileObjectLibClass;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class FileReceiver {


    public static void start() throws IOException, ClassNotFoundException {
        ServerSocket server1 = new ServerSocket(8880);
        Socket socket = server1.accept();
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        FileObjectLibClass filePrepare = (FileObjectLibClass) in.readObject();
        saveAsFile(filePrepare);
    }

//
//
//    public static void recieveFileAsFile(String fileNme) throws IOException {
//            ServerSocket server1 = new ServerSocket(8880);
//            Socket socket = server1.accept();
//            byte[] b=new byte[1024];
//            try {
//                InputStream in = socket.getInputStream();
//                DataInputStream din = new DataInputStream (new BufferedInputStream (in));
//                File f = new File(String.valueOf(fileNme));
//                RandomAccessFile fw = new RandomAccessFile(f, "rw");
//                int num = din.read(b);
//                while (num != -1) {
//                    fw.write (b, 0, num);
//                    fw.skipBytes (num);
//                    num = din.read(b);
//                }
//                din.close();
//                fw.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        socket.close();
//        }




    private static void saveAsFile(FileObjectLibClass filePrepare) throws FileNotFoundException {
        FileOutputStream fos = new FileOutputStream("res_"+filePrepare.getFileName());
        try {

            fos.write(filePrepare.getFileBin());
            System.out.println(filePrepare.getFileBin());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}

