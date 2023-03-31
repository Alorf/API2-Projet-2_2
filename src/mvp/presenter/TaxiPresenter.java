package mvp.presenter;

import locationTaxi.Adresse;
import locationTaxi.Taxi;
import mvp.model.taxi.DAOTaxi;
import mvp.view.taxi.TaxiViewInterface;

import java.util.List;

public class TaxiPresenter {
    private DAOTaxi model;

    private TaxiViewInterface view;

    public TaxiPresenter(DAOTaxi model, TaxiViewInterface view) {
        this.model = model;
        this.view = view;
        this.view.setPresenter(this);
    }

    public void start() {
        List<Taxi> taxis = model.getTaxis();
        view.setListDatas(taxis);
    }

    public void addTaxi(Taxi taxi) {
        Taxi cl = model.addTaxi(taxi);
        if (cl == null) {
            view.affMsg("Erreur lors de la création");
        } else {
            view.affMsg("Création de : " + cl);
        }

        List<Taxi> taxis = model.getTaxis();
        view.setListDatas(taxis);

    }

    public Taxi readTaxi(int idRech) {
        Taxi taxi = model.readTaxi(idRech);
        if (taxi == null) {
            System.out.println("Taxi introuvable");
            return null;
        } else {
            System.out.println("Taxi trouvé");
        }

        return taxi;
    }

    public void updateTaxi(Taxi taxi) {
        boolean ok = model.updateTaxi(taxi);

        if (ok) {
            System.out.println("Taxi modifié");
        } else {
            System.out.println("Taxi non modifié, erreur");
        }
    }

    public void removeTaxi(int idTaxi) {
        boolean ok = model.removeTaxi(idTaxi);

        if (ok) {
            view.affMsg("Taxi effacé");
        } else {
            view.affMsg("Taxi non effacé");
        }

        List<Taxi> taxis = model.getTaxis();
        view.setListDatas(taxis);
    }

    public List<Taxi> tout(){
        List<Taxi> lc = model.getTaxis();

        if (lc == null){
            view.affMsg("Aucun taxi dans la base de donnée");
        }

        return lc;
    }

    public Taxi selectionner() {
        Taxi taxi = view.selectionner(model.getTaxis());
        return taxi;
    }
}