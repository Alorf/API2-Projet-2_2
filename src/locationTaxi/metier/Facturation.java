package locationTaxi.metier;

/**
 * Classe Facturation de la société de Taxi
 * @Author Lorfèvre Arthur
 * @Version 1.0
 */
public class Facturation {
    /**
     * Cout de facturation de la course
     */
    private double cout;
    /**
     * Taxi utilisé pour la facturation de la course
     */
    private Taxi vehicule;

    /**
     * Constructeur paramétré de la facture de la course
     * @param cout Cout de la facturation de la course
     * @param vehicule Taxi utilisé pour la facturation de la course
     */
    public Facturation(double cout, Taxi vehicule) {
        this.cout = cout;
        this.vehicule = vehicule;
    }

    /**
     * Getter cout
     * @return Cout de facturation de la course
     */
    public double getCout() {
        return cout;
    }

    /**
     * Setter cout
     * @param cout Nouveau cout de facturation de la course
     */
    public void setCout(double cout) {
        this.cout = cout;
    }

    /**
     * Getter taxi
     * @return Vehicule utilisé pour la facturation de la course
     */
    public Taxi getVehicule() {
        return vehicule;
    }

    /**
     * Setter taxi
     * @param vehicule Nouveau taxi utilisé pour la facturation de la course
     */
    public void setVehicule(Taxi vehicule) {
        this.vehicule = vehicule;
    }

    @Override
    public String toString() {
        return "Facturation{" +
                "cout=" + cout +
                ", vehicule=" + vehicule +
                '}';
    }
}
