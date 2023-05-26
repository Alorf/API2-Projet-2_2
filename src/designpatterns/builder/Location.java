package designpatterns.builder;

import utilitaire.Utilitaire;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Location de la société de Taxi
 *
 * @Author Lorfèvre Arthur
 * @Version 1.0
 */
public class Location {
    /**
     * Identifiant de la location
     */
    private int id;
    /**
     * Date de la location
     */
    private LocalDate date;
    /**
     * Kilomètres total parcouru
     */
    private int kmTotal;
    /**
     * Client de la location
     */
    private Client client;
    /**
     * Adresse de départ de la location
     */
    private Adresse adrDepart;
    /**
     * Liste de facturations de la location
     */
    private List<Facturation> facturations = new ArrayList<>();

    private Location(LocationBuilder lb) {
        this.id = lb.id;
        this.date = lb.date;
        this.kmTotal = lb.kmTotal;
        this.client = lb.client;
        this.adrDepart = lb.adrDepart;
    }

    /**
     * Getter identifiant
     *
     * @return Identifiant de la location
     */
    public int getId() {
        return id;
    }

    /**
     * Getter date
     *
     * @return Date de la location
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Getter kilomètres total
     *
     * @return Kilomètres total parcouru lors de la location
     */
    public int getKmTotal() {
        return kmTotal;
    }

    /**
     * Getter client
     *
     * @return Client de la location
     */
    public Client getClient() {
        return client;
    }

    /**
     * Getter adresse
     *
     * @return Adresse de départ de la location
     */
    public Adresse getAdrDepart() {
        return adrDepart;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter factures
     *
     * @return Liste de facturations de la location
     */
    public List<Facturation> getFacturations() {
        return facturations;
    }

    /**
     * Setter factures
     *
     * @param facturations Nouvelle liste de facturations de la location
     */
    public void setFacturation(List<Facturation> facturations) {
        this.facturations = facturations;
    }

    /**
     * Affichage des attributs
     *
     * @return Les attributs de l'objet
     */
    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", date=" + Utilitaire.getDateFrench(date) +
                ", kmTotal=" + kmTotal +
                ", client=" + client +
                ", adrDepart=" + adrDepart +
                ", facturations=" + facturations +
                '}';
    }

    public void setAdrDepart(Adresse adrDepart) {
        this.adrDepart= adrDepart;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public static class LocationBuilder {
        /**
         * Identifiant de la location
         */
        private int id;
        /**
         * Date de la location
         */
        private LocalDate date;
        /**
         * Kilomètres total parcouru
         */
        private int kmTotal;
        /**
         * Client de la location
         */
        private Client client;
        /**
         * Adresse de départ de la location
         */
        private Adresse adrDepart;

        public LocationBuilder setId(int id) {
            this.id = id;

            return this;
        }

        public LocationBuilder setDate(LocalDate date) {
            this.date = date;

            return this;
        }

        public LocationBuilder setKmTotal(int kmTotal) {
            this.kmTotal = kmTotal;

            return this;
        }

        public LocationBuilder setClient(Client client) {
            this.client = client;

            return this;
        }

        public LocationBuilder setAdrDepart(Adresse adrDepart) {
            this.adrDepart = adrDepart;

            return this;
        }

        public Location build(boolean globalCheck) throws Exception {

            if (globalCheck){
                if (client == null || adrDepart == null || date == null || date.isBefore(LocalDate.now()) || kmTotal < 0) {
                    throw new Exception("Informations de construction incomplètes");
                }
            }

            return new Location(this);
        }
    }
}
