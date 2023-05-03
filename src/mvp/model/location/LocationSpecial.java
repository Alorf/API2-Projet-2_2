package mvp.model.location;

import designpatterns.builder.Facturation;
import designpatterns.builder.Location;
import designpatterns.builder.Taxi;

import java.math.BigDecimal;
import java.util.List;

public interface LocationSpecial {

    boolean addFacturation(Location loc, Taxi taxi);
    List<Facturation> getFacturations(Location loc);

    BigDecimal prixTotalLocation(Location location);

}
