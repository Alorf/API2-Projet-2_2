package mvp.model.location;

import locationTaxi.metier.Facturation;
import locationTaxi.metier.Location;
import locationTaxi.metier.Taxi;

import java.math.BigDecimal;
import java.util.List;

public interface LocationSpecial {

    boolean addFacturation(Location loc, Taxi taxi);
    List<Facturation> getFacturations(Location loc);

    BigDecimal prixTotalLocation(Location location);

}
