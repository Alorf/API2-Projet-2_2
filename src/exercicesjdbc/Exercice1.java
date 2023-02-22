package exercicesjdbc;

import myconnections.DBConnection;

import java.sql.*;
import java.util.Scanner;

public class Exercice1 {

    public void exercice() {

        Scanner reader = new Scanner(System.in);
        Connection dbConnect = DBConnection.getConnection();
        if (dbConnect == null) {
            System.exit(1);
        }

        System.out.println("Connexion établie...");

        try(
                Statement stmt = dbConnect.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM API_TAXI");
        ){
                while(rs.next()){
                    System.out.println(rs.getString("immatriculation"));

                    System.out.println(rs.getString("carburant"));
                }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Entrez la plaque du taxi pour trouver le carburant de celui-ci");
        String plaque = reader.nextLine();

        try (
                PreparedStatement pstmt = dbConnect.prepareStatement(
                        "SELECT * FROM API_TAXI WHERE immatriculation=upper(?)"
                );
        ) {
            pstmt.setString(1, plaque);

            try (ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()){
                    System.out.println(rs.getString("carburant"));
                }

            }
        } catch (SQLException e) {
            System.out.println("Erreur " + e.getMessage());
        }

    }

    public static void main(String[] args) {
        Exercice1 exo = new Exercice1();
        exo.exercice();
    }
}
