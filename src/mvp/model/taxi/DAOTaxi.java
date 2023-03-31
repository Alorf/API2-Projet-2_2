package mvp.model.taxi;

import locationTaxi.Taxi;

import java.util.List;

public interface DAOTaxi {

    Taxi addTaxi(Taxi taxi);

    Taxi readTaxi(int idRech);

    boolean updateTaxi(Taxi taxiModifie);

    boolean removeTaxi(int idTaxi);

    List<Taxi> getTaxis();

}
