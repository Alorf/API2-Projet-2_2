package mvp.presenter;

import mvp.model.DAO;
import mvp.view.ViewInterface;

import java.util.List;

public abstract class Presenter<T> {
    protected DAO<T> model;

    protected ViewInterface<T> view;

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

    public void add(T object) {
        T newObject = model.add(object);
        if (newObject == null) {
            view.affMsg("Erreur lors de la création de l'élément");
        } else {
            view.affMsg("Création de : " + newObject);
        }

        List<T> objects = model.getAll();
        view.setListDatas(objects);
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
            view.affMsg("Elément modifié");
        } else {
            view.affMsg("Elément non modifié, erreur");
        }

        List<T> objects = model.getAll();
        view.setListDatas(objects);
    }

    public void remove(int idRemove) {
        boolean ok = model.remove(idRemove);

        if (ok) {
            view.affMsg("Elément effacée");
        } else {
            view.affMsg("Elément non effacé, erreur");
        }

        List<T> objects = model.getAll();
        view.setListDatas(objects);
    }

    public T selectionner() {
        T object = view.selectionner(model.getAll());
        return object;
    }
}