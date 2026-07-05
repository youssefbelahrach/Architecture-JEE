package ma.enset.digitalbanking.dtos;

import lombok.Data;

import java.util.List;

/** Historique paginé d'un compte (renvoyé par l'endpoint pageOperations). */
@Data
public class AccountHistoryDTO {
    private String accountId;
    private double balance;
    private int currentPage;
    private int totalPages;
    private int pageSize;
    private List<AccountOperationDTO> accountOperationDTOS;
}
