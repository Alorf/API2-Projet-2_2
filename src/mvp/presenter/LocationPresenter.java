package mvp.presenter;

import designpatterns.builder.Adresse;
import designpatterns.builder.Client;
import designpatterns.builder.Location;
import designpatterns.builder.Taxi;
import mvp.model.DAO;
import mvp.model.location.LocationSpecial;
import mvp.view.location.LocationViewInterface;

import java.math.BigDecimal;
import java.util.List;

public class LocationPresenter {
    private DAO<Location> model;
    private DAO<Client> clientModel;
    private DAO<Adresse> adresseModel;

    private LocationViewInterface view;
    private ClientPresenter clientPresenter;
    private AdressePresenter adressePresenter;
    private TaxiPresenter taxiPresenter;

    public void setClientPresenter(ClientPresenter clientPresenter){
        this.clientPresenter = clientPresenter;
    }

    public void setAdressePresenter(AdressePresenter adressePresenter){
        this.adressePresenter = adressePresenter;
    }

    public void setTaxiPresenter(TaxiPresenter taxiPresenter){
        this.taxiPresenter = taxiPresenter;
    }

    public LocationPresenter(DAO<Location> model, LocationViewInterface view) {
        this.model = model;
        this.view = view;
        this.view.setPresenter(this);
    }

    public void start() {
        List<Location> locations = model.getAll();
        view.setListDatas(locations);
        view.menu();
    }

    public void addLocation(Location location) {
        Location loc;
        Client client = clientPresenter.selectionner();
        Adresse adresse = adressePresenter.selectionner();

        location.setAdrDepart(adresse);
        location.setClient(client);

        loc = model.add(location);

        if (loc == null) {
            view.affMsg("Erreur lors de la création le la location");
        } else {
            view.affMsg("Création de : " + loc);

            //Ajout de la facture
            addFacturation(loc);
        }

        List<Location> locations = model.getAll();
        view.setListDatas(locations);

    }

    public Location readLocation(int idRech) {
        Location location = model.read(idRech);

        if (location == null) {
            view.affMsg("Location introuvable");
        }

        return location;
    }

    public void updateLocation(Location location) {
        boolean ok = model.update(location);

        if (ok) {
            view.affMsg("Location modifiée");
        } else {
            view.affMsg("Location non modifiée, erreur");
        }

        List<Location> locations = model.getAll();
        view.setListDatas(locations);
    }

    public void removeLocation(int idAdr) {
        boolean ok = model.remove(idAdr);

        if (ok) {
            view.affMsg("Location effacée");
        } else {
            view.affMsg("Location non effacée, erreur");
        }

        List<Location> locations = model.getAll();
        view.setListDatas(locations);
    }

    public void addFacturation(Location loc){
        loc = readLocation(loc.getId());
        Taxi taxi = taxiPresenter.selectionner(loc.getFacturations());

        if (taxi == null){
            view.affMsg("Pas de taxi disponnible");
            return;
        }

        boolean ok = ((LocationSpecial) model).addFacturation(loc, taxi);

        if (ok){
            view.affMsg("Facturation ajoutée");
        }else{
            view.affMsg("Erreur lors de l'ajout de la facturation");
        }
    }

    public Client choixClient(){
        //Utilisée lors de l'update
        Client client = clientPresenter.selectionner();

        return client;
    }

    public Adresse choixAdresse(){
        //Utilisée lors de l'update
        Adresse adresse = adressePresenter.selectionner();

        return adresse;
    }

    public void prixTotalLocation(Location loc) {
        BigDecimal total = ((LocationSpecial) model).prixTotalLocation(loc);

        if (total == null ){
            view.affMsg("Il n'y à pas de total à cette location");
        }else{
            view.affMsg("Montant total de la location : " + total + "€");
        }
    }
}