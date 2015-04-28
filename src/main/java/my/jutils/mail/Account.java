package my.jutils.mail;

/**
 * Account Information. <br />
 * I recommend to retrieve this data from database, or just store here at
 * program <br />
 * statically.
 *
 * @author Erieze
 */
public interface Account {

    /**
     * Email account username.
     *
     * @return Username
     */
    public String username();

    /**
     * Email account password.
     *
     * @return Your precious
     */
    public String password();

}
