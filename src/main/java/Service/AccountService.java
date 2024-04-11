package Service;

import java.util.List;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    /**
     * No-args contructor for a accountSevice instantiates a plain accountDAO.
     */
    public AccountService() {
        accountDAO = new AccountDAO();
    }

    /**
     * Constructor for a accounService when a accountDAO is provided.
     * 
     * @param accountDAO a accountDAO.
     */
    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    /**
     * Use the accountDAO to retrieve all authors.
     *
     * @return all Accounts.
     */
    public List<Account> getAllAccounts() {
        return accountDAO.getAllAccounts();
    }

    /**
     * Use accountDAO to add a new account to the database.
     *
     * @param account an account object.
     * @return Account if added successful.
     */
    public Account addAccount(Account account) {
        return accountDAO.insertAccount(account);
    }

    /**
     * Use accountDAO to login to a account with provided username and password.
     * 
     * @param account a account.
     * @return Account if login is successful.
     */
    public Account login(Account account) {
        return accountDAO.loginAccount(account.getUsername(), account.getPassword());
    }

}
