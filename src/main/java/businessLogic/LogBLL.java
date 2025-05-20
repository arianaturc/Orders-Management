package businessLogic;

import dataAccess.LogDAO;
import dataModel.Bill;

import java.util.List;

/**
 * Business Logic Layer class for retrieving billing logs.
 */
public class LogBLL {

    private final LogDAO logDAO = new LogDAO();

    public LogBLL() {

    }
    /**
     * Retrieves all billing entries.
     *
     * @return a list of all bills
     */
    public List<Bill> findAllProducts() {
        return logDAO.findAll();
    }
}
