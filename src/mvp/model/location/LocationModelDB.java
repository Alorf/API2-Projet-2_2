package mvp.model.location;

import locationTaxi.metier.*;
import mvp.model.DAO;
import myconnections.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class LocationModelDB implements DAO<Location>, LocationSpecial {

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
    public Location add(Location location) {
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
                req2.setDate(1, Date.valueOf(dateloc));
                req2.setInt(2, kmTotal);
                req2.setInt(3, adresse.getId());
                req2.setInt(4, client.getId());
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
    public Location read(int idRech) {
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
    public boolean update(Location locationModifie) {

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
            } else {
                logger.error("Record introuvable");
            }
        } catch (SQLException e) {
            logger.error("Erreur sql : " + e);
        }

        return false;
    }

    @Override
    public boolean remove(int idLocation) {

        String deleteFacQuery = "DELETE FROM API_FACTURE WHERE ID_LOCATION = ?";
        String deleteLocQuery = "DELETE FROM API_LOCATION WHERE ID_LOCATION = ?";

        try (
                PreparedStatement deleteFac = dbConnect.prepareStatement(deleteFacQuery);
                PreparedStatement deleteLoc = dbConnect.prepareStatement(deleteLocQuery)
        ) {
            deleteFac.setInt(1, idLocation);
            int responseFac = deleteFac.executeUpdate();

            if (responseFac != 0){
                deleteLoc.setInt(1, idLocation);
                int responseLoc = deleteLoc.executeUpdate();

                if (responseLoc != 0) {
                    return true;
                }
            }else{
                logger.error("Record de location introuvable");
            }

        } catch (SQLException e) {
            logger.error("Erreur sql : " + e);
        }

        return false;
    }

    @Override
    public List<Location> getAll() {
        List<Location> ll = new ArrayList<>();
        String query = "SELECT * FROM API_LocationClientAdresse WHERE ID_LOCATION IS NOT NULL";
        try (Statement req = dbConnect.createStatement()) {
            ResultSet rs = req.executeQuery(query);
            rs.next();

            do {
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
                String numAdresse = rs.getString("num");


                Client client = new Client(idClient, mailClient, nomClient, prenomClient, telClient);
                Adresse adresse = new Adresse(idAdresse, cpAdresse, localiteAdresse, rueAdresse, numAdresse);
                Location loc = new Location(idLoc, dateloc, kmTotal, client, adresse);

                List<Facturation> facs = getFacturations(loc);

                loc.setFacturation(facs);

                ll.add(loc);

                //todo: Ajouter la liste des facturations, par une autre requête ?

            } while (rs.next());

            return ll;
        } catch (SQLException e) {
            logger.error("Erreur sql : " + e);
        }

        return null;
    }

    @Override
    public List<Facturation> getFacturations(Location loc) {
        String query = "SELECT * FROM API_FACTURESLOCATION WHERE ID_LOCATION = ?";

        try (PreparedStatement req = dbConnect.prepareStatement(query)) {
            req.setInt(1, loc.getId());
            ResultSet rs = req.executeQuery();
            rs.next();

            List<Facturation> facs = new ArrayList<>();

            do {
                double cout = rs.getInt(1);
                int idTaxi = rs.getInt(2);
                String immatriculation = rs.getString(3);
                String carburant = rs.getString(4);
                double prixKm = rs.getDouble(5);

                facs.add(new Facturation(cout, new Taxi(idTaxi, immatriculation, carburant, prixKm)));

            } while (rs.next());

            return facs;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
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

            if (reponse != 0) {
                return true;
            }


        } catch (SQLException e) {
            logger.error("erreur d'ajout de facturation : " + e);
        }
        return false;
    }

    @Override
    public BigDecimal prixTotalLocation(Location location) {
        //todo : utiliser la fonction
        return null;
    }
}
