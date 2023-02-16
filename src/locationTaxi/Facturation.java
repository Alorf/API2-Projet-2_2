package locationTaxi;

/**
 * Classe Facturation de la société de Taxi
 * @Author Lorfèvre Arthur
 * @Version 1.0
 */
public class Facturation {
    /**
     * Cout de facturation
     */
    private double cout;
    /**
     * Taxi utilisé pour la facturation
     */
    private Taxi taxi;

    /**
     * Constructeur de la facture
     * @param cout Cout de la facturation
     * @param taxi Taxi utilisé pour la facturation
     */
    public Facturation(double cout, Taxi taxi) {
        this.cout = cout;
        this.taxi = taxi;
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
     * @return taxi utilisé pour la facturation
     */
    public Taxi getTaxi() {
        return taxi;
    }

    /**
     * Setter taxi
     * @param taxi Nouveau taxi utilisé pour la facturation
     */
    public void setTaxi(Taxi taxi) {
        this.taxi = taxi;
    }
}
