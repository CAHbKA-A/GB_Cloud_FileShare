package services;

/*обработака сообщений полученных от сервера*/

import FilePropertyLib.FileProperty;
import ObjectCreatorClassLib.ObjectCreatorClass;
import io.netty.channel.ChannelHandlerContext;

import static java.lang.Thread.sleep;

public class MessageProcessing {

    void messageProcessor(String type, ObjectCreatorClass o, ChannelHandlerContext ctx,ClientHandler clientHandler ) {
      //  ClientHandler clientHandler = new ClientHandler();
        //обрабатываем результат авторизации
        if (type.equals("auth_res")) {
            if (o.getMessage().equals("Authorization success")) {
                System.out.println("Authorization success, we have token = " + o.getToken());
                ObjectCreatorClass tree = new ObjectCreatorClass("tree", "CLIENT_FOLDER", "");
                clientHandler.sendObject(tree, ctx);
            } else
                System.out.println("Authorization failure");
        }





        if (type.equals("file")) {
            System.out.println("i have a file:" + o.getFileName() + " .Size:" + o.getFileSize());
            FileProcessing.saveAsFile(o);
        }
        if (type.equals("BigFileMessage")) {
            if (o.getMessage().equals("Start")) {
                System.out.println("Big FIle " + o.getFileName() + " will sending!!");
            }
            if (o.getMessage().equals("End")) {
                System.out.println("All "+o.getTotalFiles()+" parts of Big FIle " + o.getFileName() + " relieved! lets merge.");
                FileProcessing.saveAsBigFile(o.getFileName() ,o.getTotalFiles(), o.getLastModify());
            }

            if (o.getMessage().equals("part")) {
                System.out.println("Receiving  part");
            }
        }






//  обрабатываем списое недостающих на сервере файов
        if (type.equals("giveMeFiles")) {
            System.out.println("We must send "+o.getTotalFiles() +" files to server");

            /*отправляем файлы согласно списку*/
            for (FileProperty o1 : o.getFileList()) {
                //    System.out.println(o1.getPath());
                ObjectCreatorClass file = new ObjectCreatorClass("file", "", o1.getPath());
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                clientHandler.sendObject(file, ctx);
            }

            System.out.println("All files sent");
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }




            //мониторим папки
            Thread threadFileWatcher = new FileWatcher("CLIENT_FOLDER", ctx,clientHandler);
            threadFileWatcher.start();













        }
//если папки одинаковые, то запускаем вочер папки у клиента
        if (type.equals("foldersAreSame")) {
            System.out.println("Folders are synchronized!!!");



            //мониторим папки
            Thread threadFileWatcher = new FileWatcher("CLIENT_FOLDER", ctx,clientHandler);
            threadFileWatcher.start();

        }







    }

//    private void threadSynkByTime() {
//        long timer = System.currentTimeMillis();
//        System.out.println(System.currentTimeMillis());
//        System.out.println(timer);
//
//        if (System.currentTimeMillis()-timer>= 1000){
//
//            ObjectCreatorClass tree = new ObjectCreatorClass("tree", "CLIENT_FOLDER", "");
//            timer=System.currentTimeMillis();
//        }
//    }
}
