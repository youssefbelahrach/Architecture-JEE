package ma.enset.digitalbanking.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Compte courant : possède une autorisation de découvert (overDraft).
 * Enregistré avec la valeur discriminante "CA".
 */
@Entity
@DiscriminatorValue("CA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentAccount extends BankAccount {

    /** Montant maximal de découvert autorisé. */
    private double overDraft;
}
