package mvp.presenter;

import locationTaxi.*;
import mvp.model.client.ClientSpecial;
import mvp.model.client.DAOClient;
import mvp.view.client.ClientViewInterface;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class ClientPresenter {
    private DAOClient model;

    private ClientViewInterface view;

    public ClientPresenter(DAOClient model, ClientViewInterface view) {
        this.model = model;
        this.view = view;
        this.view.setPresenter(this);
    }

    public void start() {
        List<Client> clients = model.getClients();
        view.setListDatas(clients);
    }

    public void addClient(Client client) {
        Client cl = model.addClient(client);
        if (cl == null) {
            view.affMsg("Erreur lors de la création");
        } else {
            view.affMsg("Création de : " + cl);
        }

        List<Client> clients = model.getClients();
        view.setListDatas(clients);

    }

    public Client readClient(int idRech) {
        Client client = model.readClient(idRech);
        if (client == null) {
            System.out.println("Client introuvable");
            return null;
        } else {
            System.out.println("Client trouvé");
        }

        return client;
    }

    public void updateClient(Client client) {
        boolean ok = model.updateClient(client);

        if (ok) {
            System.out.println("Client modifié");
        } else {
            System.out.println("Client non modifié, erreur");
        }
    }

    public void removeClient(int idCli) {
        boolean ok = model.removeClient(idCli);

        if (ok) {
            view.affMsg("Client effacé");
        } else {
            view.affMsg("Client non effacé");
        }

        List<Client> clients = model.getClients();
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
        List<Location> ll = ((ClientSpecial) model).locationEntreDeuxDates(client, d1, d2);

        if (ll == null || ll.isEmpty()) {
            view.affMsg("Aucune location pour ces deux dates pour ce client");
        } else {
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

    public void facturations(Client client){
        List<Facturation> lf = ((ClientSpecial) model).facturations(client);

        if (lf == null || lf.isEmpty()) {
            view.affMsg("Aucune facture de location pour ce client");
        } else {
            view.affListe(lf);
        }
    }

    public void locations(Client client){
        List<Location> ll = ((ClientSpecial) model).locations(client);

        if (ll == null || ll.isEmpty()) {
            view.affMsg("Aucune facture de location pour ce client");
        } else {
            view.affListe(ll);
        }
    }

    public List<Client> tout(){
        List<Client> lc = model.getClients();

        if (lc == null){
            view.affMsg("Aucun client dans la base de donnée");
        }

        return lc;
    }

    public Client selectionner() {
        Client client = view.selectionner(model.getClients());
        return client;
    }
}