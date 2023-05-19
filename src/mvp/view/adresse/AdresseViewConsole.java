package mvp.view.adresse;

import designpatterns.builder.Adresse;
import utilitaire.Utilitaire;
import mvp.presenter.AdressePresenter;

import java.util.Arrays;
import java.util.List;

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
    }

    @Override
    public void affMsg(String msg) {
        System.out.println("Information : " + msg);

    }

    @Override
    public Adresse selectionner(List<Adresse> adresses) {
        int choix = Utilitaire.choixListe(adresses);

        return adresses.get(choix - 1);
    }

    @Override
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
            Utilitaire.afficherListe(la);

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
                default ->
                //Fin
                {
                    return;
                }
            }
        } while (true);
    }

    public void creerAdresse() {
        int cp = Integer.parseInt(Utilitaire.regex("[0-9]{4}", "Entrez le code postal de l'adresse : ").toUpperCase());
        String localite = Utilitaire.regex("[a-zA-Z0-9]+", "Entrez la localité de l'adresse : ");
        String rue = Utilitaire.regex(".*", "Entrez la rue de l'adresse : ");
        String num = Utilitaire.regex("[a-zA-Z0-9 ]+", "Entrez le numéro de l'adresse : ");


        Adresse adresse;
        try {
            adresse = new Adresse.AdresseBuilder()
                    .setCp(cp)
                    .setLocalite(localite)
                    .setRue(rue)
                    .setNum(num)
                    .build();

            presenter.addAdresse(adresse);

        } catch (Exception e) {
            affMsg("Erreur Builder : " + e);
        }

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

        updateLoop:
        do {
            choix = Utilitaire.choixListe(Arrays.asList(menu));

            switch (choix) {

                case 1 -> {
                    //Modifier cp
                    System.out.println("Anciennement : " + cp);
                    cp = Integer.parseInt(Utilitaire.regex("[0-9 ]{4}", "Entrez le nouveau code postal : "));
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
                default -> {
                    break updateLoop;
                }
            }

        } while (true);

        try {

            Adresse newAdresse = new Adresse.AdresseBuilder()
                    .setId(idRech)
                    .setCp(cp)
                    .setLocalite(localite)
                    .setRue(rue)
                    .setNum(num)
                    .build();

            if (!newAdresse.equals(adresse)){
                presenter.updateAdresse(newAdresse);
            }
        } catch (Exception e) {
            affMsg("Erreur Builder : " + e);
        }
    }

    public void rechercherAdresse() {
        int idRech = Integer.parseInt(Utilitaire.regex("[0-9]+", "Id de l'adresse recherché : "));

        Adresse adresse = presenter.readAdresse(idRech);

    }

    public void supprimerAdresse() {
        int idAdresse = Integer.parseInt(Utilitaire.regex("[0-9]+", "Entrez l'id de l'adresse que vous souhaitez supprimer : "));

        presenter.removeAdresse(idAdresse);
    }
}
