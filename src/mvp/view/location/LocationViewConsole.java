package mvp.view.location;

import designpatterns.builder.Adresse;
import designpatterns.builder.Client;
import designpatterns.builder.Facturation;
import designpatterns.builder.Location;
import mvp.view.AbstractViewConsole;
import utilitaire.Utilitaire;
import mvp.presenter.location.LocationPresenter;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class LocationViewConsole extends AbstractViewConsole<Location> implements SpecialLocationViewInterface {

    @Override
    protected void creer() {
        String[] menu = {
          "Oui",
          "Non"
        };
        int choix;

        LocalDate date = Utilitaire.lecDate("Date de la location");
        int kmTotal = Utilitaire.lireEntier("Nombre de kilomètres : ");

        try {
            Location location = new Location.LocationBuilder()
                    //.setId(0)
                    .setDate(date)
                    .setKmTotal(kmTotal)
                    .build(false);

            presenter.add(location);

            do {
                ((LocationPresenter) presenter).addFacturation(location);
                System.out.printf("Souhaitez-vous continuer ?");
                choix = Utilitaire.choixListe(Arrays.asList(menu));
            }while (choix == 1);

        } catch (Exception e) {
            affMsg("Erreur Builder : " + e);
        }

    }

    @Override
    protected void modifier() {
        int idRech = Utilitaire.lireEntier("Id de la location recherché : ");

        Location location = presenter.read(idRech);

        if (location == null) {
            return;
        }

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
                    System.out.println("Anciennement : " + Utilitaire.getDateFrench(date));
                    date = Utilitaire.lecDate("Nouvelle date");
                }
                case 2 -> {
                    System.out.println("Anciennement : " + kmTotal);
                    kmTotal = Utilitaire.lireEntier("Nouveau kilométrage : ");
                }
                case 3 -> {
                    System.out.println("Anciennement : " + client);
                    //Sélection client
                    client = ((LocationPresenter) presenter).choixClient();
                }
                case 4 -> {
                    System.out.println("Anciennement : " + adresse);
                    //Sélection adresse
                    adresse = ((LocationPresenter) presenter).choixAdresse();

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
                    .build(false);

            if (!newLocation.equals(location)) {
                presenter.update(newLocation);
            }

        } catch (Exception e) {
            affMsg("Erreur Builder : " + e);
        }

    }

    @Override
    protected void rechercher() {
        int idRech = Utilitaire.lireEntier("Id de la location recherchée : ");

        Location location = presenter.read(idRech);

        affMsg(location.toString());

    }

    @Override
    protected void supprimer() {
        int idLocation = Utilitaire.lireEntier("Entrez l'id de la location que vous souhaitez supprimer : ");

        presenter.remove(idLocation);
    }

    @Override
    protected void special() {
        int choixLoc = Utilitaire.choixListe(lobjects);

        Location loc = lobjects.get(choixLoc - 1);

        System.out.println("Que voulez-vous faire ?");
        String[] menu = {
                "Toutes les location à une date précise",
                "Ajout d'une facturation (SGBD)",
                "Voir les facturations",
                "Suppression d'une facturation",
                "Prix total d'une location (SGBD)",
                "Quitter"
        };

        int choix;

        special:
        do {
            choix = Utilitaire.choixListe(Arrays.asList(menu));

            switch (choix) {
                case 1 -> {
                    //Toutes les location à une date précise"
                    LocalDate date = Utilitaire.lecDate("Entrez une date");
                    ((LocationPresenter) presenter).locationsDate(date);
                }
                case 2 ->
                    //Ajout d'une facturation
                        ((LocationPresenter) presenter).addFacturation(loc);
                case 3 ->
                    //Suppression d'une facturation
                        ((LocationPresenter) presenter).facturations(loc);
                case 4 ->
                    //Suppression d'une facturation
                        ((LocationPresenter) presenter).removeFacturation(loc);
                case 5 ->
                    //Prix total d'une location
                        ((LocationPresenter) presenter).prixTotalLocation(loc);
                default -> {
                    break special;
                }
            }
        } while (true);
    }

    @Override
    public Facturation selectionnerFacture(List<Facturation> objects) {

        if (objects == null || objects.isEmpty()) {
            return null;
        }

        int choix = Utilitaire.choixListe(objects);

        return objects.get(choix - 1);
    }
}
