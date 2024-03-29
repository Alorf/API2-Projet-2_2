package mvp.view.client;

import designpatterns.builder.Client;
import mvp.view.AbstractViewConsole;
import utilitaire.Utilitaire;
import mvp.presenter.client.ClientPresenter;

import java.time.LocalDate;
import java.util.*;

public class ClientViewConsole extends AbstractViewConsole<Client> {

    /*
        Ici, tout est chargé en mémoire donc je peux me permettre d'afficher les factures et les locations
     */


    @Override
    protected void creer() {
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

            presenter.add(client);

        } catch (Exception e) {
            affMsg("Erreur Builder : " + e);
        }

    }

    @Override
    protected void modifier() {
        boolean isModif = false;

        int idRech = Utilitaire.lireEntier("Id du client recherché : ");

        Client client = presenter.read(idRech);

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

                    if (!mail.equals(client.getMail())) {
                        isModif = true;
                    }
                }
                case 2 -> {
                    //Modifier nom
                    System.out.println("Anciennement : " + nom);

                    nom = Utilitaire.regex("[a-zA-Z -]+", "Entrez le nouveau nom : ");

                    if (!nom.equals(client.getNom())) {
                        isModif = true;
                    }
                }
                case 3 -> {
                    System.out.println("Anciennement : " + prenom);

                    //Modifier prenom
                    prenom = Utilitaire.regex("[a-zA-Z -]+", "Entrez le nouveau prénom : ");

                    if (!prenom.equals(client.getPrenom())) {
                        isModif = true;
                    }
                }
                case 4 -> {
                    //Modifier téléphone
                    System.out.println("Anciennement : " + tel);

                    tel = Utilitaire.regex("[0-9/ +]+", "Entrez le nouveau numéro de téléphone du client : ");

                    if (!tel.equals(client.getTel())) {
                        isModif = true;
                    }
                }
                default -> {
                    break updateLoop;
                }
            }

        } while (true);

        if (isModif) {
            try {

                Client newClient = new Client.ClientBuilder()
                        .setId(idRech)
                        .setMail(mail)
                        .setNom(nom)
                        .setPrenom(prenom)
                        .setTel(tel)
                        .build();

                //Le equals se base que sur le mail, donc on doit tout vérifier ici


                presenter.update(newClient);

            } catch (Exception e) {
                affMsg("Erreur Builder : " + e);
            }
        }
    }

    @Override
    protected void rechercher() {
        int idRech = Utilitaire.lireEntier("Id du client recherché : ");

        Client cl = presenter.read(idRech);

        affMsg(cl.toString());
    }

    @Override
    protected void supprimer() {
        int idCli = Utilitaire.lireEntier("Entrez l'id du client que vous souhaitez supprimer : ");

        presenter.remove(idCli);
    }

    @Override
    protected void special() {

        Client client = lobjects.get(Utilitaire.choixListe(lobjects) - 1);

        //Avoir tout ce que le client possèdes
        client = presenter.read(client.getId());

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
                        ((ClientPresenter) presenter).taxiUtiliseSansDoublon(client);
                case 2 -> {
                    //Toutes les locations entre deux dates
                    LocalDate d1 = Utilitaire.lecDate("Entrez la date de début");
                    LocalDate d2 = Utilitaire.lecDate("Entrez la date de fin");
                    ((ClientPresenter) presenter).locationEntreDeuxDates(client, d1, d2);
                }
                case 3 ->
                    //Toutes les adresses où il s'est rendu sans doublon
                        ((ClientPresenter) presenter).adresseLocationSansDoublon(client);
                case 4 ->
                    //Liste des locations du client
                        ((ClientPresenter) presenter).locations(client);
                case 5 ->
                    //Liste des facturations du client
                        ((ClientPresenter) presenter).facturations(client);
                case 6 ->
                    //Nombre de locations
                        ((ClientPresenter) presenter).nombreLocation(client);
                case 7 ->
                    //Cout total des locations
                        ((ClientPresenter) presenter).prixTotalLocs(client);
                default -> {
                    break special;
                }
            }
        } while (true);

    }
}
