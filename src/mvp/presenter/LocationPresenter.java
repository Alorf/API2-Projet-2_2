package mvp.presenter;

import designpatterns.builder.Adresse;
import designpatterns.builder.Client;
import designpatterns.builder.Location;
import designpatterns.builder.Taxi;
import mvp.model.DAO;
import mvp.model.location.LocationSpecial;
import mvp.view.ViewInterface;

import java.math.BigDecimal;
import java.util.List;

public class LocationPresenter extends Presenter<Location> {
    private ClientPresenter clientPresenter;
    private Presenter<Adresse> adressePresenter;
    private TaxiPresenter taxiPresenter;

    public LocationPresenter(DAO<Location> model, ViewInterface<Location> view) {
        super(model, view);
    }

    public void setClientPresenter(ClientPresenter clientPresenter) {
        this.clientPresenter = clientPresenter;
    }

    public void setAdressePresenter(Presenter<Adresse> adressePresenter) {
        this.adressePresenter = adressePresenter;
    }

    public void setTaxiPresenter(TaxiPresenter taxiPresenter) {
        this.taxiPresenter = taxiPresenter;
    }

    public void addFacturation(Location loc) {
        loc = read(loc.getId());
        Taxi taxi = taxiPresenter.selectionner(loc.getFacturations());

        if (taxi == null) {
            view.affMsg("Pas de taxi disponnible");
            return;
        }

        boolean ok = ((LocationSpecial) model).addFacturation(loc, taxi);

        if (ok) {
            view.affMsg("Facturation ajoutée");
        } else {
            view.affMsg("Erreur lors de l'ajout de la facturation");
        }
    }

    public void removeFacturation(int idLoc, int idVehicule) {
        boolean ok = ((LocationSpecial) model).removeFacturation(idLoc, idVehicule);

        if (ok) {
            view.affMsg("Elément effacée");
        } else {
            view.affMsg("Elément non effacé, erreur");
        }
    }

    public Client choixClient() {
        //Utilisée lors de l'update
        Client client = clientPresenter.selectionner();

        return client;
    }

    public Adresse choixAdresse() {
        //Utilisée lors de l'update
        Adresse adresse = adressePresenter.selectionner();

        return adresse;
    }

    public void prixTotalLocation(Location loc) {
        BigDecimal total = ((LocationSpecial) model).prixTotalLocation(loc);

        if (total == null) {
            view.affMsg("Il n'y à pas de total à cette location");
        } else {
            view.affMsg("Montant total de la location : " + total + "€");
        }
    }
}