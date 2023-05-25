package mvp.presenter.client;

import designpatterns.builder.*;

import java.time.LocalDate;

public interface SpecialClientPresenter {

    void taxiUtiliseSansDoublon(Client client);

    void locationEntreDeuxDates(Client client, LocalDate d1, LocalDate d2);

    void adresseLocationSansDoublon(Client client);

    void facturations(Client client);

    void locations(Client client);

    void nombreLocation(Client client);

    void prixTotalLocs(Client client);
}