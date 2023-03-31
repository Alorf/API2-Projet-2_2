package mvp.model.location;


import locationTaxi.*;
import myconnections.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class LocationModelDB implements DAOLocation, LocationSpecial {

    private Connection dbConnect;

    private static final Logger logger = LogManager.getLogger(LocationModelDB.class);

    public LocationModelDB() {
        dbConnect = DBConnection.getConnection();
        if (dbConnect == null) {
            logger.error("erreur de connexion");
            System.exit(1);
        }
    }

    @Override
    public Location addLocation(Location location) {
        LocalDate dateloc = location.getDate();
        int kmTotal = location.getKmTotal();
        Client client = location.getClient();
        Adresse adresse = location.getAdrDepart();

        String ajoutLoc = "INSERT INTO API_LOCATION(DATELOC, KMTOTAL, ID_ADRESSE, ID_CLIENT) VALUES (?, ?, ?, ?)";
        String getLocId = "SELECT ID_LOCATION FROM API_LOCATION WHERE DATELOC = ? AND KMTOTAL=? AND ID_ADRESSE=? AND ID_CLIENT=?";

        try (PreparedStatement req1 = dbConnect.prepareStatement(ajoutLoc);
             PreparedStatement req2 = dbConnect.prepareStatement(getLocId)
        ) {
            req1.setDate(1, Date.valueOf(dateloc));
            req1.setInt(2, kmTotal);
            req1.setInt(3, adresse.getId());
            req1.setInt(4, client.getId());

            int response = req1.executeUpdate();

            if (response == 1) {
                req2.setDate(2, Date.valueOf(dateloc));
                req2.setInt(3, kmTotal);
                req2.setInt(4, adresse.getId());
                req2.setInt(5, client.getId());
                ResultSet rs = req2.executeQuery();

                if (rs.next()) {
                    int idLoc = rs.getInt(1);
                    location.setId(idLoc);
                    return location;
                } else {
                    logger.error("Record introuvable");
                }
            }

        } catch (SQLException e) {
            logger.error("Erreur sql : " + e);
        }

        return null;
    }

    @Override
    public Location readLocation(int idRech) {
        String query = "SELECT * FROM API_LAFTC WHERE ID_LOCATION = ?";

        try (PreparedStatement req = dbConnect.prepareStatement(query)) {
            req.setInt(1, idRech);
            ResultSet rs = req.executeQuery();

            if (rs.next()) {
                int idClient = rs.getInt("id_client");
                String mailClient = rs.getString("mail");
                String nomClient = rs.getString("nom");
                String prenomClient = rs.getString("prenom");
                String telClient = rs.getString("tel");

                int idLoc = rs.getInt("id_location");
                LocalDate dateloc = rs.getDate("dateloc").toLocalDate();
                int kmTotal = rs.getInt("kmtotal");

                int idAdresse = rs.getInt("id_adresse");
                int cpAdresse = rs.getInt("cp");
                String localiteAdresse = rs.getString("localite");
                String rueAdresse = rs.getString("rue");
                String numeAdresse = rs.getString("rue");


                Client client = new Client(idClient, mailClient, nomClient, prenomClient, telClient);
                Adresse adresse = new Adresse(idAdresse, cpAdresse, localiteAdresse, rueAdresse, numeAdresse);
                Location location = new Location(idLoc, dateloc, kmTotal, client, adresse);
                //todo : Ajout de la liste de facturations ici ?

                return location;

            } else {
                logger.error("Record introuvable");
            }
        } catch (SQLException e) {
            logger.error("Erreur sql : " + e);
        }

        return null;

    }

    @Override
    public boolean updateLocation(Location locationModifie) {

        String query = "UPDATE API_LOCATION SET DATELOC = ?, KMTOTAL = ?, ID_ADRESSE = ?, ID_CLIENT = ? WHERE ID_LOCATION = ?";
        try (PreparedStatement req = dbConnect.prepareStatement(query)) {

            req.setDate(1, Date.valueOf(locationModifie.getDate()));
            req.setInt(2, locationModifie.getKmTotal());
            req.setInt(3, locationModifie.getAdrDepart().getId());
            req.setInt(3, locationModifie.getClient().getId());
            req.setInt(4, locationModifie.getId());

            int res = req.executeUpdate();

            if (res != 0) {
                return true;
            }else{
                logger.error("Record introuvable");
            }
        } catch (SQLException e) {
            logger.error("Erreur sql : " + e);
        }

        return false;
    }

    @Override
    public boolean removeLocation(int idLocation) {

        String deleteQuery = "DELETE FROM API_LOCATION WHERE ID_LOCATION = ?";

        try (PreparedStatement delete = dbConnect.prepareStatement(deleteQuery)) {
            delete.setInt(1, idLocation);
            int response = delete.executeUpdate();

            if (response != 0) {
                return true;
            } else {
                logger.error("Record introuvable");
            }

        } catch (SQLException e) {
            logger.error("Erreur sql : " + e);
        }

        return false;
    }

    @Override
    public List<Location> getLocations() {
        List<Location> ll = new ArrayList<>();
        String query = "SELECT * FROM API_LAFTC WHERE ID_LOCATION IS NOT NULL ORDER BY ID_LOCATION";
        try (PreparedStatement req = dbConnect.prepareStatement(query)) {
            ResultSet rs = req.executeQuery(query);

            while (rs.next()) {

                int idClient = rs.getInt("id_client");
                String mailClient = rs.getString("mail");
                String nomClient = rs.getString("nom");
                String prenomClient = rs.getString("prenom");
                String telClient = rs.getString("tel");

                int idLoc = rs.getInt("id_location");

                LocalDate dateloc = rs.getDate("dateloc").toLocalDate();
                int kmTotal = rs.getInt("kmtotal");

                int idAdresse = rs.getInt("id_adresse");
                int cpAdresse = rs.getInt("cp");
                String localiteAdresse = rs.getString("localite");
                String rueAdresse = rs.getString("rue");
                String numeAdresse = rs.getString("rue");


                Client client = new Client(idClient, mailClient, nomClient, prenomClient, telClient);
                Adresse adresse = new Adresse(idAdresse, cpAdresse, localiteAdresse, rueAdresse, numeAdresse);
                Location loc = new Location(idLoc, dateloc, kmTotal, client, adresse);

                ll.add(loc);
                //todo: Ajouter la liste des facturations, par une autre requête ?

            }

            return ll;
        } catch (SQLException e) {
            logger.error("Erreur sql : " + e);
        }

        return null;
    }

    @Override
    public boolean addFacturation(Location loc, Taxi taxi) {
        String query = "insert into  API_FACTURE values(?,?,?)";
        try (PreparedStatement req = dbConnect.prepareStatement(query)) {
            req.setInt(1, loc.getId());
            req.setInt(2, taxi.getId());
            req.setBigDecimal(3, BigDecimal.valueOf((loc.getKmTotal() * taxi.getPrixKm())));
            int reponse = req.executeUpdate();

            if (reponse != 0){
                return true;
            }


        } catch (SQLException e) {
            logger.error("erreur d'ajout de facturation : " + e);
        }
        return false;
    }
}
