package mvp.presenter;

import designpatterns.builder.Adresse;
import designpatterns.builder.Client;
import mvp.model.DAO;
import mvp.model.taxi.TaxiModelDB;
import mvp.view.adresse.AdresseViewInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class AdressePresenter {
    private DAO<Adresse> model;

    private AdresseViewInterface view;

    private static final Logger logger = LogManager.getLogger(AdressePresenter.class);

    public AdressePresenter(DAO<Adresse> model, AdresseViewInterface view) {
        this.model = model;
        this.view = view;
        this.view.setPresenter(this);
    }

    public void start() {
        List<Adresse> adresses = model.getAll();
        view.setListDatas(adresses);
        view.menu();
    }

    public void addAdresse(Adresse adresse) {
        Adresse ad = model.add(adresse);
        if (ad == null) {
            view.affMsg("Erreur lors de la création");
        } else {
            view.affMsg("Création de : " + ad);
        }

        List<Adresse> adresses = model.getAll();
        view.setListDatas(adresses);
    }

    public Adresse readAdresse(int idRech) {
        Adresse adresse = model.read(idRech);

        if (adresse == null) {
            view.affMsg("Adresse introuvable");
        }

        return adresse;
    }

    public void updateAdresse(Adresse adresse) {
        boolean ok = model.update(adresse);

        if (ok) {
            view.affMsg("Adresse modifiée");
        } else {
            view.affMsg("Adresse non modifiée, erreur");
        }

        List<Adresse> adresses = model.getAll();
        view.setListDatas(adresses);
    }

    public void removeAdresse(int idAdr) {
        boolean ok = model.remove(idAdr);

        if (ok) {
            view.affMsg("Adresse effacée");
        } else {
            view.affMsg("Adresse non effacée, erreur");
        }

        List<Adresse> adresses = model.getAll();
        view.setListDatas(adresses);
    }

    public Adresse selectionner() {
        logger.info("Appel de la sélection");

        List<Adresse> adresses = model.getAll();

        if (adresses == null || adresses.isEmpty()) {
            view.affMsg("Aucune adresse");
            return null;
        }

        Adresse adresse = view.selectionner(adresses);
        return adresse;
    }
}