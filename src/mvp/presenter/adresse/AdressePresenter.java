package mvp.presenter.adresse;

import designpatterns.builder.*;
import mvp.model.DAO;
import mvp.model.adresse.AdresseSpecial;
import mvp.presenter.Presenter;
import mvp.view.ViewInterface;

import java.util.List;


public class AdressePresenter extends Presenter<Adresse> implements SpecialAdressePresenter {

    public AdressePresenter(DAO<Adresse> model, ViewInterface<Adresse> viewInterface) {
        super(model, viewInterface);
    }

    @Override
    public void locationParAdresse(Adresse adresse) {
        List<Location> locs = ((AdresseSpecial) model).locationParAdresse(adresse);

        if (locs == null || locs.isEmpty()){
            view.affMsg("Pas de location pour cette adresse");
        } else{
            view.affListe(locs);
        }

    }
}