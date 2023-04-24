package mvp.model;



import java.util.List;

public interface DAO<T> {

    T add(T objet);

    T read(int idRech);

    boolean update(T ObjetModifie);

    boolean remove(int id);

    List<T> getAll();

}
