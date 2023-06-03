package mvp.model.taxi;

import designpatterns.builder.Location;
import designpatterns.builder.Taxi;

import java.util.List;

public interface TaxiSpecial {

    List<Location> locationTaxi(Taxi taxi);

    int distanceParcouru(Taxi taxi);

}
