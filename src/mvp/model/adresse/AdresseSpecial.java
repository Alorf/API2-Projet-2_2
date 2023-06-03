package mvp.model.adresse;

import designpatterns.builder.Adresse;
import designpatterns.builder.Location;

import java.util.List;

public interface AdresseSpecial {
    List<Location> locationParAdresse(Adresse adresse);

}
