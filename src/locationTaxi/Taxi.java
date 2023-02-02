package locationTaxi;

import java.util.Objects;

public class Taxi {
    private int id;
    private String immatriculation;
    // Unique
    private String carburant;
    private double prixKm;

    public Taxi(int id, String immatriculation, String carburant, double prixKm) {
        this.id = id;
        this.immatriculation = immatriculation;
        this.carburant = carburant;
        this.prixKm = prixKm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public String getCarburant() {
        return carburant;
    }

    public void setCarburant(String carburant) {
        this.carburant = carburant;
    }

    public double getPrixKm() {
        return prixKm;
    }

    public void setPrixKm(double prixKm) {
        this.prixKm = prixKm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Taxi taxi = (Taxi) o;
        return Objects.equals(immatriculation, taxi.immatriculation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(immatriculation);
    }
}
