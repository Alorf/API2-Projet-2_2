package mvp.presenter;

import designpatterns.builder.Facturation;
import designpatterns.builder.Location;
import designpatterns.builder.Taxi;
import mvp.model.DAO;
import mvp.model.taxi.TaxiSpecial;
import mvp.view.taxi.TaxiViewInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class TaxiPresenter {
    private DAO<Taxi> model;

    private static final Logger logger = LogManager.getLogger(TaxiPresenter.class);

    private TaxiViewInterface view;

    public TaxiPresenter(DAO<Taxi> model, TaxiViewInterface view) {
        this.model = model;
        this.view = view;
        this.view.setPresenter(this);
    }

    public void start() {
        List<Taxi> taxis = model.getAll();
        view.setListDatas(taxis);
    }

    public void addTaxi(Taxi taxi) {
        Taxi cl = model.add(taxi);
        if (cl == null) {
            view.affMsg("Erreur lors de la création");
        } else {
            view.affMsg("Création de : " + cl);
        }

        List<Taxi> taxis = model.getAll();
        view.setListDatas(taxis);

    }

    public Taxi readTaxi(int idRech) {
        Taxi taxi = model.read(idRech);
        if (taxi == null) {
            System.out.println("Taxi introuvable");
            return null;
        } else {
            System.out.println("Taxi trouvé");
        }

        return taxi;
    }

    public void updateTaxi(Taxi taxi) {
        boolean ok = model.update(taxi);

        if (ok) {
            System.out.println("Taxi modifié");
        } else {
            System.out.println("Taxi non modifié, erreur");
        }
    }

    public void removeTaxi(int idTaxi) {
        boolean ok = model.remove(idTaxi);

        if (ok) {
            view.affMsg("Taxi effacé");
        } else {
            view.affMsg("Taxi non effacé");
        }

        List<Taxi> taxis = model.getAll();
        view.setListDatas(taxis);
    }

    public Taxi selectionner(List<Facturation> facs) {
        logger.info("Appel de la sélection");

        List<Taxi> taxis = model.getAll();

        for (Facturation fac : facs){
            //On retire les véhicules qui ne nous intéressent plus
            taxis.remove(fac.getVehicule());
        }

        if (taxis.isEmpty()){
            view.affMsg("Aucun taxi disponnible");
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