package service;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.serialization.ClassResolver;
import lib.FileObjectLibClass;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ObjectDecoder extends SimpleChannelInboundHandler<Object> {

    public ObjectDecoder(ClassResolver cacheDisabled) {
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
     //   ctx.writeAndFlush(new FileObjectLibClass("CLIENT_FOLDER/txt.txt"));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //  FileObjectLibClass objectForTransfer = new FileObjectLibClass("CLIENT_FOLDER/txt.txt");

    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        FileObjectLibClass objectForTransfer = (FileObjectLibClass) o;
        saveAsFile(objectForTransfer);
    }


    private static void saveAsFile(FileObjectLibClass filePrepare) throws FileNotFoundException {
        FileOutputStream fos = new FileOutputStream("SERVER_FOLDER_" + filePrepare.getFileName());
        try {

            fos.write(filePrepare.getFileBin());
            System.out.println(filePrepare.getFileBin());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
