package mvp.presenter.taxi;

import designpatterns.builder.Facturation;
import designpatterns.builder.Taxi;

import java.util.List;

public interface SpecialTaxiPresenter {

    Taxi selectionner(List<Facturation> facs);

    void locationsTaxi(Taxi taxi);

    void distanceParcouru(Taxi taxi);

}