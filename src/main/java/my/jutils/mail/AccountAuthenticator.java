package my.jutils.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Account username and password authenticator.
 *
 * @author Erieze and Einar Lagera
 */
public class AccountAuthenticator extends Authenticator {

    private final PasswordAuthentication auth;

    /**
     * Authenticate the account using the specified username and password.
     *
     * @param user Username
     * @param password Password
     */
    public AccountAuthenticator(String user, String password) {
        auth = new PasswordAuthentication(user, password);
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        return auth;
    }

}
