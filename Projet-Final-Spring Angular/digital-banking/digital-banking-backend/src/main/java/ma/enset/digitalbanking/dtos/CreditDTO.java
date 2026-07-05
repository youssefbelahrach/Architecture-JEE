package ma.enset.digitalbanking.dtos;

import lombok.Data;

/** Corps de requête pour un versement (crédit). */
@Data
public class CreditDTO {
    private String accountId;
    private double amount;
    private String description;
}
