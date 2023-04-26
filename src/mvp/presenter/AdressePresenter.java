package mvp.presenter;

import locationTaxi.metier.Adresse;
import mvp.model.DAO;
import mvp.view.adresse.AdresseViewInterface;

import java.util.List;

public class AdressePresenter {
    private DAO<Adresse> model;

    private AdresseViewInterface view;

    public AdressePresenter(DAO<Adresse> model, AdresseViewInterface view) {
        this.model = model;
        this.view = view;
        this.view.setPresenter(this);
    }

    public void start() {
        List<Adresse> adresses = model.getAll();
        view.setListDatas(adresses);
    }

    public void addAdresse(Adresse adresse) {
        Adresse cl = model.add(adresse);
        if (cl == null) {
            view.affMsg("Erreur lors de la création");
        } else {
            view.affMsg("Création de : " + cl);
        }

        List<Adresse> adresses = model.getAll();
        view.setListDatas(adresses);
    }

    public Adresse readAdresse(int idRech) {
        Adresse adresse = model.read(idRech);
        if (adresse == null) {
            System.out.println("Adresse introuvable");
            return null;
        } else {
            System.out.println("Adresse trouvée");
        }

        return adresse;
    }

    public void updateAdresse(Adresse adresse) {
        boolean ok = model.update(adresse);

        if (ok) {
            System.out.println("Adresse modifiée");
        } else {
            System.out.println("Adresse non modifiée, erreur");
        }
    }

    public void removeAdresse(int idAdr) {
        boolean ok = model.remove(idAdr);

        if (ok) {
            view.affMsg("Adresse effacée");
        } else {
            view.affMsg("Adresse non effacée");
        }

        List<Adresse> adresses = model.getAll();
        view.setListDatas(adresses);
    }

    public List<Adresse> tout(){
        List<Adresse> lc = model.getAll();

        if (lc == null){
            view.affMsg("Aucune adresse dans la base de donnée");
        }

        return lc;
    }

    public Adresse selectionner() {
        Adresse adresse = view.selectionner(model.getAll());
        return adresse;
    }
}