package mvp.view.taxi;

import designpatterns.builder.Taxi;
import mvp.view.AbstractViewConsole;
import utilitaire.Utilitaire;
import mvp.presenter.taxi.TaxiPresenter;

import java.util.Arrays;

public class TaxiViewConsole extends AbstractViewConsole<Taxi> {

    @Override
    protected void creer() {
        String immatriculation = Utilitaire.regex(".*", "Entrez l'immatriculation du taxi : ").toUpperCase();
        //Normalement le regex serait 'T-[A-Z]{3}-[0-9]{3}'
        String carburant = Utilitaire.regex("[a-zA-Z0-9]+", "Entrez le carburant du taxi : ");
        double prixKm = Utilitaire.lireDouble("Entrez le prix au kilomètre : ");

        try {
            Taxi taxi = new Taxi.TaxiBuilder()
                    //.setId(0)
                    .setImmatriculation(immatriculation)
                    .setCarburant(carburant)
                    .setPrixKm(prixKm)
                    .build();

            presenter.add(taxi);

        } catch (Exception e) {
            affMsg("Erreur Builder : " + e);
        }

    }

    @Override
    protected void modifier() {
        int idRech = Utilitaire.lireEntier("Id du taxi recherché : ");

        Taxi taxi = presenter.read(idRech);

        if (taxi == null) {
            return;
        }

        String immatriculation = taxi.getImmatriculation();
        String carburant = taxi.getCarburant();
        double prixKm = taxi.getPrixKm();

        String[] menu = {
                "Immatriculation",
                "Carburant",
                "Prix au kilomètre",
                "Sortir"
        };

        int choix;

        System.out.println("Que souhaitez vous modifier ?");

        updateLoop:
        do {
            choix = Utilitaire.choixListe(Arrays.asList(menu));


            switch (choix) {

                case 1 -> {
                    //Modifier immatriculation
                    System.out.println("Anciennement : " + immatriculation);
                    immatriculation = Utilitaire.regex(".*", "Entrez la nouvelle immatriculation : ").toUpperCase();
                    //Normalement le regex serait 'T-[A-Z]{3}-[0-9]{3}'
                }
                case 2 -> {
                    //Modifier carburant
                    System.out.println("Anciennement : " + carburant);

                    carburant = Utilitaire.regex("[a-zA-Z0-9]+", "Entrez le nouveau carburant : ");
                }
                case 3 -> {
                    System.out.println("Anciennement : " + prixKm);

                    //Modifier prixKm
                    prixKm = Utilitaire.lireDouble("Entrez le prix au kilomètre : ");
                }
                default -> {
                    break updateLoop;
                }
            }

        } while (true);

        try {
            Taxi newTaxi = new Taxi.TaxiBuilder()
                    .setId(idRech)
                    .setImmatriculation(immatriculation)
                    .setCarburant(carburant)
                    .setPrixKm(prixKm)
                    .build();

            if (!newTaxi.equals(taxi)) {
                presenter.update(newTaxi);
            }

        } catch (Exception e) {
            System.err.println("Erreur Builder : " + e);
        }

    }

    @Override
    protected void rechercher() {
        int idRech = Utilitaire.lireEntier("Id du taxi recherché : ");

        Taxi taxi = presenter.read(idRech);

        affMsg(taxi.toString());
    }

    @Override
    protected void special() {

        Taxi taxi = lobjects.get(Utilitaire.choixListe(lobjects)-1);

        System.out.println("Que voulez-vous faire ?");
        String[] menu = {
                "Les locations de ce taxi (SGBD)",
                "Sortir"
        };
        int choix;

        special:
        do {
            choix = Utilitaire.choixListe(Arrays.asList(menu));

            switch (choix) {
                case 1 ->
                    //Tous les taxis utilisés sans doublon
                    ((TaxiPresenter) presenter).locationsTaxi(taxi);
                default -> {
                    break special;
                }
            }
        } while (true);

    }

    @Override
    protected void supprimer() {
        int idTaxi = Utilitaire.lireEntier("Entrez l'id du taxi que vous souhaitez supprimer : ");

        presenter.remove(idTaxi);
    }
}
