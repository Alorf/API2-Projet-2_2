package designpatterns.Observer;

public class TestObserver {
    public static void main(String[] args) {
        Taxi t1 = new Taxi(1, "AA-123-AA", "Essence", 1.5);
        Taxi t2 = new Taxi(2, "BB-456-BB", "Diesel", 1.3);

        Client c1 = new Client(1, "dupont@gmail.com", "Dupont", "Jean", "475666666");
        Client c2 = new Client(2, "test@gmail.com", "Pastice", "Richard", "478555555");

        t1.addObserver(c1);
        t2.addObserver(c2);

        t1.setPrixKm(12);
        t2.setPrixKm(15);
    }
}
