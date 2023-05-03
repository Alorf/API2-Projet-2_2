package mvp.view.client;

import designpatterns.builder.Client;
import mvp.presenter.ClientPresenter;

import java.util.List;
import java.util.Set;

public interface ClientViewInterface {
    public void setPresenter(ClientPresenter presenter);

    public void setListDatas(List<Client> clients);

    public void affMsg(String msg);

    public void affListe(List info);

    public void affListe(Set info);

    public Client selectionner(List<Client> clients);
}
