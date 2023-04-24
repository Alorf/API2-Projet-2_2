package mvp.view.client;

import locationTaxi.*;
import mvp.Utilitaire;
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
        int i = 1;
        for (Client cl : lc) {
            System.out.println((i++) + "." + cl);
        }
        menu();
    }

    @Override
    public void affMsg(String msg) {
        System.out.println("information : " + msg);

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

    public void menu() {

        int choix;

        String[] menu = new String[]{
                "Créer",
                "Rechercher",
                "Modifier",
                "Supprimer",
                "Tous",
                "Finir"
        };

        do {
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
                    //Autre un client
                        tout();
                case 6 ->
                //Fin
                {
                    return;
                }
            }
        } while (true);
    }

    public void creerClient() {
        String mail = Utilitaire.regex("[a-zA-Z.@]+", "Entrez le mail du client : ").toLowerCase();
        String nom = Utilitaire.regex("[a-zA-Z]+", "Entrez le nom du client : ");
        String prenom = Utilitaire.regex("[a-zA-Z]+", "Entrez le prénom du client : ");
        String tel = Utilitaire.regex("[0-9/ +]+", "Entrez le numéro de téléphone du client : ");


        presenter.addClient(new Client(0, mail, nom, prenom, tel));
    }

    public void modifierClient() {
        int idRech = Integer.parseInt(Utilitaire.regex("[0-9]+", "Id du client recherché : "));

        Client client = presenter.readClient(idRech);

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
        System.out.println("Que souhaitez vous modifier");

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

                    nom = Utilitaire.regex("[a-zA-Z]+", "Entrez le nouveau nom : ");
                }
                case 3 -> {
                    System.out.println("Anciennement : " + prenom);

                    //Modifier prenom
                    prenom = Utilitaire.regex("[a-zA-Z]+", "Entrez le nouveau prénom : ");
                }
                case 4 -> {
                    //Modifier téléphone
                    System.out.println("Anciennement : " + tel);

                    tel = Utilitaire.regex("[0-9/ +]+", "Entrez le nouveau numéro de téléphone du client : ");
                }
                case 5 -> {
                    break updateLoop;
                }
                default -> System.out.println("Mauvaise saisie, recommencez !");
            }

        } while (true);

        presenter.updateClient(new Client(idRech, mail, nom, prenom, tel));
    }

    public void rechercherClient() {
        int idRech = Integer.parseInt(Utilitaire.regex("[0-9]+", "Id du client recherché : "));

        Client cl = presenter.readClient(idRech);

        opSpeciales(cl);
    }

    private void opSpeciales(Client client) {

        //int idRech = Integer.parseInt(Utilitaire.regex("[0-9]+", "Id du client recherché : "));
        //Client client = presenter.readClient(idRech);

        if (client == null) {
            System.out.println("Client introuvable");
            return;
        }
        System.out.println("Client trouvé");

        System.out.println("Que voulez-vous faire ?");
        String[] menu = {
                "Tous les taxis utilisés sans doublon",
                "Toutes les locations entre deux dates",
                "Toutes les adresses où il s'est rendu sans doublon",
                "Toutes les locations",
                "Toutes les factures",
                "Le nombre de locations",
                "Sortir"
        };
        int choix;

        special:
        do {
            choix = Utilitaire.choixListe(Arrays.asList(menu));

            switch (choix) {
                case 1 -> {
                    //Tous les taxis utilisés sans doublon
                    presenter.taxiUtiliseSansDoublon(client);
                }
                case 2 -> {
                    //Toutes les locations entre deux dates
                    LocalDate d1 = Utilitaire.lecDate();
                    LocalDate d2 = Utilitaire.lecDate();
                    presenter.locationEntreDeuxDates(client, d1, d2);
                }
                case 3 -> {
                    //Toutes les adresses où il s'est rendu sans doublon
                    presenter.adresseLocationSansDoublon(client);

                }
                case 4 -> {
                    //Liste des locations du client
                    presenter.locations(client);
                }
                case 5 -> {
                    //Liste des facturations du client
                    presenter.facturations(client);
                }
                case 6 -> {
                    //Nombre de locations
                    presenter.nombreLocation(client);
                }
                case 7 -> {
                    break special;
                }
            }
        } while (true);

    }

    public void supprimerClient() {
        int idCli = Integer.parseInt(Utilitaire.regex("[0-9]+", "Entrez l'id du client que vous souhaitez supprimer : "));

        presenter.removeClient(idCli);
    }

    public void tout() {
        List<Client> clients = presenter.tout();

        Utilitaire.afficherListe(clients);
    }
}
