package mvp.presenter.taxi;

import designpatterns.builder.Facturation;
import designpatterns.builder.Location;
import designpatterns.builder.Taxi;
import mvp.model.DAO;
import mvp.model.taxi.TaxiSpecial;
import mvp.presenter.Presenter;
import mvp.view.ViewInterface;

import java.util.List;

public class TaxiPresenter extends Presenter<Taxi> implements SpecialTaxiPresenter {

    public TaxiPresenter(DAO<Taxi> model, ViewInterface<Taxi> view) {
        super(model, view);
    }

    @Override
    public Taxi selectionner(List<Facturation> facs) {

        List<Taxi> taxis = model.getAll();

        if (taxis == null || taxis.isEmpty()) {
            return null;
        }

        if (facs != null && !facs.isEmpty()) {
            for (Facturation fac : facs) {
                //On retire les véhicules qui ne nous intéressent plus
                taxis.remove(fac.getVehicule());
            }
        }

        Taxi taxi = view.selectionner(taxis);
        return taxi;
    }

    @Override
    public void locationsTaxi(Taxi taxi) {
        List<Location> locations = ((TaxiSpecial) model).locationTaxi(taxi);

        if (locations == null || locations.isEmpty()) {
            view.affMsg("Aucune location pour ce taxi");
        } else {
            view.affListe(locations);
        }
    }
}