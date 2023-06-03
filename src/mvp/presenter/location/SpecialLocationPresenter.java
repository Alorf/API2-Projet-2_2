package mvp.presenter.location;

import designpatterns.builder.Adresse;
import designpatterns.builder.Client;
import designpatterns.builder.Facturation;
import designpatterns.builder.Location;
import mvp.presenter.Presenter;
import mvp.presenter.client.ClientPresenter;
import mvp.presenter.taxi.TaxiPresenter;

import java.time.LocalDate;


public interface SpecialLocationPresenter {

    void setClientPresenter(ClientPresenter clientPresenter);

    void setAdressePresenter(Presenter<Adresse> adressePresenter);

    void setTaxiPresenter(TaxiPresenter taxiPresenter);

    void addFacturation(Location location);

    void facturations(Location loc);

    void removeFacturation(Location location);

    Client choixClient();

    Adresse choixAdresse();

    Facturation choixFacturation(Location location);

    void prixTotalLocation(Location loc);

    void locationsDate(LocalDate date);
}