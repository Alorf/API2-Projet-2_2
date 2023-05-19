package mvp.view.location;

import designpatterns.builder.Location;
import mvp.presenter.LocationPresenter;

import java.util.List;

public interface LocationViewInterface {
    public void setPresenter(LocationPresenter presenter);

    public void setListDatas(List<Location> Locations);

    public void affMsg(String msg);

    void menu();
}
