package mvp.presenter.adresse;

import designpatterns.builder.*;
import mvp.model.DAO;
import mvp.presenter.Presenter;
import mvp.view.ViewInterface;


public class AdressePresenter extends Presenter<Adresse> {

    public AdressePresenter(DAO<Adresse> model, ViewInterface<Adresse> viewInterface) {
        super(model, viewInterface);
    }
}