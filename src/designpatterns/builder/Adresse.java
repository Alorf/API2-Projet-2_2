package designpatterns.builder;

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

    private Adresse(AdresseBuilder ab) {
        this.id = ab.id;
        this.cp = ab.cp;
        this.localite = ab.localite;
        this.rue = ab.rue;
        this.num = ab.num;
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
     * Getter localité
     * @return localité de l'adresse
     */
    public String getLocalite() {
        return localite;
    }

    /**
     * Getter rue
     * @return Rue de l'adresse
     */
    public String getRue() {
        return rue;
    }

    /**
     * Getter num
     * @return Numéro de porte de l'adresse
     */
    public String getNum() {
        return num;
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

    /**
     * Affichage des attributs
     * @return Les attributs de l'objet
     */
    @Override
    public String toString() {
        return "Adresse{" +
                "id=" + id +
                ", cp=" + cp +
                ", localite='" + localite + '\'' +
                ", rue='" + rue + '\'' +
                ", num='" + num + '\'' +
                '}';
    }

    public static class AdresseBuilder{
        /**
         * Identifiant de l'adresse
         */
        protected int id;
        /**
         * Code postale de l'adresse
         */
        protected int cp;
        /**
         * Localité de l'adresse
         */
        protected String localite;
        /**
         * Rue de l'adresse
         */
        protected String rue;
        /**
         * Numéro de porte de l'adresse
         */
        protected String num;

        public AdresseBuilder setId(int id) {
            this.id = id;

            return this;
        }

        public AdresseBuilder setCp(int cp) {
            this.cp = cp;

            return this;
        }

        public AdresseBuilder setLocalite(String localite) {
            this.localite = localite;

            return this;
        }

        public AdresseBuilder setRue(String rue) {
            this.rue = rue;

            return this;
        }

        public AdresseBuilder setNum(String num) {
            this.num = num;

            return this;
        }

        public Adresse build() throws Exception{
            if (false){
                //todo : faire le if
                throw new Exception("Informations de construction incomplètes");
            }

            return new Adresse(this);
        }
    }
}
