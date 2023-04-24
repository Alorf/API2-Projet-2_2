package mvp.model.location;

import locationTaxi.*;

import java.math.BigDecimal;

public interface LocationSpecial {

    boolean addFacturation(Location loc, Taxi taxi);

    BigDecimal prixTotalLocation(Location location);

}
