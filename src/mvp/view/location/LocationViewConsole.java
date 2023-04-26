package mvp.view.location;

import locationTaxi.metier.Adresse;
import locationTaxi.metier.Client;
import locationTaxi.metier.Location;
import utilitaire.Utilitaire;
import mvp.presenter.LocationPresenter;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class LocationViewConsole implements LocationViewInterface {
    private LocationPresenter presenter;
    private List<Location> ll;

    public LocationViewConsole() {

    }

    @Override
    public void setPresenter(LocationPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setListDatas(List<Location> locations) {
        this.ll = locations;
        int i = 1;
        for (Location location : ll) {
            System.out.println((i++) + "." + location);
        }
        menu();
    }

    @Override
    public void affMsg(String msg) {
        System.out.println("information : " + msg);

    }

    public void menu() {

        int choix;

        String[] menu = new String[]{
                "Créer",
                "Rechercher",
                "Modifier",
                "Supprimer",
                "Tous",
                "Special",
                "Finir",
        };

        do {

            choix = Utilitaire.choixListe(Arrays.asList(menu));

            switch (choix) {
                case 1 ->
                    //Créer une location
                        creerLocation();
                case 2 ->
                    //Rechercher une location
                        rechercherLocation();
                case 3 ->
                    //Modifier une location
                        modifierLocation();
                case 4 ->
                    //Supprimer une location
                        supprimerLocation();
                case 5 ->
                    //Autre une location
                        tout();
                case 6 ->
                    //Ajout de facturation
                        special();
                case 7 ->
                {
                    //Fin
                    return;
                }
            }
        } while (true);
    }

    public void creerLocation() {
        LocalDate date = Utilitaire.lecDate();
        int kmTotal = Integer.parseInt(Utilitaire.regex("[0-9]+", "Nombre de kilomètres : "));

        presenter.addLocation(new Location(0, date, kmTotal, null, null));
    }

    public void modifierLocation() {
        int idRech = Integer.parseInt(Utilitaire.regex("[0-9]+", "Id de la location recherché : "));

        Location location = presenter.readLocation(idRech);

        LocalDate date = location.getDate();
        int kmTotal = location.getKmTotal();
        Client client = location.getClient();
        Adresse adresse = location.getAdrDepart();


        String[] menu = {
                "Date",
                "Nombre de kilomètre",
                "Client",
                "Adresse",
                "Sortir"
        };

        int choix;
        System.out.println("Que souhaitez vous modifier");

        updateLoop:
        do {

            choix = Utilitaire.choixListe(Arrays.asList(menu));

            switch (choix) {

                case 1 -> {
                    System.out.println("Anciennement : " + date);
                    date = Utilitaire.lecDate();
                }
                case 2 -> {
                    System.out.println("Anciennement : " + kmTotal);
                    kmTotal = Integer.parseInt(Utilitaire.regex("[0-9]+", "Nouveau kilométrage"));
                }
                case 3 -> {
                    System.out.println("Anciennement : " + client);
                    //Sélection client
                    client = presenter.choixClient();
                }
                case 4 -> {
                    System.out.println("Anciennement : " + adresse);
                    //Sélection adresse
                    adresse = presenter.choixAdresse();

                }
                case 5 -> {
                    break updateLoop;
                }
            }

        } while (true);

        presenter.updateLocation(new Location(idRech, date, kmTotal, client, adresse));
    }

    public void rechercherLocation() {
        int idRech = Integer.parseInt(Utilitaire.regex("[0-9]+", "Id de la location recherchée : "));

        Location location = presenter.readLocation(idRech);

    }

    public void supprimerLocation() {
        int idLocation = Integer.parseInt(Utilitaire.regex("[0-9]+", "Entrez l'id de la location que vous souhaitez supprimer : "));

        presenter.removeLocation(idLocation);
    }

    public void tout() {
        List<Location> locations = presenter.tout();

        Utilitaire.afficherListe(locations);
    }

    private void special() {
        int choixLoc = Utilitaire.choixListe(ll);

        Location loc = ll.get(choixLoc - 1);

        System.out.println("Que voulez-vous faire ?");
        String[] menu = {
                "Ajout d'une facturation",
                "Quitter"
        };

        //todo : Modification et suppression de facturation ?

        int choix;

        special:
        do {
            choix = Utilitaire.choixListe(Arrays.asList(menu));

            switch (choix) {
                case 1 ->
                    //Ajout d'une facturation
                    presenter.addFacturation(loc);
                case 2 -> {
                    break special;
                }
            }
        } while (true);

    }
}
