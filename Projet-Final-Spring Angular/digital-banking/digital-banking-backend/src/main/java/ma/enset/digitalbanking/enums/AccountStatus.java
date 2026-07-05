package ma.enset.digitalbanking.enums;

/**
 * État d'un compte bancaire au cours de son cycle de vie.
 */
public enum AccountStatus {
    CREATED,     // compte créé, pas encore activé
    ACTIVATED,   // compte activé et utilisable
    SUSPENDED    // compte suspendu / bloqué
}
