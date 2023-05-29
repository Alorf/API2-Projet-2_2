package designpatterns.builder;

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

    private Facturation(FacturationBuilder fb) {
        this.cout = fb.cout;
        this.vehicule = fb.vehicule;
    }

    /**
     * Getter cout
     * @return Cout de facturation de la course
     */
    public double getCout() {
        return cout;
    }


    /**
     * Getter taxi
     * @return Vehicule utilisé pour la facturation de la course
     */
    public Taxi getVehicule() {
        return vehicule;
    }

    @Override
    public String toString() {
        return "Facturation{" +
                "cout=" + cout +
                ", vehicule=" + vehicule +
                '}';
    }

    public static class FacturationBuilder{
        /**
         * Cout de facturation de la course
         */
        protected double cout;
        /**
         * Taxi utilisé pour la facturation de la course
         */
        protected Taxi vehicule;

        public FacturationBuilder setCout(double cout) {
            this.cout = cout;

            return this;
        }

        public FacturationBuilder setVehicule(Taxi vehicule) {
            this.vehicule = vehicule;

            return this;
        }

        public Facturation build() throws Exception{
            if (false){
                //Vérification non demandée mais je garde le code si jamais
                throw new Exception("Informations de construction incomplètes");
            }

            return new Facturation(this);
        }
    }
}
