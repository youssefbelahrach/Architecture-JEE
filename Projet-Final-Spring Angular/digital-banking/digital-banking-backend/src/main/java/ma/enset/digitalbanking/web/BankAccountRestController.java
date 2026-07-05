package ma.enset.digitalbanking.web;

import lombok.AllArgsConstructor;
import ma.enset.digitalbanking.dtos.*;
import ma.enset.digitalbanking.exceptions.BalanceNotSufficientException;
import ma.enset.digitalbanking.exceptions.BankAccountNotFoundException;
import ma.enset.digitalbanking.services.BankAccountService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class BankAccountRestController {

    private BankAccountService bankAccountService;

    @GetMapping("/accounts")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public List<BankAccountDTO> listAccounts() {
        return bankAccountService.bankAccountList();
    }

    @GetMapping("/accounts/{accountId}")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);
    }

    /** Historique complet (non paginé). */
    @GetMapping("/accounts/{accountId}/operations")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public List<AccountOperationDTO> getHistory(@PathVariable String accountId) {
        return bankAccountService.accountHistory(accountId);
    }

    /** Historique paginé, utilisé par le frontend. */
    @GetMapping("/accounts/{accountId}/pageOperations")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public AccountHistoryDTO getAccountHistory(
            @PathVariable String accountId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(accountId, page, size);
    }

    // Les opérations qui modifient les soldes sont réservées aux administrateurs
    @PostMapping("/accounts/debit")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public DebitDTO debit(@RequestBody DebitDTO debitDTO)
            throws BankAccountNotFoundException, BalanceNotSufficientException {
        bankAccountService.debit(debitDTO.getAccountId(), debitDTO.getAmount(), debitDTO.getDescription());
        return debitDTO;
    }

    @PostMapping("/accounts/credit")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public CreditDTO credit(@RequestBody CreditDTO creditDTO) throws BankAccountNotFoundException {
        bankAccountService.credit(creditDTO.getAccountId(), creditDTO.getAmount(), creditDTO.getDescription());
        return creditDTO;
    }

    @PostMapping("/accounts/transfer")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void transfer(@RequestBody TransferRequestDTO transferRequestDTO)
            throws BankAccountNotFoundException, BalanceNotSufficientException {
        bankAccountService.transfer(
                transferRequestDTO.getAccountSource(),
                transferRequestDTO.getAccountDestination(),
                transferRequestDTO.getAmount());
    }
}
