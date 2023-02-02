package locationTaxi;

public class Location {
    private int id;
    private String date;
    private int kmTotal;
    private Client client;
    private Adresse adrDepart;
    private Facturation facturation;

    public Location(int id, String date, int kmTotal, Client client, Adresse adrDepart, Facturation facturation) {
        this.id = id;
        this.date = date;
        this.kmTotal = kmTotal;
        this.client = client;
        this.adrDepart = adrDepart;
        this.facturation = facturation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getKmTotal() {
        return kmTotal;
    }

    public void setKmTotal(int kmTotal) {
        this.kmTotal = kmTotal;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Adresse getAdrDepart() {
        return adrDepart;
    }

    public void setAdrDepart(Adresse adrDepart) {
        this.adrDepart = adrDepart;
    }

    public Facturation getFacturation() {
        return facturation;
    }

    public void setFacturation(Facturation facturation) {
        this.facturation = facturation;
    }
}
