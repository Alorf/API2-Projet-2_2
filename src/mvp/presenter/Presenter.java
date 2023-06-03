package mvp.presenter;

import mvp.model.DAO;
import mvp.view.ViewInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public abstract class Presenter<T> {
    protected DAO<T> model;

    protected ViewInterface<T> view;

    protected static final Logger logger = LogManager.getLogger(Presenter.class);


    public Presenter(DAO<T> model, ViewInterface<T> view) {
        this.model = model;
        this.view = view;
        this.view.setPresenter(this);
    }

    public void start() {
        List<T> objects = model.getAll();
        view.setListDatas(objects);
        view.menu();
    }

    protected void majListe() {
        List<T> objects = model.getAll();
        view.setListDatas(objects);
    }

    public void add(T object) {
        T newObject = model.add(object);

        if (newObject == null) {
            view.affMsg("Erreur lors de la création de l'élément");
        } else {
            view.affMsg("Création de : " + newObject);
        }

        majListe();
    }

    public T read(int idRech) {
        T object = model.read(idRech);

        if (object == null) {
            view.affMsg("Elément introuvable");
        }

        return object;
    }

    public void update(T object) {
        boolean ok = model.update(object);

        if (ok) {
            view.affMsg("Elément modifié : " + object);
        } else {
            view.affMsg("Elément non modifié, erreur");
        }

        majListe();
    }

    public void remove(int idRemove) {
        boolean ok = model.remove(idRemove);

        if (ok) {
            view.affMsg("Elément effacée : id " + idRemove);
        } else {
            view.affMsg("Elément non effacé, erreur");
        }

        majListe();
    }

    public T selectionner() {
        logger.info("Sélection d'un élément " + model.getClass().getSimpleName() + " dans la liste");


        List<T> objects = model.getAll();

        if (objects == null || objects.isEmpty()) {
            return null;
        }

        T object = view.selectionner(objects);

        return object;
    }
}