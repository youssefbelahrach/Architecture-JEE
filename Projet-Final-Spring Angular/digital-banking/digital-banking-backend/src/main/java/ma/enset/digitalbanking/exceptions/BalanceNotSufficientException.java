package ma.enset.digitalbanking.exceptions;

/** Levée lors d'un débit/virement quand le solde est insuffisant. */
public class BalanceNotSufficientException extends Exception {
    public BalanceNotSufficientException(String message) {
        super(message);
    }
}
