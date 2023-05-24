package mvp.view.adresse;

import designpatterns.builder.Adresse;
import mvp.view.AbstractViewConsole;
import utilitaire.Utilitaire;

import java.util.Arrays;

public class AdresseViewConsole extends AbstractViewConsole<Adresse> {

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
            Utilitaire.afficherListe(lobjects);

            choix = Utilitaire.choixListe(Arrays.asList(menu));

            switch (choix) {
                case 1 ->
                    //Créer une adresse
                        creer();
                case 2 ->
                    //Rechercher une adresse
                        rechercher();
                case 3 ->
                    //Modifier une adresse
                        modifier();
                case 4 ->
                    //Supprimer une adresse
                        supprimer();
                default ->
                //Fin
                {
                    return;
                }
            }
        } while (true);
    }

    public void creer() {
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

            presenter.add(adresse);

        } catch (Exception e) {
            affMsg("Erreur Builder : " + e);
        }

    }

    public void modifier() {
        int idRech = Integer.parseInt(Utilitaire.regex("[0-9]+", "Id de l'adresse recherché : "));

        Adresse adresse = presenter.read(idRech);

        if (adresse == null) {
            return;
        }

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
                presenter.update(newAdresse);
            }
        } catch (Exception e) {
            affMsg("Erreur Builder : " + e);
        }
    }

    public void rechercher() {
        int idRech = Integer.parseInt(Utilitaire.regex("[0-9]+", "Id de l'adresse recherché : "));

        Adresse adresse = presenter.read(idRech);

    }

    public void supprimer() {
        int idAdresse = Integer.parseInt(Utilitaire.regex("[0-9]+", "Entrez l'id de l'adresse que vous souhaitez supprimer : "));

        presenter.remove(idAdresse);
    }
}
