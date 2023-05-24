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
                System.out.println("Recommencez votre entrée !");
            }
        } while (!chaine.matches(regex));

        return chaine;
    }

    //Provient de Monsieur Poriaux
    public static LocalDate lecDate() {

        while (true) {
            try {
                String splitBy = "";
                String date = regex("^(0?[1-9]|[12][0-9]|3[01])[\\/\\s](0?[1-9]|1[0-2])[\\/\\s](\\d{4})$", "Entrez la date : ");
                String[] jma = null;

                if (date.contains("/")) {
                    splitBy = "/";
                }

                jma = date.split(splitBy);

                int j = Integer.parseInt(jma[0]);
                int m = Integer.parseInt(jma[1]);
                int a = Integer.parseInt(jma[2]);

                return LocalDate.of(a, m, j);
            } catch (Exception e) {
                System.out.println("Erreur d'entrée de date, recommencez");
            }
        }
    }

    public static void afficherListe(List objs) {
        if (objs != null) {
            int i = 0;
            for (Object obj : objs) {

                System.out.println(++i + ". " + obj);
            }
        }
    }

    public static void afficherListe(Set objs) {
        if (objs != null) {
            int i = 0;
            Iterator it = objs.iterator();
            while (it.hasNext()) {
                System.out.println(++i + " " + it.next());
            }
        }
    }

    public static int choixListe(List objs) {
        System.out.println("");
        int choix;

        do {
            afficherListe(objs);
            choix = Integer.parseInt(regex("[0-9]+", "Choisissez un élément de la liste : "));

            if (choix < 0 || choix > objs.size()) {
                System.out.println("\nChoix incorrect !\n");
            } else {
                break;
            }

        } while (true);
        System.out.println("");

        return choix;
    }

    //Provient de Monsieur Poriaux
    public static double lireDouble(String msg) {
        msg = msg.endsWith(" ") ? msg : msg + " ";
        double n = 0;
        do {
            try {
                System.out.print(msg);
                String ns = reader.nextLine();
                n = Double.parseDouble(ns);
                return n;
            } catch (NumberFormatException e) {
                System.out.println("Erreur de saisie, recommencez");
            }

        } while (true);
    }
}
