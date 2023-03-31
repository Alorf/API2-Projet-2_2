package mvp;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import locationTaxi.Adresse;
import locationTaxi.Client;
import locationTaxi.Location;
import locationTaxi.Taxi;
import myconnections.DBConnection;

public class OldGestionClients {

    private Connection dbConnect;

    public void gestion() {

        dbConnect = DBConnection.getConnection();
        if (dbConnect == null) {
            System.exit(1);
        }

        int choix;

        String[] menu = new String[]{
                "Créer",
                "Rechercher",
                "Modifier",
                "Supprimer",
                "Tous",
                "Finir",
        };

        do {
            choix = Utilitaire.choixListe(Arrays.asList(menu));


            switch (choix) {
                case 1 ->
                    //Créer un client
                        creerClient();
                case 2 ->
                    //Rechercher un client
                        rechercherClient();
                case 3 ->
                    //Modifier un client
                        modifierClient();
                case 4 ->
                    //Supprimer un client
                        supprimerClient();
                case 5 ->
                    //Autre un client
                        tous();
                case 6 ->
                    //Fin
                        System.exit(0);
                default -> System.out.println("Mauvaise saisie, recommencez !");
            }
        } while (true);
    }

    public void creerClient() {
        String mail = Utilitaire.regex("[a-zA-Z.@]+", "Entrez le mail du client : ").toLowerCase();
        String nom = Utilitaire.regex("[a-zA-Z]+", "Entrez le nom du client : ");
        String prenom = Utilitaire.regex("[a-zA-Z]+", "Entrez le prénom du client : ");
        String tel = Utilitaire.regex("[0-9/ +]+", "Entrez le numéro de téléphone du client : ");

        String ajoutCli = "INSERT INTO api_tclient(mail, nom, prenom, tel) VALUES (?, ?, ?, ?)";
        String getCliId = "SELECT id_client FROM api_tclient WHERE mail=?";

        try (PreparedStatement req1 = dbConnect.prepareStatement(ajoutCli);
             PreparedStatement req2 = dbConnect.prepareStatement(getCliId)
        ) {
            req1.setString(1, mail);
            req1.setString(2, nom);
            req1.setString(3, prenom);
            req1.setString(4, tel);

            int response = req1.executeUpdate();
            System.out.println(response + " ligne insérée");

            if (response == 1) {
                req2.setString(1, mail);
                ResultSet rs = req2.executeQuery();

                if (rs.next()) {
                    int idCli = rs.getInt(1);
                    System.out.println("Id client : " + idCli);
                } else {
                    System.out.println("Record introuvable");
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur sql : " + e);
        }
    }

    public void rechercherClient() {
        int idRech = Integer.parseInt(Utilitaire.regex("[0-9]+", "Id du client recherché : "));
        String query = "SELECT * FROM api_tclient WHERE id_client = ?";

        try (PreparedStatement req = dbConnect.prepareStatement(query)) {
            req.setInt(1, idRech);
            ResultSet rs = req.executeQuery();

            if (rs.next()) {
                String mail = rs.getString(2);
                String nom = rs.getString(3);
                String prenom = rs.getString(4);
                String tel = rs.getString(5);

                Client cl = new Client(idRech, mail, nom, prenom, tel);
                System.out.println(cl);

                opSpeciales(cl);
            } else {
                System.out.println("Record introuvable");
            }
        } catch (SQLException e) {
            System.err.println("Erreur sql : " + e);
        }

    }

    //Cette méthode permet de prévoir le cas où le client n'existe pas et donc éviter toute entrée superflue
    public boolean clientExiste(int idCli){
        String query = "SELECT * FROM api_tclient WHERE id_client=?";
        try(PreparedStatement req = dbConnect.prepareStatement(query)){
            req.setInt(1, idCli);
            ResultSet rs = req.executeQuery();

            return rs.next();

        }catch (SQLException e){
            System.err.println("Erreur sql : " + e);
        }

        return true;
    }

    public void modifierClient() {
        int idCli = Integer.parseInt(Utilitaire.regex("[0-9]+", "Entrez l'identifiant du client : "));

        if (!clientExiste(idCli)){
            System.out.println("Le client n'existe pas");
            return;
        }

        String query = "";
        String newValue = "";

        String[] menu = {
                "Mail",
                "Nom",
                "Prénom",
                "Numéro de téléphone",
                "Sortir"
        };

        int choix;
        System.out.println("Que souhaitez vous modifier");

        do {
            choix = Utilitaire.choixListe(Arrays.asList(menu));


            switch (choix) {

                case 1 -> {
                    //Modifier mail
                    newValue = Utilitaire.regex("[a-zA-Z.@]+", "Entrez le nouveau mail : ").toLowerCase();
                    query = "UPDATE api_tclient SET mail = ? WHERE id_client = ?";
                    update(query, newValue, idCli);

                }
                case 2 -> {
                    //Modifier nom
                    newValue = Utilitaire.regex("[a-zA-Z]+", "Entrez le nouveau nom : ");
                    query = "UPDATE api_tclient SET nom = ? WHERE id_client = ?";
                    update(query, newValue, idCli);

                }
                case 3 -> {
                    //Modifier prenom
                    newValue = Utilitaire.regex("[a-zA-Z]+", "Entrez le nouveau prénom : ");
                    query = "UPDATE api_tclient SET prenom = ? WHERE id_client = ?";
                    update(query, newValue, idCli);


                }
                case 4 -> {
                    //Modifier téléphone
                    newValue = Utilitaire.regex("[0-9/ +]+", "Entrez le nouveau numéro de téléphone du client : ");
                    query = "UPDATE api_tclient SET tel = ? WHERE id_client = ?";
                    update(query, newValue, idCli);


                }
                case 5 -> {
                    System.out.println("Retour au menu");
                    return;

                }
                default -> System.out.println("Mauvaise saisie, recommencez !");
            }

        } while (true);
    }

    public void update(String query, String newValue, int idCli){

        try (PreparedStatement req = dbConnect.prepareStatement(query)) {

            req.setString(1, newValue);
            //Si j'ai une table avec des types différents, il serait plus judicieux de mettre setObject pour réutiliser cette fonction
            req.setInt(2, idCli);

            int res = req.executeUpdate();

            if (res != 0) {
                System.out.println(res + " ligne mise à jour");
            }else{
                //Je garde le print si jamais un soucis intervient
                System.out.println("Record introuvable");
            }
        } catch (SQLException e) {
            System.err.println("Erreur sql : " + e);
        }
    }

    public void supprimerClient() {

        int idCli = Integer.parseInt(Utilitaire.regex("[0-9]+", "Entrez l'id du client que vous souhaitez supprimer : "));

        if (!clientExiste(idCli)){
            System.out.println("Le client n'existe pas, retour");
            return;
        }

        String cont = Utilitaire.regex("[12onON]", "Voulez-vous vraiment supprimer ce client ? o/n : ");
        if (cont.equalsIgnoreCase("n") || cont.equals("2")) {
            System.out.println("Annulation de la suppression");
            return;
        }

        String deleteQuery = "DELETE FROM api_tclient WHERE id_client = ?";

        try (PreparedStatement delete = dbConnect.prepareStatement(deleteQuery)) {
            delete.setInt(1, idCli);
            int response = delete.executeUpdate();

            if (response != 0) {
                System.out.println(response + " ligne supprimée");
            } else {
                //Je garde tout de même le print si jamais un soucis intervient
                System.out.println("Record introuvable");
            }

        } catch (SQLException e) {
            System.err.println("Erreur sql : " + e);
        }
    }

    public void tous() {
        String query = "SELECT * FROM api_tclient";
        try (PreparedStatement req = dbConnect.prepareStatement(query)) {
            ResultSet rs = req.executeQuery(query);

            while (rs.next()) {
                int idCli = rs.getInt(1);
                String mail = rs.getString(2);
                String nom = rs.getString(3);
                String prenom = rs.getString(4);
                String tel = rs.getString(5);

                Client cl = new Client(idCli, mail, nom, prenom, tel);
                System.out.println(cl);
            }
        } catch (SQLException e) {
            System.err.println("Erreur sql : " + e);
        }
    }

    private void opSpeciales(Client client) {
        System.out.println("Que voulez-vous faire ?");
        String[] menu = {
                "Tous les taxis utilisés sans doublon",
                "Toutes les locations entre deux dates",
                "Toutes les adresses où il s'est rendu sans doublon"
        };
        int choix;

        do {
            choix = Utilitaire.choixListe(Arrays.asList(menu));


        } while (choix < 1 || choix > menu.length);


        switch (choix) {
            case 1 ->
                //Tous les taxis utilisés sans doublon
                    taxiUtiliseSansDoublon(client);
            case 2 ->
                //Toutes les locations entre deux dates
                    locationEntreDeuxDates(client);
            case 3 ->
                //Toutes les adresses où il s'est rendu sans doublon
                    adresseLocationSansDoublon(client);
        }

    }

    private void taxiUtiliseSansDoublon(Client client) {
        String query = "SELECT ID_TAXI, IMMATRICULATION, CARBURANT, PRIXKM FROM API_TAXIPARCLIENT tpc JOIN api_taxi t ON  tpc.TAXI = t.IMMATRICULATION WHERE client=?";

        try (PreparedStatement req = dbConnect.prepareStatement(query)) {
            req.setString(1, client.getNom() + " " + client.getPrenom());
            ResultSet rs = req.executeQuery();

            boolean trouve = false;
            while (rs.next()){
                trouve=true;
                Taxi taxi = new Taxi(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4));
                System.out.println(taxi);
            }

            if (!trouve){
                System.out.println("Aucun taxi trouvée pour ce client");
            }
        } catch (SQLException e) {
            System.err.println("Erreur sql : " + e);
        }

    }

    private void locationEntreDeuxDates(Client client){
        LocalDate d1 = Utilitaire.lecDate();
        LocalDate d2 = Utilitaire.lecDate();

        String query = "SELECT * FROM API_LOCATION WHERE id_client = ? AND DATELOC BETWEEN to_date(?) AND to_date(?)";

        try (PreparedStatement req = dbConnect.prepareStatement(query)) {
            req.setInt(1, client.getId());
            req.setDate(2, Date.valueOf(d1));
            req.setDate(3, Date.valueOf(d2));
            ResultSet rs = req.executeQuery();

            boolean trouve = false;
            while (rs.next()){
                trouve=true;
                Location loc = new Location(rs.getInt(1), rs.getDate(2).toLocalDate(), rs.getInt(3), client, null);
                System.out.println(loc);
            }

            if (!trouve){
                System.out.println("Aucune location ne correspond aux deux dates");
            }
        } catch (SQLException e) {
            System.err.println("Erreur sql : " + e);
        }
    }

    private void adresseLocationSansDoublon(Client client){
        String query = "SELECT DISTINCT api_adresse.id_adresse, cp, localite, rue, num FROM API_ADRESSE JOIN API_LOCATION AL on API_ADRESSE.ID_ADRESSE = AL.ID_ADRESSE WHERE id_client = ?";

        try (PreparedStatement req = dbConnect.prepareStatement(query)) {
            req.setInt(1, client.getId());

            ResultSet rs = req.executeQuery();

            boolean trouve = false;
            while (rs.next()){
                trouve=true;
                Adresse Adresse = new Adresse(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5));
                System.out.println(Adresse);
            }

            if (!trouve){
                System.out.println("Aucune location trouvée pour ce client");
            }
        } catch (SQLException e) {
            System.err.println("Erreur sql : " + e);
        }
    }

    public static void main(String[] args) {
        OldGestionClients g = new OldGestionClients();
        g.gestion();
    }
}
