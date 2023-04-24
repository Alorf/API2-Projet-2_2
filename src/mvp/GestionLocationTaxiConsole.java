package mvp;



import locationTaxi.Adresse;
import locationTaxi.Client;
import locationTaxi.Location;
import locationTaxi.Taxi;
import mvp.model.DAO;
import mvp.model.adresse.AdresseModelDB;
import mvp.model.client.ClientModelHyb;
import mvp.model.location.LocationModelDB;
import mvp.model.taxi.TaxiModelDB;
import mvp.presenter.AdressePresenter;
import mvp.presenter.ClientPresenter;
import mvp.presenter.LocationPresenter;
import mvp.presenter.TaxiPresenter;
import mvp.view.adresse.AdresseViewConsole;
import mvp.view.adresse.AdresseViewInterface;
import mvp.view.client.ClientViewConsole;
import mvp.view.client.ClientViewInterface;
import mvp.view.location.LocationViewConsole;
import mvp.view.location.LocationViewInterface;
import mvp.view.taxi.TaxiViewConsole;
import mvp.view.taxi.TaxiViewInterface;

import java.util.Arrays;
import java.util.List;

public class GestionLocationTaxiConsole {

    private DAO<Adresse> adresseModel;
    private DAO<Client> clientModel;
    private DAO<Location> locationModel;
    private DAO<Taxi> taxiModel;

    private AdressePresenter adressePresenter;
    private ClientPresenter clientPresenter;
    private LocationPresenter locationPresenter;
    private TaxiPresenter taxiPresenter;

    private AdresseViewInterface adresseViewInterface;
    private ClientViewInterface clientViewInterface;
    private LocationViewInterface locationViewInterface;
    private TaxiViewInterface taxiViewInterface;

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

        locationPresenter.setAdressePresenter(adressePresenter);
        locationPresenter.setClientPresenter(clientPresenter);
        locationPresenter.setTaxiPresenter(taxiPresenter);

        List<String> loptions = Arrays.asList("Adresse", "Client", "Taxi", "Location", "Quitter");
        do {
            int ch = Utilitaire.choixListe(loptions);
            switch (ch) {
                case 1:
                    adressePresenter.start();
                    break;
                case 2:
                    clientPresenter.start();
                    break;
                case 3:
                    taxiPresenter.start();
                    break;
                case 4: locationPresenter.start();
                    break;
                case 5:
                    System.exit(0);
            }
        } while (true);
    }

    public static void main(String[] args) {
        GestionLocationTaxiConsole gt = new GestionLocationTaxiConsole();

        gt.gestion();
    }
}
