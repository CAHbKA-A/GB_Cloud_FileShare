import java.io.Serializable;

public class Cat implements Serializable {
    private String name;

    @Override
    public String toString() {
        return "Cat{" +
                "name='" + name + '\'' +
                '}';
    }

    public Cat(String name) {
        this.name = name;
    }
}
