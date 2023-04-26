package mvp.view.taxi;

import locationTaxi.metier.Taxi;
import mvp.presenter.TaxiPresenter;

import java.util.List;

public interface TaxiViewInterface {
    void setPresenter(TaxiPresenter presenter);

    void setListDatas(List<Taxi> taxis);

    void affMsg(String msg);

    Taxi selectionner(List<Taxi> taxis);

}
