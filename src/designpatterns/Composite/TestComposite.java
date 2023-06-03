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
        c2.getElements().add(t3);
        c2.getElements().add(t4);
        c3.getElements().add(t5);

        c2.getElements().add(c1);
        c3.getElements().add(c2);

        System.out.println(c1); // id 1 -> 2 véhicules
        System.out.println("------------------------");
        System.out.println(c2); // id 2 -> 2 véhicules
        System.out.println("------------------------");
        System.out.println(c3.nombreVehicule());
        System.out.println(c3); // id 3 -> 1 véhicules
        // Total de 5 véhicules
        //Le c3 a rappeller 2x la classe Catégorie donc 1 \n en normal et 2 \n en plus
    }
}
