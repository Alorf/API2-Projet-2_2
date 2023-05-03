package mvp.view.location;

import designpatterns.builder.Adresse;
import designpatterns.builder.Client;
import designpatterns.builder.Location;
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
        System.out.println("Information : " + msg);

    }

    public void menu() {

        int choix;

        String[] menu = new String[]{
                "Créer",
                "Rechercher",
                "Modifier",
                "Supprimer",
                "Opérations spéciales",
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
                    //Opérations spéciales
                        opSpeciales();
                default ->
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

        try {
            Location location = new Location.LocationBuilder()
                    //.setId(0)
                    .setDate(date)
                    .setKmTotal(kmTotal)
                    .build();

            presenter.addLocation(location);

        } catch (Exception e) {
            System.err.println("Erreur Builder : " + e);
        }

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

        System.out.println("Que souhaitez vous modifier ?");

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
                default -> {
                    break updateLoop;
                }
            }

        } while (true);

        try {
            Location newLocation = new Location.LocationBuilder()
                    .setId(idRech)
                    .setDate(date)
                    .setKmTotal(kmTotal)
                    .setClient(client)
                    .setAdrDepart(adresse)
                    .build();

            presenter.updateLocation(newLocation);

        } catch (Exception e) {
            System.err.println("Erreur Builder : " + e);
        }

    }

    public void rechercherLocation() {
        int idRech = Integer.parseInt(Utilitaire.regex("[0-9]+", "Id de la location recherchée : "));

        Location location = presenter.readLocation(idRech);

    }

    public void supprimerLocation() {
        int idLocation = Integer.parseInt(Utilitaire.regex("[0-9]+", "Entrez l'id de la location que vous souhaitez supprimer : "));

        presenter.removeLocation(idLocation);
    }

    private void opSpeciales() {
        int choixLoc = Utilitaire.choixListe(ll);

        Location loc = ll.get(choixLoc - 1);

        System.out.println("Que voulez-vous faire ?");
        String[] menu = {
                "Ajout d'une facturation",
                "Prix total d'une location (SGBD)",
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
                case 2 ->
                    //Prix total d'une location
                    presenter.prixTotalLocation(loc);
                default -> {
                    break special;
                }
            }
        } while (true);
    }
}
