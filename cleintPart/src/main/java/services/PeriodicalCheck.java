package services;

import ObjectCreatorClassLib.ObjectCreatorClass;
import io.netty.channel.ChannelHandlerContext;

public class PeriodicalCheck extends Thread {
    private ClientHandler clientHandler;
    private ChannelHandlerContext ctx;
    private long timer;


    public PeriodicalCheck(ClientHandler clientHandler, ChannelHandlerContext ctx) {
        this.clientHandler = clientHandler;
        this.ctx = ctx;
        this.timer = System.currentTimeMillis();
    }

    @Override
    public void run() {
        System.out.println("periodical synchronisation started");
      //  System.out.println(System.currentTimeMillis());

        while (true) {
            try {
                sleep(60000);
            //    if (System.currentTimeMillis() - timer >= 30000) {
                    System.out.println(timer);
                    System.out.println("period!!!" + " " + Thread.currentThread().getName());
                    timer = System.currentTimeMillis();
             //   }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

             ObjectCreatorClass tree = new ObjectCreatorClass("tree", "CLIENT_FOLDER", "");
            /*отправляем обновленное дерево*/

              clientHandler.sendObject(tree, ctx);
              break;
        }
    }
}


//
//        System.out.println(System.currentTimeMillis());
//        System.out.println(timer);
//
//        if (System.currentTimeMillis()-timer>= 1000){
//
//            ObjectCreatorClass tree = new ObjectCreatorClass("tree", "CLIENT_FOLDER", "");
//            timer=System.currentTimeMillis();
//        }