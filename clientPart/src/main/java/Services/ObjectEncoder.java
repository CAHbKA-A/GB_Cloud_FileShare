package Services;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lib.FileObjectLibClass;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ObjectEncoder extends SimpleChannelInboundHandler<Object> {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //   ctx.writeAndFlush( new FileObjectLibClass("CLIENT_FOLDER/txt.txt"));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
          FileObjectLibClass objectForTransfer = new FileObjectLibClass("CLIENT_FOLDER/txt.txt");
           ctx.writeAndFlush(objectForTransfer)
                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        FileObjectLibClass objectForTransfer = (FileObjectLibClass) o;
        saveAsFile(objectForTransfer);
    }

    /*если прилетел фаил*/
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
