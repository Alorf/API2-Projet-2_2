package mvp.model.client;

import designpatterns.builder.*;
import mvp.model.DAO;
import myconnections.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
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
            logger.error("Erreur de connexion");
            System.exit(1);
        }
    }

    @Override
    public Client add(Client client) {
        //Appel procédure SGBD (1)
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

            cs.executeUpdate();

            int idCli = cs.getInt(5);
            client.setId(idCli);

            return client;


        } catch (SQLException e) {
            logger.error("Erreur sql lors de l'ajout : " + e);
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

                Client client = new Client.ClientBuilder()
                        .setId(idRech)
                        .setMail(mailClient)
                        .setNom(nomClient)
                        .setPrenom(prenomClient)
                        .setTel(telClient)
                        .build();

                List<Location> locations = new ArrayList<>();

                do {
                    int idLoc = rs.getInt("id_location");
                    if (idLoc == 0) {
                        break;
                    }
                    LocalDate dateloc = rs.getDate("dateloc").toLocalDate();
                    int kmTotalLoc = rs.getInt("kmtotal");

                    int idAdresse = rs.getInt("id_adresse");
                    int cpAdresse = rs.getInt("cp");
                    String localiteAdresse = rs.getString("localite");
                    String rueAdresse = rs.getString("rue");
                    String numAdresse = rs.getString("num");

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
                            .setKmTotal(kmTotalLoc)
                            .setClient(client)
                            .setAdrDepart(adresse)
                            .build(false);

                    List<Facturation> facs = getFacturations(loc);
                    loc.setFacturation(facs);

                    locations.add(loc);

                } while (rs.next());


                client.setLocations(locations);
                return client;

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

    private List<Facturation> getFacturations(Location loc) {
        String query = "SELECT * FROM API_FACTURESLOCATION WHERE ID_LOCATION = ?";

        try (PreparedStatement req = dbConnect.prepareStatement(query)) {
            req.setInt(1, loc.getId());
            ResultSet rs = req.executeQuery();

            List<Facturation> facs = new ArrayList<>();

            if (rs.next()) {
                //le if permet de savoir si il y a une facturation ou pas pour une location
                do {


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

                } while (rs.next());
            }


            return facs;
        } catch (SQLException e) {
            logger.error("Erreur sql lors du getFacturations: " + e);
        } catch (Exception e) {
            logger.error("Erreur builder lors du getFacturations : " + e);
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
                logger.error("Erreur sql lors de l'update : Record introuvable");
            }
        } catch (SQLException e) {
            logger.error("Erreur sql lors de l'update : " + e);
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
                logger.error("Record introuvable lors du remove");
            }

        } catch (SQLException e) {
            logger.error("Erreur sql lors du remove : " + e);
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

                Client cl = new Client.ClientBuilder()
                        .setId(idCli)
                        .setMail(mail)
                        .setNom(nom)
                        .setPrenom(prenom)
                        .setTel(tel)
                        .build();
                lc.add(cl);
            }

            return lc;
        } catch (SQLException e) {
            logger.error("Erreur sql lors du getAll : " + e);
        } catch (Exception e) {
            logger.error("Erreur builder lors du getAll : " + e);
        }

        return null;
    }

    @Override
    public Set<Taxi> taxiUtiliseSansDoublon(Client client) {
        Set<Taxi> lt = new HashSet<>();
        String query = "SELECT ID_TAXI, IMMATRICULATION, CARBURANT, PRIXKM FROM API_TAXIPARCLIENT TAXPARCLI JOIN API_TAXI TAX ON TAXPARCLI.TAXI = TAX.IMMATRICULATION WHERE ID_CLIENT=?";

        try (PreparedStatement req = dbConnect.prepareStatement(query)) {
            req.setInt(1, client.getId());

            ResultSet rs = req.executeQuery();

            boolean trouve = false;

            while (rs.next()) {
                trouve = true;
                int idTaxi = rs.getInt(1);
                String immatriculation = rs.getString(2);
                String carburant = rs.getString(3);
                double prixKm = rs.getDouble(4);

                Taxi taxi = new Taxi.TaxiBuilder()
                        .setId(idTaxi)
                        .setImmatriculation(immatriculation)
                        .setCarburant(carburant)
                        .setPrixKm(prixKm)
                        .build();

                lt.add(taxi);
            }

            if (!trouve) {
                logger.error("Record introuvable lors de taxiUtiliseSansDoublon");
            } else {
                return lt;
            }
        } catch (SQLException e) {
            logger.error("Erreur sql lors de taxiUtiliseSansDoublon : " + e);
        } catch (Exception e) {
            logger.error("Erreur Builder lors de taxiUtiliseSansDoublon : " + e);
        }

        return lt;
    }

    @Override
    public List<Location> locationEntreDeuxDates(Client client, LocalDate d1, LocalDate d2) {
        List<Location> ll = new ArrayList<>();

        String query = "SELECT * FROM API_LOCATIONCLIENTADRESSE WHERE ID_CLIENT = ? AND DATELOC BETWEEN to_date(?) AND to_date(?)";

        try (PreparedStatement req = dbConnect.prepareStatement(query)) {
            req.setInt(1, client.getId());
            req.setDate(2, Date.valueOf(d1));
            req.setDate(3, Date.valueOf(d2));
            ResultSet rs = req.executeQuery();

            boolean trouve = false;
            while (rs.next()) {
                trouve = true;

                int idLoc = rs.getInt("id_location");
                LocalDate dateloc = rs.getDate("dateloc").toLocalDate();
                int kmTotalLoc = rs.getInt("kmtotal");

                int idAdresse = rs.getInt("id_adresse");
                int cpAdresse = rs.getInt("cp");
                String localiteAdresse = rs.getString("localite");
                String rueAdresse = rs.getString("rue");
                String numAdresse = rs.getString("num");

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
                        .setKmTotal(kmTotalLoc)
                        .setClient(client)
                        .setAdrDepart(adresse)
                        .build(false);

                ll.add(loc);
            }

            if (!trouve) {
                logger.error("Record introuvable lors de locationEntreDeuxDates");
            } else {
                return ll;
            }
        } catch (SQLException e) {
            logger.error("Erreur sql lors de locationEntreDeuxDates : " + e);
        } catch (Exception e) {
            logger.error("Erreur Builder lors de locationEntreDeuxDates : " + e);
        }

        return ll;
    }

    @Override
    public Set<Adresse> adresseLocationSansDoublon(Client client) {
        Set<Adresse> la = new HashSet<>();

        String query = "SELECT DISTINCT API_ADRESSE.ID_ADRESSE, CP, LOCALITE, RUE, NUM FROM API_ADRESSE JOIN API_LOCATION AL on API_ADRESSE.ID_ADRESSE = AL.ID_ADRESSE WHERE ID_CLIENT = ?";

        try (PreparedStatement req = dbConnect.prepareStatement(query)) {
            req.setInt(1, client.getId());

            ResultSet rs = req.executeQuery();

            boolean trouve = false;
            while (rs.next()) {
                trouve = true;

                int idAdresse = rs.getInt("id_adresse");
                int cpAdresse = rs.getInt("cp");
                String localiteAdresse = rs.getString("localite");
                String rueAdresse = rs.getString("rue");
                String numAdresse = rs.getString("num");

                Adresse adresse = new Adresse.AdresseBuilder()
                        .setId(idAdresse)
                        .setCp(cpAdresse)
                        .setLocalite(localiteAdresse)
                        .setRue(rueAdresse)
                        .setNum(numAdresse)
                        .build();

                la.add(adresse);
            }

            if (!trouve) {
                logger.error("Record introuvable lors de adresseLocationSansDoublon");
            } else {
                return la;
            }
        } catch (SQLException e) {
            logger.error("Erreur sql lors de adresseLocationSansDoublon : " + e);
        } catch (Exception e) {
            logger.error("Erreur Builder lors de adresseLocationSansDoublon : " + e);
        }

        return la;
    }

    @Override
    public List<Location> locations(Client client) {
        return client.getLocations();
    }

    @Override
    public List<Facturation> facturations(Client client) {
        /*
            bouger la méthode vers la classe client ?
            Non car je ne considère pas cela comme une opération spéciale mais une information
         */

        List<Facturation> facs = new ArrayList<>();

        for (Location location : client.getLocations()) {
            if (location.getFacturations() != null && !location.getFacturations().isEmpty()) {
                facs.addAll(location.getFacturations());
            }
        }

        return facs.isEmpty() ? null : facs;
    }

    @Override
    public int nombreLocation(Client client) {
        //Appel de fonction SGBD (3)
        String nombreLoc = "{? = call api_fonc_nombre_location_client(?)}";

        try (CallableStatement cs = dbConnect.prepareCall(nombreLoc)) {
            cs.registerOutParameter(1, Types.INTEGER);

            cs.setString(2, client.getMail());
            cs.execute();

            int nbre = cs.getInt(1);

            return nbre;

        } catch (SQLException e) {
            logger.error("Erreur sql lors de nombreLocation : " + e);
        }

        return -1;
    }

    @Override
    public BigDecimal prixTotalLocs(Client client) {
        //Appel de fonction SGBD (6)
        String prixTotalLocs = "{? = call api_fonc_prix_total_loc_client(?)}";

        try (CallableStatement cs = dbConnect.prepareCall(prixTotalLocs)) {
            cs.registerOutParameter(1, Types.INTEGER);

            cs.setString(2, client.getMail());
            cs.execute();

            BigDecimal prixTotal = cs.getBigDecimal(1);

            return prixTotal;

        } catch (SQLException e) {
            logger.error("Erreur sql lors de prixTotalLocs : " + e);
        }

        return null;
    }
}
