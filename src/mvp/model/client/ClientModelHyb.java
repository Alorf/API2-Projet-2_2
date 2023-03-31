package mvp.model.client;

import locationTaxi.*;
import myconnections.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClientModelHyb implements DAOClient, ClientSpecial {

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
    public Client addClient(Client client) {
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
    public Client readClient(int idRech) {
        String query = "SELECT * FROM API_LAFTC WHERE ID_CLIENT = ?";

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

                int idLoc = rs.getInt("id_location");
                if (idLoc != 0) {
                    do {
                        idLoc = rs.getInt("id_location");
                        LocalDate dateloc = rs.getDate("dateloc").toLocalDate();
                        int kmTotalLoc = rs.getInt("kmtotal");

                        int idAdresse = rs.getInt("id_adresse");
                        int cpAdresse = rs.getInt("cp");
                        String localiteAdresse = rs.getString("localite");
                        String rueAdresse = rs.getString("rue");
                        String numAdresse = rs.getString("num");

                        Adresse adresse = new Adresse(idAdresse, cpAdresse, localiteAdresse, rueAdresse, numAdresse);

                        int coutFac = rs.getInt("cout");

                        int idTaxi = rs.getInt("id_taxi");
                        String immatriculationTaxi = rs.getString("immatriculation");
                        String carburantTaxi = rs.getString("carburant");
                        double prixKmTaxi = rs.getDouble("prixkm");

                        Taxi taxi = new Taxi(idTaxi, immatriculationTaxi, carburantTaxi, prixKmTaxi);

                        Facturation fac = new Facturation(coutFac, taxi);
                        Location loc = new Location(idLoc, dateloc, kmTotalLoc, client, adresse);

                        loc.getFacturations().add(fac);
                        //todo : mais si une location a plusieurs taxis ?

                        locations.add(loc);

                    } while (rs.next());
                }

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

    @Override
    public boolean updateClient(Client clientModifie) {

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
    public boolean removeClient(int idClient) {

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
    public List<Client> getClients() {
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
/*
    @Override
    public Set<Taxi> taxiUtiliseSansDoublon(Client client) {
        return client.taxiUtiliseSansDoublon();
    }
 */

    @Override

    public Set<Taxi> taxiUtiliseSansDoublon(Client client) {
        Set<Taxi> lt = new HashSet<>();
        String query = "SELECT ID_TAXI, IMMATRICULATION, CARBURANT, PRIXKM FROM API_TAXIPARCLIENT TAXPARCLI JOIN API_TAXI TAX ON TAXPARCLI.TAXI = TAX.IMMATRICULATION WHERE CLIENT=?";

        try (PreparedStatement req = dbConnect.prepareStatement(query)) {
            //todo: Mettre cela dans un try catch ?
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

    @Override
    public List<Facturation> facturations(Client client) {
        List<Facturation> facs = new ArrayList<>();
        String query = "SELECT * FROM API_FACTURE";
        try (PreparedStatement req = dbConnect.prepareStatement(query)) {
            req.setInt(1, client.getId());
            ResultSet rs = req.executeQuery();

            boolean trouve = false;
            while (rs.next()) {
                trouve = true;
                double coutFac = rs.getDouble("cout");

                int idTaxi = rs.getInt("id_taxi");
                String immatriculationTaxi = rs.getString("immatriculation");
                String carburantTaxi = rs.getString("carburant");
                double prixKmTaxi = rs.getDouble("prixkm");

                Taxi taxi = new Taxi(idTaxi, immatriculationTaxi, carburantTaxi, prixKmTaxi);
                Facturation facturation = new Facturation(coutFac, taxi);

                facs.add(facturation);
            }
            if (!trouve) System.out.println("aucune commande trouvée");
        } catch (SQLException e) {
            logger.error("erreur sql : " + e);
            return null;
        }
        return null;
    }
}
