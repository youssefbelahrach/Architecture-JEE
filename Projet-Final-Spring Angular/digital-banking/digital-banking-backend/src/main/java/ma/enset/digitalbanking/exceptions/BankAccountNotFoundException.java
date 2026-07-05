package ma.enset.digitalbanking.exceptions;

/** Levée lorsqu'un compte demandé n'existe pas. */
public class BankAccountNotFoundException extends Exception {
    public BankAccountNotFoundException(String message) {
        super(message);
    }
}
