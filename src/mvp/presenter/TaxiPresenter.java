package mvp.presenter;

import designpatterns.builder.Facturation;
import designpatterns.builder.Location;
import designpatterns.builder.Taxi;
import mvp.model.DAO;
import mvp.model.taxi.TaxiSpecial;
import mvp.view.ViewInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class TaxiPresenter extends Presenter<Taxi> {

    private static final Logger logger = LogManager.getLogger(TaxiPresenter.class);

    public TaxiPresenter(DAO<Taxi> model, ViewInterface<Taxi> view) {
        super(model, view);
    }

    public void start() {
        List<Taxi> taxis = model.getAll();
        view.setListDatas(taxis);
        view.menu();
    }

    public Taxi selectionner(List<Facturation> facs) {
        logger.info("Appel de la sélection");

        List<Taxi> taxis = model.getAll();

        if (facs != null && !facs.isEmpty()){
            for (Facturation fac : facs){
                //On retire les véhicules qui ne nous intéressent plus
                taxis.remove(fac.getVehicule());
            }
        }

        if (taxis.isEmpty()){
            return null;
        }

        Taxi taxi = view.selectionner(taxis);
        return taxi;
    }

    public void locationsTaxi(Taxi taxi){
        List<Location> locations = ((TaxiSpecial) model).locationTaxi(taxi);

        if (locations == null || locations.isEmpty()){
            view.affMsg("Aucune location pour ce taxi");
        }else{
            view.affListe(locations);
        }
    }
}