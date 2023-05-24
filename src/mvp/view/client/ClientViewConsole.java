package mvp.view.client;

import designpatterns.builder.Client;
import utilitaire.Utilitaire;
import mvp.presenter.ClientPresenter;

import java.time.LocalDate;
import java.util.*;

public class ClientViewConsole implements ClientViewInterface {
    private ClientPresenter presenter;
    private List<Client> lc;

    public ClientViewConsole() {

    }

    @Override
    public void setPresenter(ClientPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setListDatas(List<Client> clients) {
        this.lc = clients;
    }

    @Override
    public void affMsg(String msg) {
        System.out.println("Information : " + msg);

    }

    @Override
    public void affListe(List info) {
        Utilitaire.afficherListe(info);
    }

    @Override
    public void affListe(Set info) {
        Utilitaire.afficherListe(info);
    }

    @Override
    public Client selectionner(List<Client> clients) {
        //lc est vide car aucun client n'est chargé en mémoire
        int choix = Utilitaire.choixListe(clients);

        return clients.get(choix - 1);
    }

    @Override
    public void menu() {

        int choix;

        String[] menu = new String[]{
                "Créer",
                "Rechercher",
                "Modifier",
                "Supprimer",
                "Opérations spéciales",
                "Finir"
        };

        do {
            affListe(lc);


            choix = Utilitaire.choixListe(Arrays.asList(menu));


            switch (choix) {
                case 1 ->
                    //Créer un client
                        creerClient();
                case 2 ->
                    //Rechercher un client
                        rechercherClient();
                case 3 ->
                    //Modifier un client
                        modifierClient();
                case 4 ->
                    //Supprimer un client
                        supprimerClient();
                case 5 ->
                    //Opérations spéciales
                        opSpeciales();
                default ->
                //Fin
                {
                    return;
                }
            }
        } while (true);
    }

    public void creerClient() {
        String mail = Utilitaire.regex("[a-zA-Z.@]+", "Entrez le mail du client : ").toLowerCase();
        String nom = Utilitaire.regex("[a-zA-Z -]+", "Entrez le nom du client : ");
        String prenom = Utilitaire.regex("[a-zA-Z -]+", "Entrez le prénom du client : ");
        String tel = Utilitaire.regex("[0-9/ +]+", "Entrez le numéro de téléphone du client : ");

        try {

            Client client = new Client.ClientBuilder()
                    //.setId(0)
                    .setMail(mail)
                    .setNom(nom)
                    .setPrenom(prenom)
                    .setTel(tel)
                    .build();

            presenter.addClient(client);

        } catch (Exception e) {
            affMsg("Erreur Builder : " + e);
        }

    }

    public void modifierClient() {
        int idRech = Integer.parseInt(Utilitaire.regex("[0-9]+", "Id du client recherché : "));

        Client client = presenter.readClient(idRech);

        if (client == null) {
            return;
        }

        String mail = client.getMail();
        String nom = client.getNom();
        String prenom = client.getPrenom();
        String tel = client.getTel();

        String[] menu = {
                "Mail",
                "Nom",
                "Prénom",
                "Numéro de téléphone",
                "Sortir"
        };

        int choix;

        System.out.println("Que souhaitez vous modifier ?");

        updateLoop:
        do {
            choix = Utilitaire.choixListe(Arrays.asList(menu));


            switch (choix) {

                case 1 -> {
                    //Modifier mail
                    System.out.println("Anciennement : " + mail);
                    mail = Utilitaire.regex("[a-zA-Z.@]+", "Entrez le nouveau mail : ").toLowerCase();
                }
                case 2 -> {
                    //Modifier nom
                    System.out.println("Anciennement : " + nom);

                    nom = Utilitaire.regex("[a-zA-Z -]+", "Entrez le nouveau nom : ");
                }
                case 3 -> {
                    System.out.println("Anciennement : " + prenom);

                    //Modifier prenom
                    prenom = Utilitaire.regex("[a-zA-Z -]+", "Entrez le nouveau prénom : ");
                }
                case 4 -> {
                    //Modifier téléphone
                    System.out.println("Anciennement : " + tel);

                    tel = Utilitaire.regex("[0-9/ +]+", "Entrez le nouveau numéro de téléphone du client : ");
                }
                default -> {
                    break updateLoop;
                }
            }

        } while (true);

        try {

            Client newClient = new Client.ClientBuilder()
                    .setId(0)
                    .setMail(mail)
                    .setNom(nom)
                    .setPrenom(prenom)
                    .setTel(tel)
                    .build();

            if (!newClient.equals(client)){
                presenter.updateClient(newClient);
            }

        } catch (Exception e) {
            affMsg("Erreur Builder : " + e);
        }
    }

    public void rechercherClient() {
        int idRech = Integer.parseInt(Utilitaire.regex("[0-9]+", "Id du client recherché : "));

        Client cl = presenter.readClient(idRech);
    }

    private void opSpeciales() {

        Client client = lc.get(Utilitaire.choixListe(lc) - 1);

        //Avoir tout ce que le client possèdes
        client = presenter.readClient(client.getId());

        System.out.println("Que voulez-vous faire ?");
        String[] menu = {
                "Tous les taxis utilisés sans doublon",
                "Toutes les locations entre deux dates",
                "Toutes les adresses où il s'est rendu sans doublon",
                "Toutes les locations",
                "Toutes les factures",
                "Le nombre de locations (SGBD)",
                "Le cout total des locations (SGBD)",
                "Sortir"
        };
        int choix;

        special:
        do {
            choix = Utilitaire.choixListe(Arrays.asList(menu));

            switch (choix) {
                case 1 ->
                    //Tous les taxis utilisés sans doublon
                        presenter.taxiUtiliseSansDoublon(client);
                case 2 -> {
                    //Toutes les locations entre deux dates
                    LocalDate d1 = Utilitaire.lecDate();
                    LocalDate d2 = Utilitaire.lecDate();
                    presenter.locationEntreDeuxDates(client, d1, d2);
                }
                case 3 ->
                    //Toutes les adresses où il s'est rendu sans doublon
                        presenter.adresseLocationSansDoublon(client);
                case 4 ->
                    //Liste des locations du client
                        presenter.locations(client);
                case 5 ->
                    //Liste des facturations du client
                        presenter.facturations(client);
                case 6 ->
                    //Nombre de locations
                        presenter.nombreLocation(client);
                case 7 ->
                    //Cout total des locations
                        presenter.prixTotalLocs(client);
                default -> {
                    break special;
                }
            }
        } while (true);

    }

    public void supprimerClient() {
        int idCli = Integer.parseInt(Utilitaire.regex("[0-9]+", "Entrez l'id du client que vous souhaitez supprimer : "));

        presenter.removeClient(idCli);
    }
}
