package mvp.model.client;

import locationTaxi.metier.*;
import mvp.model.DAO;
import myconnections.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ClientModelHyb implements DAO<Client>, ClientSpecial {

    private Connection dbConnect;

    private static final Logger logger = LogManager.getLogger(ClientModelDB.class);

    public ClientModelHyb() {
        dbConnect = DBConnection.getConnection();
        if (dbConnect == null) {
            logger.error("erreur de connexion");
            System.exit(1);
        }
    }

    @Override
    public Client add(Client client) {
        //fixme : voir si cela marche
        String mail = client.getMail();
        String nom = client.getNom();
        String prenom = client.getPrenom();
        String tel = client.getTel();

        String ajoutCli = "CALL API_PROC_CREATE_CLIENT(?, ?, ?, ?, ?)";

        try (CallableStatement cs = dbConnect.prepareCall(ajoutCli)) {
            cs.setString(1, mail);
            cs.setString(2, nom);
            cs.setString(3, prenom);
            cs.setString(4, tel);


            cs.registerOutParameter(5, Types.INTEGER);

            int response = cs.executeUpdate();

            if (response != 0){
                int idCli = cs.getInt(5);
                client.setId(idCli);

                return client;
            }

        } catch (SQLException e) {
            logger.error("Erreur sql : " + e);
        }

        return null;
    }

    @Override
    public Client read(int idRech) {
        String query = "SELECT * FROM API_LOCATIONCLIENTADRESSE WHERE ID_CLIENT = ?";

        try (PreparedStatement req = dbConnect.prepareStatement(query)) {
            req.setInt(1, idRech);
            ResultSet rs = req.executeQuery();

            if (rs.next()) {

                //int idClient = rs.getInt(1);
                String mailClient = rs.getString("mail");
                String nomClient = rs.getString("nom");
                String prenomClient = rs.getString("prenom");
                String telClient = rs.getString("tel");

                Client client = new Client(idRech, mailClient, nomClient, prenomClient, telClient);

                List<Location> locations = new ArrayList<>();

                do {
                    int idLoc = rs.getInt("id_location");
                    LocalDate dateloc = rs.getDate("dateloc").toLocalDate();
                    int kmTotalLoc = rs.getInt("kmtotal");

                    int idAdresse = rs.getInt("id_adresse");
                    int cpAdresse = rs.getInt("cp");
                    String localiteAdresse = rs.getString("localite");
                    String rueAdresse = rs.getString("rue");
                    String numAdresse = rs.getString("num");

                    Adresse adresse = new Adresse(idAdresse, cpAdresse, localiteAdresse, rueAdresse, numAdresse);

                    Location loc = new Location(idLoc, dateloc, kmTotalLoc, client, adresse);

                    List<Facturation> facs = getFacturations(loc);
                    loc.setFacturation(facs);

                    locations.add(loc);

                } while (rs.next());


                client.setLocations(locations);
                return client;

            } else {
                logger.error("Record introuvable");
            }
        } catch (SQLException e) {
            logger.error("Erreur sql : " + e);
        }

        return null;

    }

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
    public boolean update(Client clientModifie) {

        String query = "UPDATE API_TCLIENT SET MAIL = ?, NOM = ?, PRENOM = ?, TEL = ? WHERE ID_CLIENT = ?";
        try (PreparedStatement req = dbConnect.prepareStatement(query)) {

            req.setString(1, clientModifie.getMail());
            req.setString(2, clientModifie.getNom());
            req.setString(3, clientModifie.getPrenom());
            req.setString(4, clientModifie.getTel());
            req.setInt(5, clientModifie.getId());

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
    public boolean remove(int idClient) {

        String deleteQuery = "DELETE FROM API_TCLIENT WHERE ID_CLIENT = ?";

        try (PreparedStatement delete = dbConnect.prepareStatement(deleteQuery)) {
            delete.setInt(1, idClient);
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
    public List<Client> getAll() {
        List<Client> lc = new ArrayList<>();
        String query = "SELECT * FROM API_TCLIENT ORDER BY ID_CLIENT";
        try (PreparedStatement req = dbConnect.prepareStatement(query)) {
            ResultSet rs = req.executeQuery(query);

            while (rs.next()) {
                int idCli = rs.getInt(1);
                String mail = rs.getString(2);
                String nom = rs.getString(3);
                String prenom = rs.getString(4);
                String tel = rs.getString(5);

                Client cl = new Client(idCli, mail, nom, prenom, tel);
                lc.add(cl);
            }

            return lc;
        } catch (SQLException e) {
            logger.error("Erreur sql : " + e);
        }

        return null;
    }

    @Override
    public Set<Taxi> taxiUtiliseSansDoublon(Client client) {
        return client.taxiUtiliseSansDoublon();
    }

    @Override
    public List<Location> locationEntreDeuxDates(Client client, LocalDate d1, LocalDate d2) {
        return client.locationEntreDeuxDates(d1, d2);
    }

    @Override
    public Set<Adresse> adresseLocationSansDoublon(Client client) {
        return client.adresseLocationSansDoublon();
    }

    @Override
    public List<Location> locations(Client client) {
        return client.getLocations();
    }

    public List<Facturation> facturations(Client client) {
        //todo : bouger la méthode vers la classe client ?

        List<Facturation> facs = new ArrayList<>();

        for (Location location : client.getLocations()) {
            facs.addAll(location.getFacturations());
        }

        return facs.isEmpty() ? null : facs;
    }

    @Override
    public int nombreLocation(Client client) {
        //Utilisation d'une fonction de SGBD
        //fixme : ne marche pas
        String nombreLoc = "CALL API_FONC_NOMBRE_LOCATION_CLIENT(?)";

        try (CallableStatement cs = dbConnect.prepareCall(nombreLoc)) {
            cs.setString(1, client.getMail());
            int response = cs.executeUpdate();

            System.out.println(response);

            int idCli = cs.getInt(1);
            client.setId(idCli);

            return 1;

        } catch (SQLException e) {
            logger.error("Erreur sql : " + e);
        }

        return -1;
    }
}
