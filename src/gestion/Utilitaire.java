package gestion;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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

    public static void afficherMenu(List<String> menu) {
        int i = 0;
        System.out.println("");
        for (String menuTemp : menu) {
            System.out.println((i + 1) + ". " + menuTemp);
            i++;
        }
        System.out.println("");
    }

    //Provient de Monsieur Poriaux
    public static LocalDate lecDate(){
        String splitBy = "";
        String date = regex("^((0[1-9]|1[0-2])[\\/ ]){2}[0-9]{2,4}$", "Entrez la date : ");
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
}
