package mvp.model.location;


import locationTaxi.Location;

import java.util.List;

public interface DAOLocation {

    Location addLocation(Location location);

    Location readLocation(int idRech);

    boolean updateLocation(Location locationModifie);

    boolean removeLocation(int idLocation);

    List<Location> getLocations();

}
