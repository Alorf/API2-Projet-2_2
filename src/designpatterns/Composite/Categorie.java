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
        sb.append("\nCategorie{" + "id=" + id + ", nom=" + nom + "} ").append(nombreVehicule()).append(" véhicules \n");
        for (Element e : elements) {
            sb.append(e.toString());
            //Si catégorie, alors il va rappeller le toString de la catégorie => récursivité
        }
        return sb.toString();
    }

    @Override
    public int nombreVehicule() {
        int nbr = 0;
        for (Element e : elements){
            if (e instanceof Taxi){
                nbr++;
            }
        }
        return nbr;
    }


}
