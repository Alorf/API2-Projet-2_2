package mvp;


import designpatterns.builder.Adresse;
import designpatterns.builder.Client;
import designpatterns.builder.Location;
import designpatterns.builder.Taxi;
import mvp.model.DAO;
import mvp.model.adresse.AdresseModelDB;
import mvp.model.client.ClientModelDB;
import mvp.model.client.ClientModelHyb;
import mvp.model.location.LocationModelDB;
import mvp.model.taxi.TaxiModelDB;
import mvp.presenter.*;
import mvp.presenter.adresse.AdressePresenter;
import mvp.presenter.client.ClientPresenter;
import mvp.presenter.location.LocationPresenter;
import mvp.presenter.taxi.TaxiPresenter;
import mvp.view.ViewInterface;
import mvp.view.adresse.AdresseViewConsole;
import mvp.view.client.ClientViewConsole;
import mvp.view.location.LocationViewConsole;
import mvp.view.taxi.TaxiViewConsole;
import utilitaire.Utilitaire;

import java.util.Arrays;
import java.util.List;

public class GestionLocationTaxiConsole {

    private DAO<Adresse> adresseModel;
    private DAO<Client> clientModel;
    private DAO<Location> locationModel;
    private DAO<Taxi> taxiModel;

    private Presenter<Adresse> adressePresenter;
    private Presenter<Client> clientPresenter;
    private Presenter<Location> locationPresenter;
    private Presenter<Taxi> taxiPresenter;

    private ViewInterface<Adresse> adresseViewInterface;
    private ViewInterface<Client> clientViewInterface;
    private ViewInterface<Location> locationViewInterface;
    private ViewInterface<Taxi> taxiViewInterface;

    public void gestion() {
        adresseModel = new AdresseModelDB();
        adresseViewInterface = new AdresseViewConsole();
        adressePresenter = new AdressePresenter(adresseModel, adresseViewInterface);

        clientModel = new ClientModelHyb();
        clientViewInterface = new ClientViewConsole();
        clientPresenter = new ClientPresenter(clientModel, clientViewInterface);

        taxiModel = new TaxiModelDB();
        taxiViewInterface = new TaxiViewConsole();
        taxiPresenter = new TaxiPresenter(taxiModel, taxiViewInterface);

        locationModel = new LocationModelDB();
        locationViewInterface = new LocationViewConsole();
        locationPresenter = new LocationPresenter(locationModel, locationViewInterface);

        ((LocationPresenter) locationPresenter).setClientPresenter((ClientPresenter) clientPresenter);
        ((LocationPresenter) locationPresenter).setAdressePresenter(adressePresenter);
        ((LocationPresenter) locationPresenter).setTaxiPresenter((TaxiPresenter) taxiPresenter);

        List<String> loptions = Arrays.asList("Adresse", "Client", "Taxi", "Location", "Quitter");
        do {
            int ch = Utilitaire.choixListe(loptions);
            switch (ch) {
                case 1 -> adressePresenter.start();
                case 2 -> clientPresenter.start();
                case 3 -> taxiPresenter.start();
                case 4 -> locationPresenter.start();
                default -> System.exit(0);
            }
        } while (true);
    }

    public static void main(String[] args) {
        GestionLocationTaxiConsole gt = new GestionLocationTaxiConsole();

        gt.gestion();
    }
}
