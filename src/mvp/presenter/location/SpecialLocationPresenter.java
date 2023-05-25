package mvp.presenter.location;

import designpatterns.builder.Adresse;
import designpatterns.builder.Client;
import designpatterns.builder.Location;
import mvp.presenter.Presenter;
import mvp.presenter.client.ClientPresenter;
import mvp.presenter.taxi.TaxiPresenter;


public interface SpecialLocationPresenter {

    void setClientPresenter(ClientPresenter clientPresenter);

    void setAdressePresenter(Presenter<Adresse> adressePresenter);

    void setTaxiPresenter(TaxiPresenter taxiPresenter);

    void addFacturation(Location loc);

    void removeFacturation(int idLoc, int idVehicule);

    Client choixClient();

    Adresse choixAdresse();

    void prixTotalLocation(Location loc);
}