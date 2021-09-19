package FilePropertyLib;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.Objects;


public class FileProperty implements Serializable {
    private String name;
    private String path;
    private long size;
    private long lastModify;
    private double hashId;

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setLastModify(long lastModify) {
        this.lastModify = lastModify;
    }

    public double getHashId() {
        return hashId;
    }

    public void setHashId(double hashId) {
        this.hashId = hashId;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public long getSize() {
        return size;
    }

    public long getLastModify() {
        return lastModify;
    }



    @Override
    public boolean equals(Object o) {


        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileProperty that = (FileProperty) o;

        //todo проверить как отрабтывает. имена каталогов у клиента и сервера разные

        return size == that.size && lastModify == that.lastModify && name.equals(that.name) && path.equals("SERVER_FOLDER\\" + that.path);
    }

    @Override
    public int hashCode() {
        ;
        return Objects.hash(name, size, lastModify);
    }

    public FileProperty(String name, Path path, long size, long lastModify, double hashId) {
        this.name = name;
        this.path = path.toString();
        this.size = size;
        this.lastModify = lastModify;
        this.hashId = hashId;

    }

    @Override
    public String toString() {
        return "FileProperty{" +
                "name='" + name + '\'' +
                ", path=" + path +
                ", size=" + size +
                ", lastModify=" + lastModify +
                ", hashId=" + hashId +
                '}';
    }
}

