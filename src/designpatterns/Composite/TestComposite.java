package designpatterns.Composite;

public class TestComposite {
    public static void main(String[] args) {
        Taxi t1 = new Taxi(1, "AA-123-AA", "Essence", 1.5);
        Taxi t2 = new Taxi(2, "BB-456-BB", "Diesel", 1.3);
        Taxi t3 = new Taxi(3, "CC-789-CC", "Essence", 1.4);
        Taxi t4 = new Taxi(4, "DD-012-DD", "Diesel", 1.2);
        Taxi t5 = new Taxi(5, "EE-345-EE", "Essence", 1.6);
        Categorie c1 = new Categorie(1, "Camionnette");
        Categorie c2 = new Categorie(2, "Auto");
        Categorie c3 = new Categorie(3, "MotorHome");

        c1.getElements().add(t1);
        c1.getElements().add(t2);
        c2.getElements().add(c1);
        c2.getElements().add(t3);
        c2.getElements().add(t4);
        c3.getElements().add(t5);

        System.out.println(c1);
        System.out.println("\n------------------------\n");
        System.out.println(c2);
        System.out.println("\n------------------------\n");
        System.out.println(c3);
    }
}
