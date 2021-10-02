package services;

import java.io.*;
import java.nio.file.Path;

public class Config implements Serializable {
    private String login;
    private Path clientFolder;
    private String token;

    public Config(String login, Path clientFolder, String token) {
        this.login = login;
        this.clientFolder = clientFolder;
        this.token = token;
    }

    public void save(Config config) {
        try {
            FileOutputStream outputStream = null;
            outputStream = new FileOutputStream("Config.bin");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(config);
            objectOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Config read() {
        FileInputStream fileInputStream = null;
        Config config = null;
        try {
            fileInputStream = new FileInputStream("C:\\Users\\Username\\Desktop\\save.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            config = (Config) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return config;

    }

}
