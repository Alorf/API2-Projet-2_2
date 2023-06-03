package designpatterns.Composite;

public abstract class Element {
    int id;
    public Element(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public abstract int nombreVehicule();
}
