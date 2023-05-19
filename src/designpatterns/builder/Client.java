package designpatterns.builder;

import java.time.LocalDate;
import java.util.*;

/**
 * Classe Client de la société de Taxi
 * @Author Lorfèvre Arthur
 * @Version 1.0
 */
public class Client {
    /**
     * Identifiant unique du client
     */
    private int id;
    /**
     * Mail unique du client
     */
    private String mail;
    /**
     * Nom du client
     */
    private String nom;
    /**
     * Prénom du client
     */
    private String prenom;
    /**
     * Numéro de téléphone du client
     */
    private String tel;

    /**
     * Liste des locations du client
     */
    private List<Location> locations = new ArrayList<>();

    private Client(ClientBuilder cb) {
        this.id = cb.id;
        this.mail = cb.mail;
        this.nom = cb.nom;
        this.prenom = cb.prenom;
        this.tel = cb.tel;
    }

    /**
     * Getter identifiant
     * @return Identifiant du client
     */
    public int getId() {
        return id;
    }

    /**
     * Getter mail
     * @return Mail du client
     */
    public String getMail() {
        return mail;
    }

    /**
     * Getter nom
     * @return Nom du client
     */
    public String getNom() {
        return nom;
    }

    /**
     * Getter prénom
     * @return Prénom du client
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Getter numéro de téléphone
     * @return Numéro de téléphone du client
     */
    public String getTel() {
        return tel;
    }

    /**
     * Getter liste de locations
     * @return Liste des locations du client
     */
    public List<Location> getLocations() {
        return locations;
    }

    /**
     * Setter liste de locations
     * @param locations Nouvelle liste de locations du client
     */
    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    /**
     * Égalité de deux clients, basée sur le mail
     * @param o Autre objet
     * @return Booléen confirmant l'égalité ou non de la comparaison
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(mail, client.mail);
    }

    /**
     * Calcul du hashcode du client basé sur le mail
     * @return La valeur du hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(mail);
    }

    /**
     * Affichage des attributs
     * @return Les attributs de l'objet
     */
    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", mail='" + mail + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", tel='" + tel + '\'' +
                '}';
    }

    public Set<Taxi> taxiUtiliseSansDoublon(){

        Set<Taxi> lt = new HashSet<>();
        for (Location loc : locations){
            for (Facturation fac : loc.getFacturations()){
                //if (!lt.contains(fac.getVehicule())){
                    lt.add(fac.getVehicule());
                //}
            }
        }

        return lt;
    }

    public List<Location> locationEntreDeuxDates(LocalDate d1, LocalDate d2){
        List<Location> ll = new ArrayList<>();

        LocalDate dmax = d1.isAfter(d2) ? d1 : d2;
        LocalDate dmin = d1.isBefore(d2) ? d1 : d2;

        for(Location loc : locations){
            if (loc.getDate().isAfter(dmin.minusDays(1)) && loc.getDate().isBefore(dmax.plusDays(1))){
                ll.add(loc);
            }
        }

        return ll;
    }

    public Set<Adresse> adresseLocationSansDoublon(){
        Set<Adresse> la = new HashSet<>();
        for (Location loc : locations){
            //if (!la.contains(loc.getAdrDepart())){
                la.add(loc.getAdrDepart());
            //}
        }

        return la;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static class ClientBuilder{
        /**
         * Identifiant unique du client
         */
        protected int id;
        /**
         * Mail unique du client
         */
        protected String mail;
        /**
         * Nom du client
         */
        protected String nom;
        /**
         * Prénom du client
         */
        protected String prenom;
        /**
         * Numéro de téléphone du client
         */
        protected String tel;

        public ClientBuilder setId(int id) {
            this.id = id;

            return this;
        }

        public ClientBuilder setMail(String mail) {
            this.mail = mail;

            return this;
        }

        public ClientBuilder setNom(String nom) {
            this.nom = nom;

            return this;
        }

        public ClientBuilder setPrenom(String prenom) {
            this.prenom = prenom;

            return this;
        }

        public ClientBuilder setTel(String tel) {
            this.tel = tel;

            return this;
        }

        public Client build() throws Exception{
            if (false){
                throw new Exception("Informations de construction incomplètes");
            }

            return new Client(this);
        }
    }
}
