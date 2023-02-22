package exercicesjdbc;

import myconnections.DBConnection;

import java.sql.*;
import java.util.Scanner;

public class Exercice4 {

    public void exercice() {

        Scanner reader = new Scanner(System.in);
        Connection dbConnect = DBConnection.getConnection();
        if (dbConnect == null) {
            System.exit(1);
        }

        System.out.println("Connexion établie...");

        try(
                Statement stmt = dbConnect.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT DISTINCT carburant FROM API_TAXI");
        ){
            while(rs.next()){
                System.out.println(rs.getString("carburant"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



        String query = "SELECT AVG(prixkm) as moyenne FROM API_TAXI WHERE upper(carburant)=upper(?)";
        System.out.println("Entrez le type de carburant du taxi : ");
        String car = reader.nextLine();
        try (
                PreparedStatement pstmt = dbConnect.prepareStatement(query);
        ) {
            pstmt.setString(1, car);
            try (ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()){
                    System.out.println(rs.getFloat("moyenne") + " €");
                }

            }
        } catch (SQLException e) {
            System.out.println("Erreur " + e.getMessage());
        }

    }

    public static void main(String[] args) {
        Exercice4 exo = new Exercice4();
        exo.exercice();
    }
}
