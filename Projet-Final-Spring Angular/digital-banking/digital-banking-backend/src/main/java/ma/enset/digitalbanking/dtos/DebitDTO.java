package ma.enset.digitalbanking.dtos;

import lombok.Data;

/** Corps de requête pour un retrait (débit). */
@Data
public class DebitDTO {
    private String accountId;
    private double amount;
    private String description;
}
