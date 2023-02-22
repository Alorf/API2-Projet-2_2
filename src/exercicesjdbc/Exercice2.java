package exercicesjdbc;

import myconnections.DBConnection;

import java.sql.*;
import java.util.Scanner;

public class Exercice2 {

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

        String query = "SELECT * FROM api_tclient WHERE upper(nom)=upper(?)";
        try (
                PreparedStatement pstmt = dbConnect.prepareStatement(query);
        ) {
            pstmt.setString(1, nom);
            boolean trouve=false;
            try (ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()){
                    trouve=true;
                    System.out.println(rs.getString("id_client") + " - " + rs.getString("MAIL") + " - " + rs.getString("nom") + " - " + rs.getString("prenom") + " - " + rs.getString("tel"));

                }
                if (!trouve){
                    System.out.println("le client n'existe pas");
                }

            }
        } catch (SQLException e) {
            System.out.println("Erreur " + e.getMessage());
        }

    }

    public static void main(String[] args) {
        Exercice2 exo = new Exercice2();
        exo.exercice();
    }
}
