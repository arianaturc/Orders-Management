package businessLogic;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import businessLogic.validators.EmailValidator;
import businessLogic.validators.ClientAgeValidator;
import businessLogic.validators.Validator;
import dataAccess.ClientDAO;
import dataModel.Client;

/**
 * Business Logic Layer class for handling client-related operations.
 * It uses validators to ensure data integrity.
 */
public class ClientBLL {

    private List<Validator<Client>> validators;
    private ClientDAO clientDAO = new ClientDAO();

    /**
     * Initializes the client business logic layer with validators.
     */
    public ClientBLL() {
        validators = new ArrayList<Validator<Client>>();
        validators.add(new EmailValidator());
        validators.add(new ClientAgeValidator());

        clientDAO = new ClientDAO();
    }

    /**
     * Finds a client by ID.
     *
     * @param id The ID of the client.
     * @return The found client.
     * @throws NoSuchElementException if the client is not found.
     */
    public Client findClientById(int id) {
        Client st = clientDAO.findById(id);
        if (st == null) {
            throw new NoSuchElementException("The Client with id =" + id + " was not found!");
        }
        return st;
    }

    /**
     * Retrieves all clients from the database.
     *
     * @return a list of all clients
     */
    public List<Client> findAllClients() {
        return clientDAO.findAll();
    }

    /**
     * Inserts a new client after its validation.
     *
     * @param client the client to insert
     * @return the inserted client
     */
    public Client insertClient(Client client) {
        validators.forEach(v -> v.validate(client));
        return clientDAO.insert(client);
    }


    /**
     * Updates an existing client after validation.
     *
     * @param client the client to update
     */
    public void updateClient(Client client) {
        validators.forEach(v -> v.validate(client));
        clientDAO.update(client);
    }

    /**
     * Deletes a client by ID.
     *
     * @param clientId the ID of the client to delete
     */
    public void deleteClient(int clientId) {
        clientDAO.delete(clientId);
    }

}
