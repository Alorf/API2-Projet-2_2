package mvp.model.adresse;


import locationTaxi.Adresse;

import java.util.List;

public interface DAOAdresse {

    Adresse addAdresse(Adresse adresse);

    Adresse readAdresse(int idRech);

    boolean updateAdresse(Adresse adresseModifie);

    boolean removeAdresse(int idAdresse);

    List<Adresse> getAdresses();

}
