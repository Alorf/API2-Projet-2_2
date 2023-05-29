package mvp.presenter.client;

import designpatterns.builder.*;
import mvp.model.DAO;
import mvp.model.client.ClientSpecial;
import mvp.presenter.Presenter;
import mvp.view.ViewInterface;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class ClientPresenter extends Presenter<Client> implements SpecialClientPresenter {

    public ClientPresenter(DAO<Client> model, ViewInterface<Client> view) {
        super(model, view);
    }

    @Override
    public void taxiUtiliseSansDoublon(Client client) {
        Set<Taxi> lt = ((ClientSpecial) model).taxiUtiliseSansDoublon(client);

        if (lt == null || lt.isEmpty()) {
            view.affMsg("Aucun taxi commandé par ce client");
        } else {
            view.affListe(lt);
        }
    }

    @Override
    public void locationEntreDeuxDates(Client client, LocalDate d1, LocalDate d2) {

        if (client.getLocations() == null || client.getLocations().isEmpty()) {
            view.affMsg("Aucune location pour ces deux dates pour ce client");
        } else {
            List<Location> ll = ((ClientSpecial) model).locationEntreDeuxDates(client, d1, d2);
            view.affListe(ll);
        }
    }

    @Override
    public void adresseLocationSansDoublon(Client client) {
        Set<Adresse> la = ((ClientSpecial) model).adresseLocationSansDoublon(client);

        if (la == null || la.isEmpty()) {
            view.affMsg("Aucune adresse de location pour ce client");
        } else {
            view.affListe(la);
        }

    }

    @Override
    public void facturations(Client client) {
        //On récupère les factures de location du client
        List<Facturation> lf = ((ClientSpecial) model).facturations(client);

        if (lf == null || lf.isEmpty()) {
            view.affMsg("Aucune facture de location pour ce client");
        } else {
            view.affListe(lf);
        }
    }

    @Override
    public void locations(Client client) {
        //On récupère les locations du client
        List<Location> ll = ((ClientSpecial) model).locations(client);

        if (ll == null || ll.isEmpty()) {
            view.affMsg("Aucune location pour ce client");
        } else {
            view.affListe(ll);
        }
    }

    @Override
    public void nombreLocation(Client client) {
        if (client.getLocations() == null || client.getLocations().isEmpty()) {
            view.affMsg("Aucune location pour ce client");

        } else {
            int rep = ((ClientSpecial) model).nombreLocation(client);
            view.affMsg(client.getNom() + " à " + rep + " locations");

        }
    }

    @Override
    public void prixTotalLocs(Client client) {
        if (client.getLocations() == null || client.getLocations().isEmpty()) {
            view.affMsg("Aucune location pour ce client");

        } else {
            BigDecimal rep = ((ClientSpecial) model).prixTotalLocs(client);
            if (rep == null) {
                view.affMsg("Pas de cout total disponnible");
            } else {
                view.affMsg("Total des factures de " + client.getNom() + " : " + rep + "€");
            }
        }
    }
}