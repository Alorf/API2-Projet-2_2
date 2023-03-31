package mvp.view.adresse;

import locationTaxi.Adresse;
import locationTaxi.Client;
import mvp.Utilitaire;
import mvp.presenter.AdressePresenter;
import mvp.view.adresse.AdresseViewInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class AdresseViewConsole implements AdresseViewInterface {
    private AdressePresenter presenter;
    private List<Adresse> la;

    public AdresseViewConsole() {

    }

    @Override
    public void setPresenter(AdressePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setListDatas(List<Adresse> adresses) {
        this.la = adresses;
        int i = 1;
        for (Adresse adresse : la) {
            System.out.println((i++) + "." + adresse);
        }
        menu();
    }

    @Override
    public void affMsg(String msg) {
        System.out.println("information : " + msg);

    }

    @Override
    public Adresse selectionner(List<Adresse> adresses) {
        int choix = Utilitaire.choixListe(adresses);

        return adresses.get(choix-1);
    }

    public void menu() {

        int choix;

        String[] menu = new String[]{
                "Créer",
                "Rechercher",
                "Modifier",
                "Supprimer",
                "Tous",
                "Finir",
        };

        do {

            choix = Utilitaire.choixListe(Arrays.asList(menu));

            switch (choix) {
                case 1 ->
                    //Créer une adresse
                        creerAdresse();
                case 2 ->
                    //Rechercher une adresse
                        rechercherAdresse();
                case 3 ->
                    //Modifier une adresse
                        modifierAdresse();
                case 4 ->
                    //Supprimer une adresse
                        supprimerAdresse();
                case 5 ->
                    //Autre une adresse
                        tout();
                case 6 ->
                    //Fin
                {
                    return;
                }
                default -> System.out.println("Mauvaise saisie, recommencez !");
            }
        } while (true);
    }

    public void creerAdresse() {
        int cp = Integer.parseInt(Utilitaire.regex("[0-9]+", "Entrez le code postal de l'adresse : ").toUpperCase());
        String localite = Utilitaire.regex("[a-zA-Z0-9]+", "Entrez la localité de l'adresse : ");
        String rue = Utilitaire.regex(".*", "Entrez la rue de l'adresse : ");
        String num = Utilitaire.regex("[a-zA-Z0-9 ]+", "Entrez le numéro de l'adresse : ");


        presenter.addAdresse(new Adresse(0, cp, localite, rue, num));
    }

    public void modifierAdresse() {
        int idRech = Integer.parseInt(Utilitaire.regex("[0-9]+", "Id de l'adresse recherché : "));

        Adresse adresse = presenter.readAdresse(idRech);

        int cp = adresse.getCp();
        String localite = adresse.getLocalite();
        String rue = adresse.getRue();
        String num = adresse.getNum();

        String[] menu = {
                "Code postal",
                "Localité",
                "Rue",
                "Numéro",
                "Sortir"
        };

        int choix;
        System.out.println("Que souhaitez vous modifier");

        updateLoop:do {

            choix = Utilitaire.choixListe(Arrays.asList(menu));

            switch (choix) {

                case 1 -> {
                    //Modifier cp
                    System.out.println("Anciennement : " + cp);
                    cp = Integer.parseInt(Utilitaire.regex("[0-9]+", "Entrez le nouveau code postal : "));
                }
                case 2 -> {
                    //Modifier localite
                    System.out.println("Anciennement : " + localite);

                    localite = Utilitaire.regex("[a-zA-Z0-9]+", "Entrez le nouveau localite : ");
                }
                case 3 -> {
                    System.out.println("Anciennement : " + rue);

                    //Modifier rue
                    rue = Utilitaire.regex("[a-zA-Z]+", "Entrez la nouvelle rue : ");
                }
                case 4 -> {
                    System.out.println("Anciennement : " + num);

                    //Modifier rue
                    num = Utilitaire.regex("[a-zA-Z0-9]+", "Entrez le nouveau numéro : ");
                }
                case 5 -> {
                    break updateLoop;
                }
                default -> System.out.println("Mauvaise saisie, recommencez !");
            }

        } while (true);

        presenter.updateAdresse(new Adresse(idRech, cp, localite, rue, num));
    }

    public void rechercherAdresse() {
        int idRech = Integer.parseInt(Utilitaire.regex("[0-9]+", "Id de l'adresse recherché : "));

        Adresse adresse= presenter.readAdresse(idRech);

    }

    public void supprimerAdresse() {
        int idAdresse = Integer.parseInt(Utilitaire.regex("[0-9]+", "Entrez l'id de l'adresse que vous souhaitez supprimer : "));

        presenter.removeAdresse(idAdresse);
    }

    public void tout() {
        List<Adresse> adresses = presenter.tout();

        Utilitaire.afficherListe(adresses);
    }
}
