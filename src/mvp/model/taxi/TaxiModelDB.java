package mvp.model.taxi;

import locationTaxi.metier.Location;
import locationTaxi.metier.Taxi;
import mvp.model.DAO;
import myconnections.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaxiModelDB implements DAO<Taxi>, TaxiSpecial {

    private Connection dbConnect;

    private static final Logger logger = LogManager.getLogger(TaxiModelDB.class);

    public TaxiModelDB() {
        dbConnect = DBConnection.getConnection();
        if (dbConnect == null) {
            logger.error("erreur de connexion");
            System.exit(1);
        }
    }

    @Override
    public Taxi add(Taxi taxi) {
        String immatriculation = taxi.getImmatriculation();
        String carburant = taxi.getCarburant();
        double prixkm = taxi.getPrixKm();

        String ajoutTax = "INSERT INTO API_TAXI(IMMATRICULATION, CARBURANT, PRIXKM) VALUES (?, ?, ?)";
        String getTaxId = "SELECT ID_TAXI FROM API_TAXI WHERE IMMATRICULATION=?";

        try (PreparedStatement req1 = dbConnect.prepareStatement(ajoutTax);
             PreparedStatement req2 = dbConnect.prepareStatement(getTaxId)
        ) {
            req1.setString(1, immatriculation);
            req1.setString(2, carburant);
            req1.setDouble(3, prixkm);

            int response = req1.executeUpdate();

            if (response == 1) {
                req2.setString(1, immatriculation);
                ResultSet rs = req2.executeQuery();

                if (rs.next()) {
                    int idTax = rs.getInt(1);
                    taxi.setId(idTax);
                    return taxi;
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
    public Taxi read(int idRech) {
        String query = "SELECT * FROM API_TAXI WHERE ID_TAXI = ?";

        try (PreparedStatement req = dbConnect.prepareStatement(query)) {
            req.setInt(1, idRech);
            ResultSet rs = req.executeQuery();

            if (rs.next()) {
                String immatriculation = rs.getString(2);
                String carburant = rs.getString(3);
                double prixkm = rs.getDouble(4);

                Taxi taxi = new Taxi(idRech, immatriculation, carburant, prixkm);
                return taxi;

            } else {
                logger.error("Record introuvable");
            }
        } catch (SQLException e) {
            logger.error("Erreur sql : " + e);
        }

        return null;
    }

    @Override
    public boolean update(Taxi taxiModifie) {

        String query = "UPDATE API_TAXI SET IMMATRICULATION = ?, CARBURANT = ?, PRIXKM = ? WHERE ID_TAXI = ?";
        try (PreparedStatement req = dbConnect.prepareStatement(query)) {

            req.setString(1, taxiModifie.getImmatriculation());
            req.setString(2, taxiModifie.getCarburant());
            req.setDouble(3, taxiModifie.getPrixKm());
            req.setInt(4, taxiModifie.getId());

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
    public boolean remove(int idTaxi) {

        String deleteQuery = "DELETE FROM API_TAXI WHERE ID_TAXI = ?";

        try (PreparedStatement delete = dbConnect.prepareStatement(deleteQuery)) {
            delete.setInt(1, idTaxi);
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
    public List<Taxi> getAll() {
        List<Taxi> lt = new ArrayList<>();
        String query = "SELECT * FROM API_TAXI ORDER BY ID_TAXI";
        try (PreparedStatement req = dbConnect.prepareStatement(query)) {
            ResultSet rs = req.executeQuery(query);

            while (rs.next()) {
                int idTax = rs.getInt(1);
                String immatriculation = rs.getString(2);
                String carburant = rs.getString(3);
                double prixkm = rs.getDouble(4);

                Taxi t = new Taxi(idTax, immatriculation, carburant, prixkm);
                lt.add(t);
            }

            return lt;
        } catch (SQLException e) {
            logger.error("Erreur sql : " + e);
        }

        return null;
    }

    @Override
    public List<Location> locationTaxi(Taxi taxi) {
        //todo : utiliser la fonction
        return null;
    }
}
