package mvp.presenter.location;

import designpatterns.builder.*;
import mvp.model.DAO;
import mvp.model.location.LocationSpecial;
import mvp.presenter.Presenter;
import mvp.presenter.taxi.TaxiPresenter;
import mvp.presenter.client.ClientPresenter;
import mvp.view.ViewInterface;
import mvp.view.location.SpecialLocationViewInterface;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class LocationPresenter extends Presenter<Location> implements SpecialLocationPresenter {
    private ClientPresenter clientPresenter;
    private Presenter<Adresse> adressePresenter;
    private TaxiPresenter taxiPresenter;

    public LocationPresenter(DAO<Location> model, ViewInterface<Location> view) {
        super(model, view);
    }

    @Override
    public void setClientPresenter(ClientPresenter clientPresenter) {
        this.clientPresenter = clientPresenter;
    }

    @Override
    public void setAdressePresenter(Presenter<Adresse> adressePresenter) {
        this.adressePresenter = adressePresenter;
    }

    @Override
    public void setTaxiPresenter(TaxiPresenter taxiPresenter) {
        this.taxiPresenter = taxiPresenter;
    }

    @Override
    public void add(Location location) {

        Client client = clientPresenter.selectionner();
        Adresse adresse = adressePresenter.selectionner();

        location.setAdrDepart(adresse);
        location.setClient(client);

        super.add(location);
    }

    @Override
    public void addFacturation(Location location) {
        location = read(location.getId());
        Taxi taxi = taxiPresenter.selectionner(location.getFacturations());

        if (taxi == null) {
            view.affMsg("Pas de taxi disponnible");
            return;
        }

        boolean ok = ((LocationSpecial) model).addFacturation(location, taxi);

        if (ok) {
            view.affMsg("Facturation ajoutée");
        } else {
            view.affMsg("Erreur lors de l'ajout de la facturation");
        }
    }

    @Override
    public void removeFacturation(Location location) {
        location = read(location.getId());
        if (location.getFacturations() == null || location.getFacturations().isEmpty()) {
            view.affMsg("Aucune facturation à supprimer");
            return;
        }

        Facturation fact = choixFacturation(location);

        boolean ok = ((LocationSpecial) model).removeFacturation(location.getId(), fact.getVehicule().getId());

        if (ok) {
            view.affMsg("Elément effacé : " + fact);
        } else {
            view.affMsg("Elément non effacé, erreur");
        }
    }

    @Override
    public Client choixClient() {
        //Utilisée lors de l'update
        Client client = clientPresenter.selectionner();

        return client;
    }

    @Override
    public Adresse choixAdresse() {
        //Utilisée lors de l'update
        Adresse adresse = adressePresenter.selectionner();

        return adresse;
    }

    @Override
    public Facturation choixFacturation(Location location) {
        //Opération spéciale dans les locations

        List<Facturation> facturations = location.getFacturations();

        if (facturations == null || facturations.isEmpty()) {
            return null;
        }

        Facturation facturation = ((SpecialLocationViewInterface) view).selectionnerFacture(facturations);

        return facturation;
    }

    @Override
    public void prixTotalLocation(Location loc) {
        BigDecimal total = ((LocationSpecial) model).prixTotalLocation(loc);

        if (total == null) {
            view.affMsg("Il n'y à pas de total à cette location");
        } else {
            view.affMsg("Montant total de la location : " + total + "€");
        }

    }

    @Override
    public void locationsDate(LocalDate date) {
        List<Location> locs = ((LocationSpecial) model).locationsDate(date);

        if (locs == null || locs.isEmpty()){
            view.affMsg("Aucune location pour cette date");
        } else{
            view.affListe(locs);
        }
    }

    public void facturations(Location loc) {
        List<Facturation> facturations = ((LocationSpecial) model).getFacturations(loc);

        if (facturations == null || facturations.isEmpty()) {
            view.affMsg("Aucune facturation");
        } else {
            view.affListe(facturations);
        }

    }
}