package mvp.model.location;

import designpatterns.builder.*;
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
        /*Appel procédure embarquée
        Cette procédure me permet de récupéré le bon ID d'une location
        En effet, si l'utilisateur fait 2x une location avec des information identiques dans la même journée, cela renvoie plusieurs ligne
        Ici je suis sur et certains de récupérer le bon ID
         */

        LocalDate dateloc = location.getDate();
        int kmTotal = location.getKmTotal();
        Client client = location.getClient();
        Adresse adresse = location.getAdrDepart();

        String ajoutLoc = "CALL API_PROC_CREATE_LOCATION(?, ?, ?, ?, ?)";

        try (CallableStatement cs = dbConnect.prepareCall(ajoutLoc)) {
            cs.setDate(1, Date.valueOf(dateloc));
            cs.setInt(2, kmTotal);
            cs.setInt(3, adresse.getId());
            cs.setInt(4, client.getId());

            cs.registerOutParameter(5, Types.INTEGER);

            cs.executeUpdate();

            int idLoc = cs.getInt(5);
            location.setId(idLoc);

            return location;


        } catch (SQLException e) {
            logger.error("Erreur sql lors de l'ajout : " + e);
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

                List<Facturation> facs = getFacturations(location);

                location.setFacturation(facs);

                return location;

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
    public boolean update(Location locationModifie) {

        String query = "UPDATE API_LOCATION SET DATELOC = ?, KMTOTAL = ?, ID_ADRESSE = ?, ID_CLIENT = ? WHERE ID_LOCATION = ?";
        try (PreparedStatement req = dbConnect.prepareStatement(query)) {

            req.setDate(1, Date.valueOf(locationModifie.getDate()));
            req.setInt(2, locationModifie.getKmTotal());
            req.setInt(3, locationModifie.getAdrDepart().getId());
            req.setInt(4, locationModifie.getClient().getId());
            req.setInt(5, locationModifie.getId());

            int res = req.executeUpdate();

            if (res != 0) {
                return true;
            } else {
                logger.error("Record introuvable lors de l'update");
            }
        } catch (SQLException e) {
            logger.error("Erreur sql lors de l'update' : " + e);
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

            if (responseFac == 0) {
                logger.info("Pas de facturations trouvées pour la location : " + idLocation);
            }

            deleteLoc.setInt(1, idLocation);
            int responseLoc = deleteLoc.executeUpdate();

            if (responseLoc != 0) {
                return true;
            }


        } catch (SQLException e) {
            logger.error("Erreur sql lors du remove : " + e);
        }

        return false;
    }

    @Override
    public List<Location> getAll() {
        List<Location> ll = new ArrayList<>();
        String query = "SELECT * FROM API_LOCATIONCLIENTADRESSE WHERE ID_LOCATION IS NOT NULL";
        try (Statement req = dbConnect.createStatement()) {
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

                Location loc = new Location.LocationBuilder()
                        .setId(idLoc)
                        .setDate(dateloc)
                        .setKmTotal(kmTotal)
                        .setClient(client)
                        .setAdrDepart(adresse)
                        .build(false);

                ll.add(loc);
            }

            return ll;
        } catch (SQLException e) {
            logger.error("Erreur sql lors du getAll : " + e);
        } catch (Exception e) {
            logger.error("Erreur Builder lors du getAll : " + e);
        }

        return null;
    }

    @Override
    public List<Facturation> getFacturations(Location loc) {
        String query = "SELECT * FROM API_FACTURESLOCATION WHERE ID_LOCATION = ?";

        try (PreparedStatement req = dbConnect.prepareStatement(query)) {
            req.setInt(1, loc.getId());
            ResultSet rs = req.executeQuery();

            List<Facturation> facs = new ArrayList<>();

            while (rs.next()) {
                double cout = rs.getInt(1);
                int idTaxi = rs.getInt(2);
                String immatriculation = rs.getString(3);
                String carburant = rs.getString(4);
                double prixKm = rs.getDouble(5);

                Taxi taxi = new Taxi.TaxiBuilder()
                        .setId(idTaxi)
                        .setImmatriculation(immatriculation)
                        .setCarburant(carburant)
                        .setPrixKm(prixKm)
                        .build();

                Facturation fac = new Facturation.FacturationBuilder()
                        .setCout(cout)
                        .setVehicule(taxi)
                        .build();


                facs.add(fac);

            }

            return facs;
        } catch (SQLException e) {
            logger.error("Erreur sql lors du getFacturations : " + e);
        } catch (Exception e) {
            logger.error("Erreur builder lors du getFacturations : " + e);
        }

        return null;
    }

    @Override
    public boolean addFacturation(Location loc, Taxi taxi) {
        //Utilisation du trigger de SGBD (7) pour calculer le cout de la location ainsi que la procédure d'ajout de facture
        // Si un taxi est utilisé plus de 10x dans la journée, il ne sera pas disponible pour la location
        //String query = "INSERT INTO  API_FACTURE(id_location, id_taxi) values(?,?)";
        String query = "CALL api_proc_insert_fac(?, ?, ?)";
        try (CallableStatement cs = dbConnect.prepareCall(query)) {
            cs.setInt(1, taxi.getId());
            cs.setInt(2, loc.getId());
            cs.setDate(3, Date.valueOf(loc.getDate()));

            boolean reponse = cs.execute();

            if (!reponse) {
                return true;
            }


        } catch (SQLException e) {
            logger.error("Erreur sql lors de addFacturation : " + e);
        }
        return false;
    }

    @Override
    public boolean removeFacturation(int idLoc, int idVehicule) {
        String deleteFacQuery = "DELETE FROM API_FACTURE WHERE ID_LOCATION = ? AND ID_TAXI = ?";
        try (
                PreparedStatement deleteFac = dbConnect.prepareStatement(deleteFacQuery)
        ) {
            deleteFac.setInt(1, idLoc);
            deleteFac.setInt(2, idVehicule);
            int responseFac = deleteFac.executeUpdate();

            if (responseFac == 0) {
                logger.error("Erreur lors de la suppression de la facture ou pas de facture trouvée pour la location : " + idLoc);
            }

            if (responseFac != 0) {
                return true;
            }

        } catch (SQLException e) {
            logger.error("Erreur sql lors du remove : " + e);
        }
        return false;
    }

    @Override
    public BigDecimal prixTotalLocation(Location location) {
        //Appel de fonction SGBD (4)
        String totalLoc = "{? = call api_fonc_prix_location(?)}";

        try (CallableStatement cs = dbConnect.prepareCall(totalLoc)) {
            cs.registerOutParameter(1, Types.DECIMAL);

            cs.setInt(2, location.getId());
            cs.execute();

            BigDecimal total = cs.getBigDecimal(1);

            return total;

        } catch (SQLException e) {
            logger.error("Erreur sql lors de prixTotalLocation : " + e);
        }

        return null;
    }
}
