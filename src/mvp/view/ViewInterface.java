package mvp.view;


import mvp.presenter.Presenter;

import java.util.List;
import java.util.Set;

public interface ViewInterface<T> {
    public void setPresenter(Presenter<T> presenter);

    public void setListDatas(List<T> objects);

    public void affMsg(String msg);

    public void affListe(List info);

    public void affListe(Set info);

    public T selectionner(List<T> objects);

    void menu();
}
