package mvp.model;


import locationTaxi.Adresse;

import java.util.List;

public interface DAO<E> {

    E add(E objet);

    E read(int idRech);

    boolean update(E ObjetModifie);

    boolean remove(int id);

    List<E> get();

}
