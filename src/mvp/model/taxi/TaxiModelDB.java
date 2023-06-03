package mvp.model.taxi;

import designpatterns.builder.*;
import mvp.model.DAO;
import myconnections.DBConnection;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
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
                    logger.error("Record introuvable lors de l'ajout");
                }
            }

        } catch (SQLException e) {
            logger.error("Erreur sql lors de l'ajout : " + e);
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

                Taxi taxi = new Taxi.TaxiBuilder()
                        .setId(idRech)
                        .setImmatriculation(immatriculation)
                        .setCarburant(carburant)
                        .setPrixKm(prixkm)
                        .build();

                return taxi;

            } else {
                logger.error("Record introuvable lors du read");
            }
        } catch (SQLException e) {
            logger.error("Erreur sql lors du read : " + e);
        } catch (Exception e) {
            logger.error("Erreur Builder lors du read : " + e);
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
            } else {
                logger.error("Record introuvable lors de l'update");
            }
        } catch (SQLException e) {
            logger.error("Erreur sql lors de l'update : " + e);
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
                logger.error("Record introuvable lors du remove");
            }

        } catch (SQLException e) {
            logger.error("Erreur sql lors du remove : " + e);
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

                Taxi t = new Taxi.TaxiBuilder()
                        .setId(idTax)
                        .setImmatriculation(immatriculation)
                        .setCarburant(carburant)
                        .setPrixKm(prixkm)
                        .build();

                lt.add(t);
            }

            return lt;
        } catch (SQLException e) {
            logger.error("Erreur sql lors du getAll : " + e);
        } catch (Exception e) {
            logger.error("Erreur Builder lors du getAll : " + e);
        }

        return null;
    }

    @Override
    public List<Location> locationTaxi(Taxi taxi) {
        //Appel de fonction SGBD (2)
        String nombreLoc = "{? = call api_fonc_locations_taxi(?)}";

        try (CallableStatement cs = dbConnect.prepareCall(nombreLoc)) {
            cs.registerOutParameter(1, OracleTypes.CURSOR);

            cs.setString(2, taxi.getImmatriculation());
            cs.execute();

            ResultSet rs = ((OracleCallableStatement) cs).getCursor(1);
            List<Location> locations = new ArrayList<>();

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
                String numAdresse = rs.getString("num");


                Client client = new Client.ClientBuilder()
                        .setId(idClient)
                        .setMail(mailClient)
                        .setNom(nomClient)
                        .setPrenom(prenomClient)
                        .setTel(telClient)
                        .build();

                Adresse adresse = new Adresse.AdresseBuilder()
                        .setId(idAdresse)
                        .setCp(cpAdresse)
                        .setLocalite(localiteAdresse)
                        .setRue(rueAdresse)
                        .setNum(numAdresse)
                        .build();

                Location location = new Location.LocationBuilder()
                        .setId(idLoc)
                        .setDate(dateloc)
                        .setKmTotal(kmTotal)
                        .setClient(client)
                        .setAdrDepart(adresse)
                        .build(false);

                locations.add(location);
            }

            return locations;

        } catch (SQLException e) {
            logger.error("Erreur sql lors de locationTaxi : " + e);
        } catch (Exception e) {
            logger.error("Erreur Builder lors de locationTaxi : " + e);
        }

        return null;
    }

    @Override
    public int distanceParcouru(Taxi taxi) {
        int distance = 0;
        String query = "SELECT * FROM API_KMPARTAXI WHERE ID_TAXI = ?";

        try (PreparedStatement req = dbConnect.prepareStatement(query)) {
            req.setInt(1, taxi.getId());

            ResultSet rs = req.executeQuery();
            boolean trouve = false;
            while (rs.next()) {
                trouve = true;
                distance = rs.getInt(3);
            }

            if (!trouve) {
                logger.error("Record introuvable");
            } else {
                return distance;
            }
        } catch (SQLException e) {
            logger.error("Erreur sql : " + e);
        } catch (Exception e) {
            logger.error("Erreur Builder : " + e);
        }

        return -1;
    }
}
