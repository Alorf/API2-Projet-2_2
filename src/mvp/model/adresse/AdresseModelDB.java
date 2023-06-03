package mvp.model.adresse;

import designpatterns.builder.Adresse;
import designpatterns.builder.Client;
import designpatterns.builder.Location;
import mvp.model.DAO;
import myconnections.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AdresseModelDB implements DAO<Adresse>, AdresseSpecial {

    private Connection dbConnect;

    private static final Logger logger = LogManager.getLogger(AdresseModelDB.class);

    public AdresseModelDB() {
        dbConnect = DBConnection.getConnection();
        if (dbConnect == null) {
            logger.error("erreur de connexion");
            System.exit(1);
        }
    }

    @Override
    public Adresse add(Adresse adresse) {
        int cp = adresse.getCp();
        String localite = adresse.getLocalite();
        String rue = adresse.getRue();
        String num = adresse.getNum();

        String ajoutAdresse = "INSERT INTO API_ADRESSE(CP, LOCALITE, RUE, NUM) VALUES (?, ?, ?, ?)";
        String getAdresseId = "SELECT ID_ADRESSE FROM API_ADRESSE WHERE CP=? AND LOCALITE = ? AND RUE = ? AND NUM = ?";

        try (PreparedStatement req1 = dbConnect.prepareStatement(ajoutAdresse);
             PreparedStatement req2 = dbConnect.prepareStatement(getAdresseId)
        ) {
            req1.setInt(1, cp);
            req1.setString(2, localite);
            req1.setString(3, rue);
            req1.setString(4, num);

            int response = req1.executeUpdate();

            if (response == 1) {
                req2.setInt(1, cp);
                req2.setString(2, localite);
                req2.setString(3, rue);
                req2.setString(4, num);
                ResultSet rs = req2.executeQuery();

                if (rs.next()) {
                    int idAdresse = rs.getInt(1);
                    adresse.setId(idAdresse);
                    return adresse;
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
    public Adresse read(int idRech) {
        String query = "SELECT * FROM API_ADRESSE WHERE ID_ADRESSE = ?";

        try (PreparedStatement req = dbConnect.prepareStatement(query)) {
            req.setInt(1, idRech);
            ResultSet rs = req.executeQuery();

            if (rs.next()) {
                int cp = rs.getInt(2);
                String localite = rs.getString(3);
                String rue = rs.getString(4);
                String num = rs.getString(5);

                Adresse adresse = new Adresse.AdresseBuilder()
                        .setId(idRech)
                        .setCp(cp)
                        .setLocalite(localite)
                        .setRue(rue)
                        .setNum(num)
                        .build();

                return adresse;

            } else {
                logger.error("Record introuvable lors du read");
            }
        } catch (SQLException e) {
            logger.error("Erreur sql lors du read : " + e);
        } catch (Exception e) {
            logger.error("Erreur AdresseBuilder lors du read : " + e);
        }

        return null;

    }

    @Override
    public boolean update(Adresse adresseModifie) {

        String query = "UPDATE API_ADRESSE SET CP = ?, LOCALITE = ?, RUE = ?, NUM = ? WHERE ID_ADRESSE = ?";
        try (PreparedStatement req = dbConnect.prepareStatement(query)) {

            req.setInt(1, adresseModifie.getCp());
            req.setString(2, adresseModifie.getLocalite());
            req.setString(3, adresseModifie.getRue());
            req.setString(4, adresseModifie.getNum());
            req.setInt(5, adresseModifie.getId());

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
    public boolean remove(int idAdresse) {

        String deleteQuery = "DELETE FROM API_ADRESSE WHERE ID_ADRESSE = ?";

        try (PreparedStatement delete = dbConnect.prepareStatement(deleteQuery)) {
            delete.setInt(1, idAdresse);
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
    public List<Adresse> getAll() {
        List<Adresse> la = new ArrayList<>();
        String query = "SELECT * FROM API_ADRESSE ORDER BY ID_ADRESSE";
        try (PreparedStatement req = dbConnect.prepareStatement(query)) {
            ResultSet rs = req.executeQuery(query);

            while (rs.next()) {
                int idAdresse = rs.getInt(1);
                int cp = rs.getInt(2);
                String localite = rs.getString(3);
                String rue = rs.getString(4);
                String num = rs.getString(5);

                Adresse a = new Adresse.AdresseBuilder()
                        .setId(idAdresse)
                        .setCp(cp)
                        .setLocalite(localite)
                        .setRue(rue)
                        .setNum(num)
                        .build();

                la.add(a);
            }

            return la;
        } catch (SQLException e) {
            logger.error("Erreur sql lors du getAll : " + e);
        } catch (Exception e) {
            logger.error("Erreur AdresseBuilder lors du getAll : " + e);
        }

        return null;
    }

    @Override
    public List<Location> locationParAdresse(Adresse adresse) {
        List<Location> locs = new ArrayList<>();
        String query = "SELECT * FROM API_LOCATIONPARADRESSE WHERE ID_ADRESSE = ?";

        try (PreparedStatement req = dbConnect.prepareStatement(query)) {
            req.setInt(1, adresse.getId());

            ResultSet rs = req.executeQuery();
            boolean trouve = false;
            while (rs.next()) {
                trouve = true;
                int idClient = rs.getInt("id_client");
                String mailClient = rs.getString("mail");
                String nomClient = rs.getString("nom");
                String prenomClient = rs.getString("prenom");
                String telClient = rs.getString("tel");

                int idLoc = rs.getInt("id_location");
                LocalDate dateloc = rs.getDate("dateloc").toLocalDate();
                int kmTotal = rs.getInt("kmtotal");

                Client client = new Client.ClientBuilder()
                        .setId(idClient)
                        .setMail(mailClient)
                        .setNom(nomClient)
                        .setPrenom(prenomClient)
                        .setTel(telClient)
                        .build();

                Location loc = new Location.LocationBuilder()
                        .setId(idLoc)
                        .setDate(dateloc)
                        .setKmTotal(kmTotal)
                        .setClient(client)
                        .setAdrDepart(adresse)
                        .build(false);

                locs.add(loc);
            }

            if (!trouve) {
                logger.error("Record introuvable");
            } else{
                return locs;
            }
        } catch (SQLException e) {
            logger.error("Erreur sql : " + e);
        } catch (Exception e) {
            logger.error("Erreur Builder : " + e);
        }

        return null;
    }
}
