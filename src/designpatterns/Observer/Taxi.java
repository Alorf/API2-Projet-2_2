package designpatterns.Observer;

import java.util.Objects;

/**
 * Classe Taxi de la société de Taxi
 * @Author Lorfèvre Arthur
 * @Version 1.0
 */
public class Taxi extends Subject{
    /**
     * Identifiant du taxi
     */
    private int id;
    /**
     * Immatriculation unique du taxi
     */
    private String immatriculation;
    /**
     * Type de carburant du taxi
     */
    private String carburant;
    /**
     * Prix au kilomètre du taxi
     */
    private double prixKm;

    /**
     * Constructeur paramétré du taxi
     * @param id Identifiant du taxi
     * @param immatriculation Immatriculation unique du taxi
     * @param carburant Type de carburant du taxi
     * @param prixKm Prix au kilomètre du taxi
     */
    public Taxi(int id, String immatriculation, String carburant, double prixKm) {
        this.id = id;
        this.immatriculation = immatriculation;
        this.carburant = carburant;
        this.prixKm = prixKm;
    }

    /**
     * Getter identifiant
     * @return Identifiant du taxi
     */
    public int getId() {
        return id;
    }

    /**
     * Setter identifiant
     * @param id Nouvel identifiant du taxi
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter immatriculation
     * @return Immatriculation du taxi
     */
    public String getImmatriculation() {
        return immatriculation;
    }

    /**
     * Setter Immatriculation
     * @param immatriculation Nouvelle immatriculation du taxi
     */
    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    /**
     * Getter carburant
     * @return Type de carburant du taxi
     */
    public String getCarburant() {
        return carburant;
    }

    /**
     * Setter carburant
     * @param carburant Type de carburant du taxi
     */
    public void setCarburant(String carburant) {
        this.carburant = carburant;
    }

    /**
     * Getter prix au kilomètre
     * @return Prix au kilomètre du taxi
     */
    public double getPrixKm() {
        return prixKm;
    }

    /**
     * Setter prix au kilomètre
     * @param prixKm Nouveau prix au kilomètre du taxi
     */
    public void setPrixKm(double prixKm) {
        this.prixKm = prixKm;
        notifyObservers();
    }

    /**
     * Égalité entre deux taxis, basée sur l'immatriculation
     * @param o Autre objet
     * @return Booléen confirmant l'égalité ou non de la comparaison
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Taxi taxi = (Taxi) o;
        return Objects.equals(immatriculation, taxi.immatriculation);
    }

    /**
     * Calcul du hashcode du taxi basé sur l'immatriculation
     * @return La valeur du hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(immatriculation);
    }

    /**
     * Affichage des attributs
     * @return Les attributs de l'objet
     */
    @Override
    public String toString() {
        return "Taxi{" +
                "id=" + id +
                ", immatriculation='" + immatriculation + '\'' +
                ", carburant='" + carburant + '\'' +
                ", prixKm=" + prixKm +
                '}';
    }

    @Override
    public String getNotification() {
        return "Nouveau prix pour " + immatriculation + " : " + prixKm + "€/km";
    }
}
