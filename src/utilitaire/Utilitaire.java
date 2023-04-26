package utilitaire;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Utilitaire {
    private static Scanner reader = new Scanner(System.in);

    public static String regex(String regex, String msg) {
        String chaine;
        msg = msg.endsWith(" ") ? msg : msg + " ";
        do {
            System.out.printf(msg);
            chaine = reader.nextLine();
            if (!chaine.matches(regex)) {
                System.out.println("Recommencez !");
            }
        } while (!chaine.matches(regex));

        return chaine;
    }

    //Provient de Monsieur Poriaux
    public static LocalDate lecDate(){
        String splitBy = "";
        String date = regex("^(0?[1-9]|[12][0-9]|3[01])[\\/\\s](0?[1-9]|1[0-2])[\\/\\s](\\d{4})$", "Entrez la date : ");
        String[] jma = null;

        if (date.contains("/")){
            splitBy = "/";
        }

        jma = date.split(splitBy);

        int j = Integer.parseInt(jma[0]);
        int m = Integer.parseInt(jma[1]);
        int a = Integer.parseInt(jma[2]);

        return LocalDate.of(a,m,j);
    }

    //Provient de Monsieur Poriaux
    public static LocalTime lecTime(){
        String splitBy = " ";
        String heure = regex("^[0-9]{1,2}[:][0-9]{2}[:][0-9]{2}$", "Entrez l'heure : ");
        String[] hms = null;

        if (heure.contains(":")){
            splitBy = ":";
        }

        hms = heure.split(splitBy);

        int h = Integer.parseInt(hms[0]);
        int m = Integer.parseInt(hms[1]);
        int s = Integer.parseInt(hms[2]);

        return LocalTime.of(h,m,s);
    }

    public static void afficherListe(List objs){
        if (objs != null){
            int i = 0;
            for (Object obj : objs){

                System.out.println(++i + ". " +obj);
            }
        }
    }

    public static void afficherListe(Set objs){
        if (objs != null){
            int i = 0;
            for (Object obj : objs){

                System.out.println(++i + ". " +obj);
            }
        }
    }

    public static int choixListe(List objs){
        System.out.println("");
        int choix;

        do {
            afficherListe(objs);
            choix = Integer.parseInt(regex("[0-9]+", "Choisissez un élément de la liste : "));
        }while (choix < 0 || choix > objs.size());
        System.out.println("");

        return choix;
    }
}
