package locationTaxi;

import java.util.Objects;

/**
 * Classe Adresse de départ enregistrée de la société de Taxi
 * @Author Lorfèvre Arthur
 * @Version 1.0
 */
public class Adresse {
    /**
     * Identifiant de l'adresse
     */
    private int id;
    /**
     * Code postale de l'adresse
     */
    private int cp;
    /**
     * Localité de l'adresse
     */
    private String localite;
    /**
     * Rue de l'adresse
     */
    private String rue;
    /**
     * Numéro de porte de l'adresse
     */
    private String num;

    /**
     * Constructeur de l'adresse
     * @param id Identifiant de l'adresse
     * @param cp Code postal de l'adresse
     * @param localite Localité de l'adresse
     * @param rue Rue de l'adresse
     * @param num Numéro de porte de l'adresse
     */
    public Adresse(int id, int cp, String localite, String rue, String num) {
        this.id = id;
        this.cp = cp;
        this.localite = localite;
        this.rue = rue;
        this.num = num;
    }

    /**
     * Getter id
     * @return Identifiant de l'adresse
     */
    public int getId() {
        return id;
    }

    /**
     * Setter id
     * @param id Nouvel identifiant de l'adresse
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter code postal
     * @return Code postal de l'adresse
     */
    public int getCp() {
        return cp;
    }

    /**
     * Setter code postal
     * @param cp Nouveau code postal de l'adresse
     */
    public void setCp(int cp) {
        this.cp = cp;
    }

    /**
     * Getter localité
     * @return localité de l'adresse
     */
    public String getLocalite() {
        return localite;
    }

    /**
     * Setter localité
     * @param localite Nouvelle localité de l'adresse
     */
    public void setLocalite(String localite) {
        this.localite = localite;
    }

    /**
     * Getter rue
     * @return Rue de l'adresse
     */
    public String getRue() {
        return rue;
    }

    /**
     * Setter num
     * @param rue Nouvelle rue de l'adresse
     */
    public void setRue(String rue) {
        this.rue = rue;
    }

    /**
     * Getter num
     * @return Numéro de porte de l'adresse
     */
    public String getNum() {
        return num;
    }

    /**
     * Setter num
     * @param num Nouveau numéro de porte de l'adresse
     */
    public void setNum(String num) {
        this.num = num;
    }

    /**
     * Égalité entre deux adresses, basée sur cp (code postal), localité, rue, numéro
     * @param o Autre objet
     * @return Booléen confirmant l'égalité ou non de la comparaison
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Adresse adresse = (Adresse) o;
        return cp == adresse.cp && Objects.equals(localite, adresse.localite) && Objects.equals(rue, adresse.rue) && Objects.equals(num, adresse.num);
    }

    /**
     * Calcul du hashcode de l'adresse basé sur le cp (code postal), localité, rue, numéro
     * @return La valeur du hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(cp, localite, rue, num);
    }
}
