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
            Utilitaire.afficherListe(lobjects);

            choix = Utilitaire.choixListe(Arrays.asList(menu));

            switch (choix) {
                case 1 ->
                    //Créer une location
                        creer();
                case 2 ->
                    //Rechercher une location
                        rechercher();
                case 3 ->
                    //Modifier une location
                        modifier();
                case 4 ->
                    //Supprimer une location
                        supprimer();
                case 5 ->
                    //Opérations spéciales
                        opSpeciales();
                default -> {
                    //Fin
                    return;
                }
            }
        } while (true);
    }

    public void creer() {
        LocalDate date = Utilitaire.lecDate("Date de la location");
        int kmTotal = Integer.parseInt(Utilitaire.regex("[0-9]+", "Nombre de kilomètres : "));

        try {
            Location location = new Location.LocationBuilder()
                    //.setId(0)
                    .setDate(date)
                    .setKmTotal(kmTotal)
                    .build(false);

            presenter.add(location);

        } catch (Exception e) {
            affMsg("Erreur Builder : " + e);
        }

    }

    public void modifier() {
        int idRech = Integer.parseInt(Utilitaire.regex("[0-9]+", "Id de la location recherché : "));

        Location location = presenter.read(idRech);

        if (location == null) {
            return;
        }

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
                    kmTotal = Integer.parseInt(Utilitaire.regex("[0-9]+", "Nouveau kilométrage"));
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

    public void rechercher() {
        int idRech = Integer.parseInt(Utilitaire.regex("[0-9]+", "Id de la location recherchée : "));

        Location location = presenter.read(idRech);

        affMsg(location.toString());

        affMsg(location.toString());

    }

    public void supprimer() {
        int idLocation = Integer.parseInt(Utilitaire.regex("[0-9]+", "Entrez l'id de la location que vous souhaitez supprimer : "));

        presenter.remove(idLocation);
    }

    private void opSpeciales() {
        int choixLoc = Utilitaire.choixListe(lobjects);

        Location loc = lobjects.get(choixLoc - 1);

        System.out.println("Que voulez-vous faire ?");
        String[] menu = {
                "Ajout d'une facturation (SGBD)",
                "Suppression d'une facturation",
                "Prix total d'une location (SGBD)",
                "Quitter"
        };

        int choix;

        special:
        do {
            choix = Utilitaire.choixListe(Arrays.asList(menu));

            switch (choix) {
                case 1 ->
                    //Ajout d'une facturation
                        ((LocationPresenter) presenter).addFacturation(loc);
                case 2 -> {
                    //Suppression d'une facturation
                    ((LocationPresenter) presenter).removeFacturation(loc);
                }
                case 3 ->
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
