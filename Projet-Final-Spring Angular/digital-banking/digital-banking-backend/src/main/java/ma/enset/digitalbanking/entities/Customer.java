package ma.enset.digitalbanking.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Un client de la banque. Un client peut posséder plusieurs comptes.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    /**
     * Relation 1..N : un client -> plusieurs comptes.
     * mappedBy indique que c'est BankAccount.customer qui porte la clé étrangère.
     * @JsonProperty(ACCESS.WRITE_ONLY) évite les boucles infinies lors de la
     * sérialisation JSON (client -> comptes -> client -> ...).
     */
    @OneToMany(mappedBy = "customer")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<BankAccount> bankAccounts;
}
