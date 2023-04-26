package mvp.model.client;


import locationTaxi.metier.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface ClientSpecial {

    Set<Taxi> taxiUtiliseSansDoublon(Client client);

    List<Location> locationEntreDeuxDates(Client client, LocalDate d1, LocalDate d2);

    Set<Adresse> adresseLocationSansDoublon(Client client);

    List<Location> locations(Client client);

    List<Facturation> facturations(Client client);

    int nombreLocation(Client client);
}
