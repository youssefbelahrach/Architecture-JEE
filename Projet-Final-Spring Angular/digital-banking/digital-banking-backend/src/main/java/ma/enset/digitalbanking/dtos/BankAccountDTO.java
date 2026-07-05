package ma.enset.digitalbanking.dtos;

import lombok.Data;
import ma.enset.digitalbanking.enums.AccountStatus;

import java.util.Date;

/**
 * DTO mère d'un compte. Le champ "type" ("CurrentAccount"/"SavingAccount")
 * permet au frontend de distinguer le sous-type.
 * Grâce à l'héritage, Jackson sérialise aussi les champs des sous-classes.
 */
@Data
public class BankAccountDTO {
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private String currency;
    private CustomerDTO customerDTO;
    private String type;
}
