package exercicesjdbc;

import myconnections.DBConnection;

import java.sql.*;
import java.util.Scanner;

public class Exercice3 {

    public void exercice() {

        Scanner reader = new Scanner(System.in);
        Connection dbConnect = DBConnection.getConnection();
        if (dbConnect == null) {
            System.exit(1);
        }

        System.out.println("Connexion établie...");

        try(
                Statement stmt = dbConnect.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM API_TCLIENT");
        ){
            while(rs.next()){
                System.out.println(rs.getString("nom"));

                System.out.println(rs.getString("prenom"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Entrez le nom du client");
        String nom = reader.nextLine();

        String query = "SELECT * FROM API_TAXIPARCLIENT WHERE upper(CLIENT) LIKE upper(?)";
        try (
                PreparedStatement pstmt = dbConnect.prepareStatement(query);
        ) {
            pstmt.setString(1, "%" + nom + "%");
            boolean trouve=false;
            try (ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()){
                    trouve=true;
                    System.out.println("\tClient\t\t\t:\tTaxi");
                    System.out.println("--------------------------------------------");
                    System.out.println("\t"+rs.getString(1) + "\t:\t" + rs.getString(2));
                }
                if (!trouve){
                    System.out.println("Aucun résultat pour la vue");
                }

            }
        } catch (SQLException e) {
            System.out.println("Erreur " + e.getMessage());
        }

    }

    public static void main(String[] args) {
        Exercice3 exo = new Exercice3();
        exo.exercice();
    }
}
