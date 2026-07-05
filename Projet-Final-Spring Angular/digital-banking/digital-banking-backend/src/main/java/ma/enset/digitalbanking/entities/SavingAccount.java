package ma.enset.digitalbanking.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Compte épargne : possède un taux d'intérêt (interestRate).
 * Enregistré avec la valeur discriminante "SA".
 */
@Entity
@DiscriminatorValue("SA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavingAccount extends BankAccount {

    /** Taux d'intérêt appliqué au compte. */
    private double interestRate;
}
