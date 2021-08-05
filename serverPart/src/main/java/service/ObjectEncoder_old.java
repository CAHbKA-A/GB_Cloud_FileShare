package service;

/*отправка обьектов*/

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lib.FileObjectLibClass;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class ObjectEncoder_old extends SimpleChannelInboundHandler<Object> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        //отправляем фаил
        //ctx.writeAndFlush(new FileObjectLibClass("CLIENT_FOLDER/txt.txt"));
        //отправка списка
        FileObjectLibClass objectForTransfer = new FileObjectLibClass("CLIENT_FOLDER/txt.txt");
        ctx.writeAndFlush(objectForTransfer)
                .addListener(ChannelFutureListener.CLOSE);
    }

 /*когдапринимаем, фаил сохраняем  в папку. список принимать не будем.*/
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object o) throws Exception {

        FileObjectLibClass objectForTransfer = (FileObjectLibClass) o;
        saveAsFile(objectForTransfer);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

    }

    private static void saveAsFile(FileObjectLibClass filePrepare) throws FileNotFoundException {
        FileOutputStream fos = new FileOutputStream("res_" + filePrepare.getFileName());
        try {

            fos.write(filePrepare.getFileBin());
            System.out.println(filePrepare.getFileBin());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}


