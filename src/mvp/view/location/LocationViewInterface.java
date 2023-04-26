package mvp.view.location;

import locationTaxi.metier.Location;
import mvp.presenter.LocationPresenter;

import java.util.List;

public interface LocationViewInterface {
    public void setPresenter(LocationPresenter presenter);

    public void setListDatas(List<Location> Locations);

    public void affMsg(String msg);


}
