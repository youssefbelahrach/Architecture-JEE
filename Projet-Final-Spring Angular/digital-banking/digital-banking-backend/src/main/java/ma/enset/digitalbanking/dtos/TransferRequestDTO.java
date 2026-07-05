package ma.enset.digitalbanking.dtos;

import lombok.Data;

/** Corps de requête pour un virement entre deux comptes. */
@Data
public class TransferRequestDTO {
    private String accountSource;
    private String accountDestination;
    private double amount;
}
