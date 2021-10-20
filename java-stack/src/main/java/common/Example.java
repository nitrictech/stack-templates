package common;

public class Example {

    public String name;
    public String description;

    @Override
    public String toString() {
        return getClass().getSimpleName() + 
            "[name=" + name +
            ", description=" + description +
            "]";
    }
}