package designpatterns.Composite;

import java.util.HashSet;
import java.util.Set;

public class Categorie extends Element {
    String nom;
    //Type de véhicule
    Set<Element> elements = new HashSet<>();

    public Categorie(int id, String nom) {
        super(id);
        this.nom = nom;
    }

    public Set<Element> getElements() {
        return elements;
    }

    @Override
    public String toString() {
        return nom + " " + elements.size() + " véhicules";
    }
}
