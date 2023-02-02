package locationTaxi;

public class Facturation {
    private double cout;
    private Taxi vehicule;

    public Facturation(double cout, Taxi vehicule) {
        this.cout = cout;
        this.vehicule = vehicule;
    }

    public double getCout() {
        return cout;
    }

    public void setCout(double cout) {
        this.cout = cout;
    }

    public Taxi getVehicule() {
        return vehicule;
    }

    public void setVehicule(Taxi vehicule) {
        this.vehicule = vehicule;
    }
}
