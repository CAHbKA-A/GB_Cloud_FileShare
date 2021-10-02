package services;

import ObjectCreatorClassLib.ObjectCreatorClass;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

import static java.lang.Thread.sleep;


public class ClientHandler  extends SimpleChannelInboundHandler<Object> {
    private String token;
    private String clientFolder = "CLIENT_FOLDER";
    private boolean handlerIsBusy;
    private String login;
    private String password;
    MessageProcessing messageProcessing = new MessageProcessing();

    public ClientHandler(String login, String password) {
        this.login = login;
        this.password = password;
       //ClientHandler clientHandler =new  clientFolder();
    }

    public ClientHandler() {

    }

    public String getToken() {
        return token;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //авторизуемся

        sendObject(new ObjectCreatorClass("auth", login, CryptoUtils.cript(password)), ctx); //пароль в зашифрованном


    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ObjectCreatorClass objectCreatorClass = (ObjectCreatorClass) msg;
        String type = objectCreatorClass.getTypeOfMessage();
        System.out.println(type + ":message received");
        messageProcessing.messageProcessor(type, objectCreatorClass, ctx, this);
        System.out.println("<<<<>>>>");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

    }


    public void sendObject(ObjectCreatorClass o, ChannelHandlerContext ctx) {
        //для больштих файлов
        if (o.getFileSize() >= 1040000) {
            System.out.println("Big Size = " + o.getFileSize()+" "+Thread.currentThread().getName());
            // предупреждаем, что пойдет большой фаил
            ObjectCreatorClass overFile = new ObjectCreatorClass("BigFileMessage", "Start", o.getFileName());
            ctx.channel().writeAndFlush(overFile);
            String nameOfFile = o.getFileName();
            byte fileOne[] = o.getFileBin();
            // byte part[] ;
            o.setFileSize(1040000);
            o.setTypeOfMessage("file");

//разбиваем, отправляем
            int i;
            for (i = 0; i < fileOne.length / 1040000; i++) {
                o.setFileBin(Arrays.copyOfRange(fileOne, i * 1040000, i * 1040000 + 1040000));
                System.out.println("sending part " + i);
                o.setFileName(nameOfFile + "_part_" + i);


                ctx.channel().writeAndFlush(o);
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (fileOne.length % 1040000 != 0) {
                o.setFileBin(Arrays.copyOfRange(fileOne, i * 1040000, fileOne.length));
                System.out.println("sending last part " + i + "  " + (fileOne.length - (i * 1040000)));
                o.setFileName(nameOfFile + "_part_" + i);
                o.setFileSize(fileOne.length - (i * 1040000));
                ctx.channel().writeAndFlush(o);
                i++;

            }


            o.setFileBin(null);
            o.setTypeOfMessage("BigFileMessage");
            o.setMessage("End");
            o.setTotalFiles(i);
            o.setFileName(nameOfFile);
            System.out.println("end of file");
            // предупреждаем, что  отправили все части фаила
            ctx.channel().writeAndFlush(o);
// для маленьких файлов
        } else {
            System.out.println("Sending: " + o.getTypeOfMessage()+" "+Thread.currentThread().getName());

            ctx.channel().writeAndFlush(o);
        }
    }






}

