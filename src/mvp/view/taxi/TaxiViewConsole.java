package mvp.view.taxi;

import designpatterns.builder.Client;
import designpatterns.builder.Taxi;
import utilitaire.Utilitaire;
import mvp.presenter.TaxiPresenter;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class TaxiViewConsole implements TaxiViewInterface {
    private TaxiPresenter presenter;
    private List<Taxi> lt;

    public TaxiViewConsole() {

    }

    @Override
    public void setPresenter(TaxiPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setListDatas(List<Taxi> taxis) {
        this.lt = taxis;
        int i = 1;
        for (Taxi taxi : lt) {
            System.out.println((i++) + "." + taxi);
        }
        menu();
    }

    @Override
    public void affMsg(String msg) {
        System.out.println("Information : " + msg);

    }

    @Override
    public Taxi selectionner(List<Taxi> taxis) {

        int choix = Utilitaire.choixListe(taxis);

        return taxis.get(choix - 1);
    }

    @Override
    public void affListe(List info) {
        Utilitaire.afficherListe(info);
    }



    public void menu() {

        int choix;

        String[] menu = new String[]{
                "Créer",
                "Rechercher",
                "Modifier",
                "Supprimer",
                "Opérations speciales",
                "Finir",
        };

        do {
            choix = Utilitaire.choixListe(Arrays.asList(menu));


            switch (choix) {
                case 1 ->
                    //Créer un taxi
                        creerTaxi();
                case 2 ->
                    //Rechercher un taxi
                        rechercherTaxi();
                case 3 ->
                    //Modifier un taxi
                        modifierTaxi();
                case 4 ->
                    //Supprimer un taxi
                        supprimerTaxi();
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

    public void creerTaxi() {
        String immatriculation = Utilitaire.regex(".*", "Entrez l'immatriculation du taxi : ").toUpperCase();
        //Normalement le regex serait 'T-[A-Z]{3}-[0-9]{3}'
        String carburant = Utilitaire.regex("[a-zA-Z0-9]+", "Entrez le carburant du taxi : ");
        double prixKm = Double.parseDouble(Utilitaire.regex("[ \\t]*(\\+|\\-)?[ \\t]*(\\d*\\.?\\d+(E[\\+|\\-|\\d]\\d*)?)", "Entrez le prix au kilomètre du taxi : "));
        //regex d'un double https://regex101.com/r/AtCkmx/1

        try {
            Taxi taxi = new Taxi.TaxiBuilder()
                    //.setId(0)
                    .setImmatriculation(immatriculation)
                    .setCarburant(carburant)
                    .setPrixKm(prixKm)
                    .build();

            presenter.addTaxi(taxi);

        } catch (Exception e) {
            System.err.println("Erreur Builder : " + e);
        }

    }

    public void modifierTaxi() {
        int idRech = Integer.parseInt(Utilitaire.regex("[0-9]+", "Id du taxi recherché : "));

        Taxi taxi = presenter.readTaxi(idRech);

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
                    prixKm = Double.parseDouble(Utilitaire.regex("[ \\t]*(\\+|\\-)?[ \\t]*(\\d*\\.?\\d+(E[\\+|\\-|\\d]\\d*)?)", "Entrez le nouveau prix au kilomètre : "));
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

            presenter.updateTaxi(newTaxi);

        } catch (Exception e) {
            System.err.println("Erreur Builder : " + e);
        }

    }

    public void rechercherTaxi() {
        int idRech = Integer.parseInt(Utilitaire.regex("[0-9]+", "Id du taxi recherché : "));

        Taxi taxi = presenter.readTaxi(idRech);
    }

    private void opSpeciales() {

        Taxi taxi = lt.get(Utilitaire.choixListe(lt)-1);

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
                case 1 -> {
                    //Tous les taxis utilisés sans doublon
                    presenter.locationsTaxi(taxi);
                }
                default -> {
                    break special;
                }
            }
        } while (true);

    }

    public void supprimerTaxi() {
        int idTaxi = Integer.parseInt(Utilitaire.regex("[0-9]+", "Entrez l'id du taxi que vous souhaitez supprimer : "));

        presenter.removeTaxi(idTaxi);
    }
}
