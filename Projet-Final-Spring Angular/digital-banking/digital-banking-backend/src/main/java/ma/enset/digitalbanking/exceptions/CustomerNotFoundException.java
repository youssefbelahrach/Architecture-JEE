package ma.enset.digitalbanking.exceptions;

/** Levée lorsqu'un client demandé n'existe pas. */
public class CustomerNotFoundException extends Exception {
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
