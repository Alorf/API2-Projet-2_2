package mvp.view;

import mvp.presenter.Presenter;
import utilitaire.Utilitaire;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public abstract class AbstractViewConsole<T> implements ViewInterface<T> {
    protected Presenter<T> presenter;
    protected List<T> lobjects;

    @Override
    public void setPresenter(Presenter<T> presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setListDatas(List<T> lobjects) {
        this.lobjects = lobjects;
    }

    public void affMsg(String msg) {
        System.out.println("Information : " + msg);
    }

    @Override
    public void affListe(List info) {
        Utilitaire.afficherListe(info);
    }

    @Override
    public void affListe(Set info) {
        Utilitaire.afficherListe(info);
    }

    @Override
    public T selectionner(List<T> objects) {

        if (objects == null || objects.isEmpty()) {
            return null;
        }

        int choix = Utilitaire.choixListe(objects);

        return objects.get(choix - 1);
    }

    @Override
    public void menu() {

        int choix;

        String[] menu = new String[]{
                "Créer",
                "Rechercher",
                "Modifier",
                "Supprimer",
                "Opérations spéciales",
                "Tout afficher",
                "Finir",
        };

        do {
            Utilitaire.afficherListe(lobjects);

            choix = Utilitaire.choixListe(Arrays.asList(menu));

            switch (choix) {
                case 1 ->
                    //Créer un object
                        creer();
                case 2 ->
                    //Rechercher un object
                        rechercher();
                case 3 ->
                    //Modifier un object
                        modifier();
                case 4 ->
                    //Supprimer un object
                        supprimer();
                case 5 ->
                    //Opérations spéciales
                        special();
                case 6 ->
                    //Tout afficher
                        all();
                default -> {
                    //Fin
                    return;
                }
            }
        } while (true);
    }

    protected abstract void creer();

    protected abstract void modifier();

    protected abstract void rechercher();

    protected abstract void supprimer();

    protected abstract void special();

    protected void all(){
        System.out.println("-----------------------------------------");
        Utilitaire.afficherListe(lobjects);
        System.out.println("-----------------------------------------");
    }
}
