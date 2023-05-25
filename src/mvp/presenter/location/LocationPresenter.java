package mvp.presenter.location;

import designpatterns.builder.Adresse;
import designpatterns.builder.Client;
import designpatterns.builder.Location;
import designpatterns.builder.Taxi;
import mvp.model.DAO;
import mvp.model.location.LocationSpecial;
import mvp.presenter.Presenter;
import mvp.presenter.taxi.TaxiPresenter;
import mvp.presenter.client.ClientPresenter;
import mvp.view.ViewInterface;

import java.math.BigDecimal;

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
        /*
         *  Fonctionnement différent du Presenter générique
         *  Le super.add() ne peut pas être appelé car j'ai besoin d'une réponse positive pour ajouter une facturation
         *  Il faut que la méthode add du presenter retourne un booléen et là on pourrait vérifier si l'ajout c'est fait correctement
         */

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

        majListe();
    }

    @Override
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

    @Override
    public void removeFacturation(int idLoc, int idVehicule) {
        boolean ok = ((LocationSpecial) model).removeFacturation(idLoc, idVehicule);

        if (ok) {
            view.affMsg("Elément effacée");
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
    public void prixTotalLocation(Location loc) {
        BigDecimal total = ((LocationSpecial) model).prixTotalLocation(loc);

        if (total == null) {
            view.affMsg("Il n'y à pas de total à cette location");
        } else {
            view.affMsg("Montant total de la location : " + total + "€");
        }
    }
}