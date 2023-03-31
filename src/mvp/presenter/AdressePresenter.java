package mvp.presenter;

import locationTaxi.Adresse;
import locationTaxi.Client;
import mvp.model.adresse.DAOAdresse;
import mvp.view.adresse.AdresseViewInterface;

import java.util.List;

public class AdressePresenter {
    private DAOAdresse model;

    private AdresseViewInterface view;

    public AdressePresenter(DAOAdresse model, AdresseViewInterface view) {
        this.model = model;
        this.view = view;
        this.view.setPresenter(this);
    }

    public void start() {
        List<Adresse> adresses = model.getAdresses();
        view.setListDatas(adresses);
    }

    public void addAdresse(Adresse adresse) {
        Adresse cl = model.addAdresse(adresse);
        if (cl == null) {
            view.affMsg("Erreur lors de la création");
        } else {
            view.affMsg("Création de : " + cl);
        }

        List<Adresse> adresses = model.getAdresses();
        view.setListDatas(adresses);

    }

    public Adresse readAdresse(int idRech) {
        Adresse adresse = model.readAdresse(idRech);
        if (adresse == null) {
            System.out.println("Adresse introuvable");
            return null;
        } else {
            System.out.println("Adresse trouvée");
        }

        return adresse;
    }

    public void updateAdresse(Adresse adresse) {
        boolean ok = model.updateAdresse(adresse);

        if (ok) {
            System.out.println("Adresse modifiée");
        } else {
            System.out.println("Adresse non modifiée, erreur");
        }
    }

    public void removeAdresse(int idAdr) {
        boolean ok = model.removeAdresse(idAdr);

        if (ok) {
            view.affMsg("Adresse effacée");
        } else {
            view.affMsg("Adresse non effacée");
        }

        List<Adresse> adresses = model.getAdresses();
        view.setListDatas(adresses);
    }

    public List<Adresse> tout(){
        List<Adresse> lc = model.getAdresses();

        if (lc == null){
            view.affMsg("Aucune adresse dans la base de donnée");
        }

        return lc;
    }

    public Adresse selectionner() {
        Adresse adresse = view.selectionner(model.getAdresses());
        return adresse;
    }
}