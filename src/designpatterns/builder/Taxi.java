package designpatterns.builder;

import java.util.Objects;

/**
 * Classe Taxi de la société de Taxi
 * @Author Lorfèvre Arthur
 * @Version 1.0
 */
public class Taxi {
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

    public Taxi(TaxiBuilder tb) {
        this.id = tb.id;
        this.immatriculation = tb.immatriculation;
        this.carburant = tb.carburant;
        this.prixKm = tb.prixKm;
    }

    /**
     * Getter identifiant
     * @return Identifiant du taxi
     */
    public int getId() {
        return id;
    }

    /**
     * Getter immatriculation
     * @return Immatriculation du taxi
     */
    public String getImmatriculation() {
        return immatriculation;
    }

    /**
     * Getter carburant
     * @return Type de carburant du taxi
     */
    public String getCarburant() {
        return carburant;
    }

    /**
     * Getter prix au kilomètre
     * @return Prix au kilomètre du taxi
     */
    public double getPrixKm() {
        return prixKm;
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

    public void setId(int id) {
        this.id = id;
    }

    public static class TaxiBuilder{
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

        public TaxiBuilder setId(int id) {
            this.id = id;

            return this;
        }

        public TaxiBuilder setImmatriculation(String immatriculation) {
            this.immatriculation = immatriculation;

            return this;
        }

        public TaxiBuilder setCarburant(String carburant) {
            this.carburant = carburant;

            return this;
        }

        public TaxiBuilder setPrixKm(double prixKm) {
            this.prixKm = prixKm;

            return this;
        }

        public Taxi build() throws Exception{
            if (false){
                //Vérification non demandée mais je garde le code si jamais
                throw new Exception("Informations de construction incomplètes");
            }

            return new Taxi(this);
        }
    }
}
