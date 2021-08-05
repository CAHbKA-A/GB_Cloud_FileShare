package service;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.serialization.ClassResolver;
import lib.FileObjectLibClass;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ObjectDecoder extends SimpleChannelInboundHandler<Object> {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        FileTreeCreator tree = (FileTreeCreator) msg;
        System.out.println(tree+" received");

    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.channel().close();
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
