package mvp.model.client;

import locationTaxi.*;

import java.time.LocalDate;
import java.util.List;

public interface DAOClient {

    Client addClient(Client client);

    Client readClient(int idRech);

    boolean updateClient(Client clientModifie);

    boolean removeClient(int idCli);

    List<Client> getClients();

}
