package ma.enset.digitalbanking.services;

import ma.enset.digitalbanking.dtos.*;
import ma.enset.digitalbanking.exceptions.BalanceNotSufficientException;
import ma.enset.digitalbanking.exceptions.BankAccountNotFoundException;
import ma.enset.digitalbanking.exceptions.CustomerNotFoundException;

import java.util.List;

/**
 * Contrat métier de l'application bancaire.
 */
public interface BankAccountService {

    // ---- Clients ----
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    List<CustomerDTO> listCustomers();
    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;
    CustomerDTO updateCustomer(CustomerDTO customerDTO);
    void deleteCustomer(Long customerId);
    List<CustomerDTO> searchCustomers(String keyword);

    // ---- Comptes ----
    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId)
            throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId)
            throws CustomerNotFoundException;
    List<BankAccountDTO> bankAccountList();
    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;

    // ---- Opérations ----
    void debit(String accountId, double amount, String description)
            throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId, double amount, String description)
            throws BankAccountNotFoundException;
    void transfer(String accountIdSource, String accountIdDestination, double amount)
            throws BankAccountNotFoundException, BalanceNotSufficientException;

    // ---- Historique ----
    List<AccountOperationDTO> accountHistory(String accountId);
    AccountHistoryDTO getAccountHistory(String accountId, int page, int size)
            throws BankAccountNotFoundException;
}
