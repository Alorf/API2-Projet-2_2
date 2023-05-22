package designpatterns.Composite;

import java.util.HashSet;
import java.util.Set;

public class Categorie extends Element {
    String nom;
    //Type de véhicule
    Set<Element> elements = new HashSet<>();
    // véhicules de la catégorie

    public Categorie(int id, String nom) {
        super(id);
        this.nom = nom;
    }

    public Set<Element> getElements() {
        return elements;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nCategorie{" + "id=" + id + ", nom=" + nom + "}\n");
        for (Element e : elements) {
            sb.append(e.toString() + "\n");
            //Si catégorie, alors il va rappeller le toString de la catégorie => récursivité donc beaucoup de retour a la ligne...
        }
        return sb.toString();
    }
}
