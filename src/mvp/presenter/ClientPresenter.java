package mvp.presenter;

import designpatterns.builder.*;
import mvp.model.DAO;
import mvp.model.client.ClientSpecial;
import mvp.view.client.ClientViewInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class ClientPresenter {
    private DAO<Client> model;

    private static final Logger logger = LogManager.getLogger(ClientPresenter.class);

    private ClientViewInterface view;

    public ClientPresenter(DAO<Client> model, ClientViewInterface view) {
        this.model = model;
        this.view = view;
        this.view.setPresenter(this);
    }

    public void start() {
        List<Client> clients = model.getAll();
        view.setListDatas(clients);
    }

    public void addClient(Client client) {
        Client cl = model.add(client);
        if (cl == null) {
            view.affMsg("Erreur lors de la création");
        } else {
            view.affMsg("Création de : " + cl);
        }

        List<Client> clients = model.getAll();
        view.setListDatas(clients);

    }

    public Client readClient(int idRech) {
        Client client = model.read(idRech);

        return client;
    }

    public void updateClient(Client client) {
        boolean ok = model.update(client);

        if (ok) {
            System.out.println("Client modifié");
        } else {
            System.out.println("Client non modifié, erreur");
        }
    }

    public void removeClient(int idCli) {
        boolean ok = model.remove(idCli);

        if (ok) {
            view.affMsg("Client effacé");
        } else {
            view.affMsg("Client non effacé");
        }

        List<Client> clients = model.getAll();
        view.setListDatas(clients);
    }

    public void taxiUtiliseSansDoublon(Client client) {
        Set<Taxi> lt = ((ClientSpecial) model).taxiUtiliseSansDoublon(client);

        if (lt == null || lt.isEmpty()) {
            view.affMsg("Aucun taxi commandé par ce client");
        } else {
            view.affListe(lt);
        }
    }

    public void locationEntreDeuxDates(Client client, LocalDate d1, LocalDate d2) {

        if (model.getAll().isEmpty()) {
            view.affMsg("Aucune location pour ces deux dates pour ce client");
        } else {
            List<Location> ll = ((ClientSpecial) model).locationEntreDeuxDates(client, d1, d2);
            view.affListe(ll);
        }
    }

    public void adresseLocationSansDoublon(Client client) {
        Set<Adresse> la = ((ClientSpecial) model).adresseLocationSansDoublon(client);

        if (la == null || la.isEmpty()) {
            view.affMsg("Aucune adresse de location pour ce client");
        } else {
            view.affListe(la);
        }

    }

    public void facturations(Client client) {
        List<Facturation> lf = ((ClientSpecial) model).facturations(client);

        if (lf == null || lf.isEmpty()) {
            view.affMsg("Aucune facture de location pour ce client");
        } else {
            view.affListe(lf);
        }
    }

    public void locations(Client client) {
        List<Location> ll = ((ClientSpecial) model).locations(client);

        if (ll == null || ll.isEmpty()) {
            view.affMsg("Aucune facture de location pour ce client");
        } else {
            view.affListe(ll);
        }
    }

    public Client selectionner() {
        logger.info("Appel de la sélection");
        Client client = view.selectionner(model.getAll());

        return client;
    }

    public void nombreLocation(Client client) {
        if (client.getLocations() == null || client.getLocations().isEmpty()) {
            view.affMsg("Aucune location pour ce client");

        } else {
            int rep = ((ClientSpecial) model).nombreLocation(client);
            view.affMsg(client.getNom() + " à " + rep + " locations");

        }
    }

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