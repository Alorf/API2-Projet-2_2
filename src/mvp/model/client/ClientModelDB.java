package mvp.model.client;

import locationTaxi.metier.*;
import mvp.model.DAO;
import myconnections.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClientModelDB implements DAO<Client>, ClientSpecial {

    private Connection dbConnect;

    private static final Logger logger = LogManager.getLogger(ClientModelDB.class);

    public ClientModelDB() {
        dbConnect = DBConnection.getConnection();
        if (dbConnect == null) {
            logger.error("erreur de connexion");
            System.exit(1);
        }
    }

    @Override
    public Client add(Client client) {
        String mail = client.getMail();
        String nom = client.getNom();
        String prenom = client.getPrenom();
        String tel = client.getTel();

        String ajoutCli = "INSERT INTO API_TCLIENT(MAIL, NOM, PRENOM, TEL) VALUES (?, ?, ?, ?)";
        String getCliId = "SELECT ID_CLIENT FROM API_TCLIENT WHERE MAIL=?";

        try (PreparedStatement req1 = dbConnect.prepareStatement(ajoutCli);
             PreparedStatement req2 = dbConnect.prepareStatement(getCliId)
        ) {
            req1.setString(1, mail);
            req1.setString(2, nom);
            req1.setString(3, prenom);
            req1.setString(4, tel);

            int response = req1.executeUpdate();

            if (response == 1) {
                req2.setString(1, mail);
                ResultSet rs = req2.executeQuery();

                if (rs.next()) {
                    int idCli = rs.getInt(1);
                    client.setId(idCli);
                    return client;
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
    public Client read(int idRech) {
        String query = "SELECT * FROM API_TCLIENT WHERE ID_CLIENT = ?";

        try (PreparedStatement req = dbConnect.prepareStatement(query)) {
            req.setInt(1, idRech);
            ResultSet rs = req.executeQuery();

            if (rs.next()) {

                //int idClient = rs.getInt(1);
                String mail = rs.getString(2);
                String nom = rs.getString(3);
                String prenom = rs.getString(4);
                String tel = rs.getString(5);

                Client client = new Client(idRech, mail, nom, prenom, tel);
                return client;

            } else {
                logger.error("Record introuvable");
            }
        } catch (SQLException e) {
            logger.error("Erreur sql : " + e);
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
            }else{
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
        Set<Taxi> lt = new HashSet<>();
        String query = "SELECT ID_TAXI, IMMATRICULATION, CARBURANT, PRIXKM FROM API_TAXIPARCLIENT TAXPARCLI JOIN API_TAXI TAX ON TAXPARCLI.TAXI = TAX.IMMATRICULATION WHERE CLIENT=?";

        try (PreparedStatement req = dbConnect.prepareStatement(query)) {
            req.setString(1, client.getNom() + " " + client.getPrenom());
            ResultSet rs = req.executeQuery();

            boolean trouve = false;
            while (rs.next()){
                trouve=true;
                Taxi taxi = new Taxi(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4));
                logger.info(taxi);
                lt.add(taxi);
            }

            if (!trouve){
                logger.error("Record introuvable");
            }else{
                return lt;
            }
        } catch (SQLException e) {
            logger.error("Erreur sql : " + e);
        }

        return lt;
    }

    @Override
    public List<Location> locationEntreDeuxDates(Client client, LocalDate d1, LocalDate d2){
        List<Location> ll = new ArrayList<>();

        String query = "SELECT * FROM API_LOCATION WHERE ID_CLIENT = ? AND DATELOC BETWEEN to_date(?) AND to_date(?)";

        try (PreparedStatement req = dbConnect.prepareStatement(query)) {
            req.setInt(1, client.getId());
            req.setDate(2, Date.valueOf(d1));
            req.setDate(3, Date.valueOf(d2));
            ResultSet rs = req.executeQuery();

            boolean trouve = false;
            while (rs.next()){
                trouve=true;
                Location loc = new Location(rs.getInt(1), rs.getDate(2).toLocalDate(), rs.getInt(3), client, null);
                logger.info(loc);
                ll.add(loc);
            }

            if (!trouve){
                logger.error("Record introuvable");
            }else{
                return ll;
            }
        } catch (SQLException e) {
            logger.error("Erreur sql : " + e);
        }

        return ll;
    }

    @Override
    public Set<Adresse> adresseLocationSansDoublon(Client client){
        Set<Adresse> la = new HashSet<>();

        String query = "SELECT DISTINCT API_ADRESSE.ID_ADRESSE, CP, LOCALITE, RUE, NUM FROM API_ADRESSE JOIN API_LOCATION AL on API_ADRESSE.ID_ADRESSE = AL.ID_ADRESSE WHERE ID_CLIENT = ?";

        try (PreparedStatement req = dbConnect.prepareStatement(query)) {
            req.setInt(1, client.getId());

            ResultSet rs = req.executeQuery();

            boolean trouve = false;
            while (rs.next()){
                trouve=true;
                Adresse adresse = new Adresse(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5));
                la.add(adresse);
            }

            if (!trouve){
                logger.error("Record introuvable");
            }else{
                return la;
            }
        } catch (SQLException e) {
            logger.error("Erreur sql : " + e);
        }

        return la;
    }

    @Override
    public List<Location> locations(Client client) {
        return null;
    }

    @Override
    public List<Facturation> facturations(Client client) {
        return null;
    }

    @Override
    public int nombreLocation(Client client) {
        //todo : utiliser la fonction
        return 0;
    }
}
