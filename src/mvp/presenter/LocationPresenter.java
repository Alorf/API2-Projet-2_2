package mvp.presenter;

import locationTaxi.Adresse;
import locationTaxi.Client;
import locationTaxi.Location;
import locationTaxi.Taxi;
import mvp.model.adresse.DAOAdresse;
import mvp.model.client.DAOClient;
import mvp.model.location.DAOLocation;
import mvp.model.location.LocationSpecial;
import mvp.view.location.LocationViewInterface;

import java.util.List;

public class LocationPresenter {
    private DAOLocation model;
    private DAOClient clientModel;
    private DAOAdresse adresseModel;

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

    public LocationPresenter(DAOLocation model, LocationViewInterface view) {
        this.model = model;
        this.view = view;
        this.view.setPresenter(this);
    }

    public void start() {
        List<Location> locations = model.getLocations();
        view.setListDatas(locations);
    }

    public void addLocation(Location location) {
        Location loc = new Location();
        Client client = clientPresenter.selectionner();
        Adresse adresse = adressePresenter.selectionner();

        location.setAdrDepart(adresse);
        location.setClient(client);

        loc = model.addLocation(location);

        if (loc == null) {
            view.affMsg("Erreur lors de la création");
        } else {
            view.affMsg("Création de : " + loc);
        }

        List<Location> locations = model.getLocations();
        view.setListDatas(locations);

    }

    public Location readLocation(int idRech) {
        Location location = model.readLocation(idRech);
        if (location == null) {
            System.out.println("Location introuvable");
            return null;
        } else {
            System.out.println("Location trouvée");
        }

        return location;
    }

    public void updateLocation(Location location) {
        boolean ok = model.updateLocation(location);

        if (ok) {
            System.out.println("Location modifiée");
        } else {
            System.out.println("Location non modifiée, erreur");
        }
    }

    public void removeLocation(int idAdr) {
        boolean ok = model.removeLocation(idAdr);

        if (ok) {
            view.affMsg("Location effacée");
        } else {
            view.affMsg("Location non effacée");
        }

        List<Location> locations = model.getLocations();
        view.setListDatas(locations);
    }

    public List<Location> tout(){
        List<Location> lc = model.getLocations();

        if (lc == null){
            view.affMsg("Aucune location dans la base de donnée");
        }

        return lc;
    }

    public void addFacturation(Location loc){
        Taxi taxi = taxiPresenter.selectionner();

        boolean ok = ((LocationSpecial) model).addFacturation(loc, taxi);

        if (ok){
            view.affMsg("Facturation ajoutée");
        }else{
            view.affMsg("Erreur lors de l'ajout de la facturation");
        }
    }
}