package locationTaxi;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Location de la société de Taxi
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

    /**
     * Constructeur paramétré de la location
     * @param id Identifiant de la location
     * @param date Date de la location
     * @param kmTotal Kilomètres total parcouru
     * @param client Client de la location
     * @param adrDepart Adresse de départ de la location
     */
    public Location(int id, LocalDate date, int kmTotal, Client client, Adresse adrDepart) {
        this.id = id;
        this.date = date;
        this.kmTotal = kmTotal;
        this.client = client;
        this.adrDepart = adrDepart;
    }

    /**
     * Getter identifiant
     * @return Identifiant de la location
     */
    public int getId() {
        return id;
    }

    /**
     * Setter identifiant
     * @param id Nouvel identifiant de la location
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter date
     * @return Date de la location
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Setter date
     * @param date Nouvelle date de location
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Getter kilomètres total
     * @return Kilomètres total parcouru lors de la location
     */
    public int getKmTotal() {
        return kmTotal;
    }

    /**
     * Setter kilomètre total
     * @param kmTotal Nouvelle distance parcourue de la location
     */
    public void setKmTotal(int kmTotal) {
        this.kmTotal = kmTotal;
    }

    /**
     * Getter client
     * @return Client de la location
     */
    public Client getClient() {
        return client;
    }

    /**
     * Setter client
     * @param client Nouveau client de la location
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Getter adresse
     * @return Adresse de départ de la location
     */
    public Adresse getAdrDepart() {
        return adrDepart;
    }

    /**
     * Setter adresse de départ
     * @param adrDepart Nouvelle adresse de départ de la location
     */
    public void setAdrDepart(Adresse adrDepart) {
        this.adrDepart = adrDepart;
    }

    /**
     * Getter factures
     * @return Liste de facturations de la location
     */
    public List<Facturation> getFacturations() {
        return facturations;
    }

    /**
     * Setter factures
     * @param facturations Nouvelle liste de facturations de la location
     */
    public void setFacturation(List<Facturation> facturations) {
        this.facturations = facturations;
    }

    /**
     * Affichage des attributs
     * @return Les attributs de l'objet
     */
    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", date=" + date +
                ", kmTotal=" + kmTotal +
                ", client=" + client +
                ", adrDepart=" + adrDepart +
                ", facturations=" + facturations +
                '}';
    }
}
