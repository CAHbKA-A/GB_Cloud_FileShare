package services;

import java.io.*;
import java.nio.file.Path;

public class ConfigClass implements Serializable {
    private static final long serialVersionUID = 7736956330752821470L;
    private String login;
    private String clientFolder;
    private String token;

    public ConfigClass(String login, String clientFolder, String token) {
        this.login = login;
        this.clientFolder = clientFolder;
        this.token = token;
    }

    public void save(ConfigClass cc) {
        try {
            FileOutputStream outputStream = null;
            outputStream = new FileOutputStream("Config.bin");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(cc);
            objectOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ConfigClass read() {
        FileInputStream fileInputStream = null;
        ConfigClass config = null;
        try {
            fileInputStream = new FileInputStream("Config.bin");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            config = (ConfigClass) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return config;

    }

    public static void reconfig(String login, String clientFolder, String token) {
        ConfigClass cc = read();

        if (login != null) cc.login = login;
        if (clientFolder!=null)cc.clientFolder = clientFolder;
        if (token!=null)cc.token = token;

        cc.save(cc);



    }

    public  static String getToken(){
     ConfigClass cc = read();
     return cc.token;
    }

    public  static String getPats(){
        ConfigClass cc = read();
        return (cc.clientFolder);
    }
}
