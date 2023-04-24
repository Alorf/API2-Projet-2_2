package mvp.view.taxi;

import locationTaxi.Taxi;
import mvp.Utilitaire;
import mvp.presenter.TaxiPresenter;

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
        System.out.println("information : " + msg);

    }

    @Override
    public Taxi selectionner(List<Taxi> taxis) {

        int choix = Utilitaire.choixListe(taxis);

        return taxis.get(choix-1);
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
                    //Autre un taxi
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

    public void creerTaxi() {
        String immatriculation = Utilitaire.regex(".*", "Entrez l'immatriculation du taxi : ").toUpperCase();
        //Normalement le regex serait 'T-[A-Z]{3}-[0-9]{3}'
        String carburant = Utilitaire.regex("[a-zA-Z0-9]+", "Entrez le carburant du taxi : ");
        double prixKm = Double.parseDouble(Utilitaire.regex("[ \\t]*(\\+|\\-)?[ \\t]*(\\d*\\.?\\d+(E[\\+|\\-|\\d]\\d*)?)", "Entrez le prix au kilomètre du taxi : "));
        //regex d'un double https://regex101.com/r/AtCkmx/1


        presenter.addTaxi(new Taxi(0, immatriculation, carburant, prixKm));
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
        System.out.println("Que souhaitez vous modifier");

        updateLoop:do {
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
                case 4 -> {
                    break updateLoop;
                }
                default -> System.out.println("Mauvaise saisie, recommencez !");
            }

        } while (true);

        presenter.updateTaxi(new Taxi(idRech, immatriculation, carburant, prixKm));
    }

    public void rechercherTaxi() {
        int idRech = Integer.parseInt(Utilitaire.regex("[0-9]+", "Id du taxi recherché : "));

        Taxi taxi= presenter.readTaxi(idRech);

    }

    public void supprimerTaxi() {
        int idTaxi = Integer.parseInt(Utilitaire.regex("[0-9]+", "Entrez l'id du taxi que vous souhaitez supprimer : "));

        presenter.removeTaxi(idTaxi);
    }

    public void tout() {
        List<Taxi> taxis = presenter.tout();

        Utilitaire.afficherListe(taxis);
    }

}
