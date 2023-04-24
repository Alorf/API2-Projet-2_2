package mvp.presenter;

import locationTaxi.Facturation;
import locationTaxi.Taxi;
import mvp.model.DAO;
import mvp.view.taxi.TaxiViewInterface;

import java.util.List;

public class TaxiPresenter {
    private DAO<Taxi> model;

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

    public List<Taxi> tout(){
        List<Taxi> lc = model.getAll();

        if (lc == null){
            view.affMsg("Aucun taxi dans la base de donnée");
        }

        return lc;
    }

    public Taxi selectionner(List<Facturation> facs) {
        List<Taxi> taxis = model.getAll();

        for (Facturation fac : facs){
            taxis.remove(fac.getVehicule());
        }

        if (taxis.isEmpty()){
            return null;
        }

        Taxi taxi = view.selectionner(taxis);
        return taxi;
    }
}