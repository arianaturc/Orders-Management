package businessLogic;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import businessLogic.validators.EmailValidator;
import businessLogic.validators.ClientAgeValidator;
import businessLogic.validators.Validator;
import dataAccess.ClientDAO;
import dataModel.Client;

public class ClientBLL {

    private List<Validator<Client>> validators;
    private ClientDAO clientDAO = new ClientDAO();

    public ClientBLL() {
        validators = new ArrayList<Validator<Client>>();
        validators.add(new EmailValidator());
        validators.add(new ClientAgeValidator());

        clientDAO = new ClientDAO();
    }

    public Client findClientById(int id) {
        Client st = clientDAO.findById(id);
        if (st == null) {
            throw new NoSuchElementException("The Client with id =" + id + " was not found!");
        }
        return st;
    }

    public List<Client> findAllClients() {
        return clientDAO.findAll();
    }

    public Client insertClient(Client client) {
        validators.forEach(v -> v.validate(client));
        return clientDAO.insert(client);
    }

    public void updateClient(Client client) {
        validators.forEach(v -> v.validate(client));
        clientDAO.update(client);
    }

    public void deleteClient(int clientId) {
        clientDAO.delete(clientId);
    }

}
