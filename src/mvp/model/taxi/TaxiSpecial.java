package mvp.model.taxi;

import locationTaxi.metier.Location;
import locationTaxi.metier.Taxi;

import java.util.List;

public interface TaxiSpecial {

    List<Location> locationTaxi(Taxi taxi);

}
