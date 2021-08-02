package service;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;


public class ClientByIdHandler {
    private NioEventLoopGroup workerGroup;
    private ServerBootstrap server;
    private String name;
    private String login;
    private String idKey;
    //  private boolean isAuthorized;

    public ClientByIdHandler(String name, String login) {
        //  this.workerGroup = workerGroup;
        //  this.server = server;
        this.name = name;
        this.login = login;
        this.idKey = login + name;//todo генератор id по ключу НА СЕССИЮ
        //  isAuthorized = false;


    }

    public String getIdKey() {
        return idKey;
    }

    public void setIdKey(String idKey) {
        this.idKey = idKey;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
