package locationTaxi;

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

    /**
     * Constructeur paramétré de locations du client
     * @param id Identifiant unique du client
     * @param mail Mail unique du client
     * @param nom Nom du client
     * @param prenom Prénom du client
     * @param tel Numéro de téléphone du client
     */
    public Client(int id, String mail, String nom, String prenom, String tel) {
        this.id = id;
        this.mail = mail;
        this.nom = nom;
        this.prenom = prenom;
        this.tel = tel;
    }

    /**
     * Getter identifiant
     * @return Identifiant du client
     */
    public int getId() {
        return id;
    }

    /**
     * Setter id
     * @param id Nouvel identifiant du client
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter mail
     * @return Mail du client
     */
    public String getMail() {
        return mail;
    }

    /**
     * Setter mail
     * @param mail Nouveau mail du client
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * Getter nom
     * @return Nom du client
     */
    public String getNom() {
        return nom;
    }

    /**
     * Setter nom
     * @param nom Nouveau nom du client
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Getter prénom
     * @return Prénom du client
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Setter prénom
     * @param prenom Nouveau prénom du client
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Getter numéro de téléphone
     * @return Numéro de téléphone du client
     */
    public String getTel() {
        return tel;
    }

    /**
     * Setter numéro de téléphone
     * @param tel Nouveau numéro de téléphone du client
     */
    public void setTel(String tel) {
        this.tel = tel;
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

        for(Location loc : locations){
            if (loc.getDate().isAfter(d1) && loc.getDate().isBefore(d2)){
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
}
